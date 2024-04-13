package com.bing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bing.entity.Authorize;
import com.bing.entity.Doctor;
import com.bing.entity.Patient;
import com.bing.entity.Record;
import com.bing.form.createForm;
import com.bing.form.decryptForm;
import com.bing.form.searchFormDoctor;
import com.bing.form.searchFormPatient;
import com.bing.mapper.AuthorizeMapper;
import com.bing.mapper.DoctorMapper;
import com.bing.mapper.PatientMapper;
import com.bing.mapper.RecordMapper;
import com.bing.service.RecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bing.util.CommonUtil;
import com.bing.util.RSAUtil;
import com.bing.vo.DecryptVO;
import com.bing.vo.PageVO;
import com.bing.vo.RecordVO;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2023-03-19
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    @Autowired
    private PatientMapper patientMapper;
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private AuthorizeMapper authorizeMapper;


    @Override
    public Boolean create(createForm createForm) throws Exception {
        //查询病人id+新增数据
        //判断用户（手机号）是否存在
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone",createForm.getPhone());
        Patient patient = this.patientMapper.selectOne(queryWrapper);
        if(patient == null){
            return false;
        }else{ //判断姓名是否一致
            if(!patient.getName().equals(createForm.getName())){
                return false;
            }else{
                //由手机号转化为id
                Record record = new Record();
                record.setPatientid(patient.getId());
                record.setDoctorid(createForm.getDoctorid());
                record.setCreatetime(CommonUtil.createDate());
                record.setHospital(createForm.getHospital());
                record.setOffice(createForm.getOffice());
                record.setAffirm(0);
                //病历内容加密
                record.setDescription(RSAUtil.encrypt(createForm.getDescription(),patient.getPublickey()));
                record.setRemark(RSAUtil.encrypt(createForm.getRemark(),patient.getPublickey()));
                int insert = this.recordMapper.insert(record);
                if(insert != 1) return false;
            }
        }
        return  true;
    }

    @Override
    public PageVO doctorlist(searchFormDoctor searchFormDoctor) throws Exception {
        Page<Record> recordPage = new Page<>(searchFormDoctor.getPage(),searchFormDoctor.getSize());
        Page<Record> resultPage = null;
        List<Record> recordList = this.recordMapper.selectRecordByDoctor(null,null,null,null,null);

        //无查询结果时
        if(recordList.size()==0){
            resultPage = new Page<>(0, 0, 0);
        }else { //有查询结果时
            int page = searchFormDoctor.getPage();
            int size = searchFormDoctor.getSize();
            int total = recordList.size();
            // 计算总页数
            int pages = (total + size - 1) / size;
            // 如果请求的页码大于总页数，则返回最后一页
            if (page > pages) {
                page = pages;
            }
            // 计算当前页的记录起始索引和结束索引
            int startIndex = (page - 1) * size;
            int endIndex = Math.min(startIndex + size, total);
            // 构造分页对象
            resultPage = new Page<>(page, size, total);
            resultPage.setRecords(recordList.subList(startIndex, endIndex));

            System.out.println(resultPage);
        }

        recordList = resultPage.getRecords();
        //VO转换
        List<RecordVO> recordVOList = new ArrayList<>();
        for(Record record : recordList){
            RecordVO recordVO = new RecordVO();
            Patient patient = this.patientMapper.selectById(record.getPatientid());
            Doctor doctor = this.doctorMapper.selectById(record.getDoctorid());
            Authorize authorize= this.authorizeMapper.selectByRecordidAndDoctorid(record.getId(),searchFormDoctor.getDoctorid());
            recordVO.setId(record.getId());
            recordVO.setCreatetime(record.getCreatetime());
            recordVO.setName(patient.getName());
            recordVO.setPhone(patient.getPhone());
            recordVO.setSex(patient.getSex());
            //计算就诊时年龄(病历创建时间-出生日期)
            // 将字符串转换为LocalDate对象
            LocalDate birthday = LocalDate.parse(patient.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate createtime = LocalDate.parse(record.getCreatetime(), DateTimeFormatter.ISO_LOCAL_DATE);
            // 计算年龄
            Period period = Period.between(birthday, createtime);
            recordVO.setAge(period.getYears());

            recordVO.setOffice(doctor.getOffice());
            //弹框中的数据
            recordVO.setHospital(doctor.getHospital());
            if (authorize != null && authorize.getDescription() != null && authorize.getDescription().length() != 0){
                recordVO.setDescription(authorize.getDescription());
            }
            if (authorize != null && authorize.getRemark() != null && authorize.getRemark().length() != 0){
                recordVO.setRemark(authorize.getRemark());
            }
            recordVOList.add(recordVO);
            //判断是否有权限查看档案
            Authorize authorize2 = this.recordMapper.haveAutority(Integer.valueOf(searchFormDoctor.getDoctorid()),record.getId());
            if(authorize2 != null){
                recordVO.setHaveauthority(1);
            }else{
                recordVO.setHaveauthority(0);
            }
        }

        PageVO pageVO = new PageVO();
        pageVO.setData(recordVOList);
        pageVO.setTotal(resultPage.getTotal());
        return pageVO;
    }

    @Override
    public PageVO doctorsearch(searchFormDoctor searchFormDoctor){
        Page<Record> recordPage = new Page<>(searchFormDoctor.getPage(), searchFormDoctor.getSize());
        Page<Record> resultPage = null;

        List<Record> recordList = new ArrayList<>();
        recordList = recordMapper.selectRecordByDoctor(searchFormDoctor.getRecordid(),searchFormDoctor.getName(), searchFormDoctor.getPhone(),searchFormDoctor.getHospital(),searchFormDoctor.getOffice());
        System.out.println(recordList);

        //无查询结果时
        if(recordList.size()==0){
            resultPage = new Page<>(0, 0, 0);
        }else { //有查询结果时
            int page = searchFormDoctor.getPage();
            int size = searchFormDoctor.getSize();
            int total = recordList.size();
            // 计算总页数
            int pages = (total + size - 1) / size;
            // 如果请求的页码大于总页数，则返回最后一页
            if (page > pages) {
                page = pages;
            }
            // 计算当前页的记录起始索引和结束索引
            int startIndex = (page - 1) * size;
            int endIndex = Math.min(startIndex + size, total);
            // 构造分页对象
            resultPage = new Page<>(page, size, total);
            resultPage.setRecords(recordList.subList(startIndex, endIndex));

            System.out.println(resultPage);
        }

        recordList = resultPage.getRecords();
        //VO转换
        List<RecordVO> recordVOList = new ArrayList<>();
        for(Record record : recordList){
            RecordVO recordVO = new RecordVO();
            Patient patient = this.patientMapper.selectById(record.getPatientid());
            Doctor doctor = this.doctorMapper.selectById(record.getDoctorid());
            Authorize authorize= this.authorizeMapper.selectByRecordidAndDoctorid(record.getId(),searchFormDoctor.getDoctorid());
            recordVO.setId(record.getId());
            recordVO.setCreatetime(record.getCreatetime());
            recordVO.setName(patient.getName());
            recordVO.setPhone(patient.getPhone());
            recordVO.setSex(patient.getSex());
            //计算就诊时年龄(病历创建时间-出生日期)
            // 将字符串转换为LocalDate对象
            LocalDate birthday = LocalDate.parse(patient.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate createtime = LocalDate.parse(record.getCreatetime(), DateTimeFormatter.ISO_LOCAL_DATE);
            // 计算年龄
            Period period = Period.between(birthday, createtime);
            recordVO.setAge(period.getYears());

            recordVO.setOffice(doctor.getOffice());
            recordVO.setHospital(doctor.getHospital());
            recordVO.setAffirm(record.getAffirm());
            //弹框中的数据
            recordVO.setHospital(doctor.getHospital());
            if (authorize != null && authorize.getDescription() != null && authorize.getDescription().length() != 0){
                recordVO.setDescription(authorize.getDescription());
            }
            if (authorize != null && authorize.getRemark() != null && authorize.getRemark().length() != 0){
                recordVO.setRemark(authorize.getRemark());
            }
            recordVOList.add(recordVO);

            //判断是否有权限查看档案
            Authorize authorize2 = this.recordMapper.haveAutority(Integer.valueOf(searchFormDoctor.getDoctorid()),record.getId());
            if(authorize2 != null){
                recordVO.setHaveauthority(1);
            }else{
                recordVO.setHaveauthority(0);
            }
        }
        PageVO pageVO = new PageVO();
        pageVO.setData(recordVOList);
        pageVO.setTotal(resultPage.getTotal());
        return pageVO;
    }

    @Override
    public PageVO doctorview(searchFormDoctor searchFormDoctor) {
        Page<Record> recordPage = new Page<>(searchFormDoctor.getPage(), searchFormDoctor.getSize());
        Page<Record> resultPage = null;

        List<Record> recordList = new ArrayList<>();
        recordList = recordMapper.selectRecordByDoctor(searchFormDoctor.getRecordid(),searchFormDoctor.getName(), searchFormDoctor.getPhone(),searchFormDoctor.getHospital(),searchFormDoctor.getOffice());
        System.out.println(recordList);

        //无查询结果时
        if(recordList.size()==0){
            resultPage = new Page<>(0, 0, 0);
        }else { //有查询结果时
            int page = searchFormDoctor.getPage();
            int size = searchFormDoctor.getSize();
            int total = recordList.size();
            // 计算总页数
            int pages = (total + size - 1) / size;
            // 如果请求的页码大于总页数，则返回最后一页
            if (page > pages) {
                page = pages;
            }
            // 计算当前页的记录起始索引和结束索引
            int startIndex = (page - 1) * size;
            int endIndex = Math.min(startIndex + size, total);
            // 构造分页对象
            resultPage = new Page<>(page, size, total);
            resultPage.setRecords(recordList.subList(startIndex, endIndex));

            System.out.println(resultPage);
        }

        recordList = resultPage.getRecords();
        //VO转换
        List<RecordVO> recordVOList = new ArrayList<>();
        for(Record record : recordList){
            //先判断是否有权限查看档案
            Authorize authorize2 = this.recordMapper.haveAutority(Integer.valueOf(searchFormDoctor.getDoctorid()),record.getId());
            if(authorize2 == null){
                continue;
            }
            RecordVO recordVO = new RecordVO();
            Patient patient = this.patientMapper.selectById(record.getPatientid());
            Doctor doctor = this.doctorMapper.selectById(record.getDoctorid());
            Authorize authorize= this.authorizeMapper.selectByRecordidAndDoctorid(record.getId(),searchFormDoctor.getDoctorid());
            recordVO.setId(record.getId());
            recordVO.setCreatetime(record.getCreatetime());
            recordVO.setName(patient.getName());
            recordVO.setPhone(patient.getPhone());
            recordVO.setSex(patient.getSex());
            //计算就诊时年龄(病历创建时间-出生日期)
            // 将字符串转换为LocalDate对象
            LocalDate birthday = LocalDate.parse(patient.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate createtime = LocalDate.parse(record.getCreatetime(), DateTimeFormatter.ISO_LOCAL_DATE);
            // 计算年龄
            Period period = Period.between(birthday, createtime);
            recordVO.setAge(period.getYears());

            recordVO.setOffice(doctor.getOffice());
            recordVO.setHospital(doctor.getHospital());
            recordVO.setAffirm(record.getAffirm());
            //弹框中的数据
            recordVO.setHospital(doctor.getHospital());
            if (authorize != null && authorize.getDescription() != null && authorize.getDescription().length() != 0){
                recordVO.setDescription(authorize.getDescription());
            }
            if (authorize != null && authorize.getRemark() != null && authorize.getRemark().length() != 0){
                recordVO.setRemark(authorize.getRemark());
            }
            recordVOList.add(recordVO);
        }
        PageVO pageVO = new PageVO();
        pageVO.setData(recordVOList);
        pageVO.setTotal(resultPage.getTotal());
        return pageVO;
    }

    @Override
    public PageVO patientlist(Integer page,Integer size,Integer id) throws Exception {
        //System.out.println("id:"+id);
        Page<Record> recordPage = new Page<>(page,size);
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("patientid",id);
        Page<Record> resultPage = this.recordMapper.selectPage(recordPage,queryWrapper);
        List<Record> recordList = resultPage.getRecords();
        //VO转换
        List<RecordVO> recordVOList = new ArrayList<>();
        for(Record record : recordList){
            RecordVO recordVO = new RecordVO();
            Patient patient = this.patientMapper.selectById(record.getPatientid());
            Doctor doctor = this.doctorMapper.selectById(record.getDoctorid());
            recordVO.setId(record.getId());
            recordVO.setCreatetime(record.getCreatetime());
            recordVO.setName(patient.getName());
            recordVO.setPhone(patient.getPhone());
            recordVO.setSex(patient.getSex());
            //计算就诊时年龄(病历创建时间-出生日期)
            // 将字符串转换为LocalDate对象
            LocalDate birthday = LocalDate.parse(patient.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate createtime = LocalDate.parse(record.getCreatetime(), DateTimeFormatter.ISO_LOCAL_DATE);
            // 计算年龄
            Period period = Period.between(birthday, createtime);
            recordVO.setAge(period.getYears());

            recordVO.setOffice(doctor.getOffice());
            //弹框中的数据
            recordVO.setHospital(doctor.getHospital());
            recordVO.setAffirm(record.getAffirm());
            recordVO.setDescription(record.getDescription());
            recordVO.setRemark(record.getRemark());
            recordVOList.add(recordVO);
        }

        PageVO pageVO = new PageVO();
        pageVO.setData(recordVOList);
        pageVO.setTotal(resultPage.getTotal());
        return pageVO;
    }

    @Override
    public PageVO patientsearch(searchFormPatient searchFormPatient){
        Page<Record> recordPage = new Page<>(searchFormPatient.getPage(), searchFormPatient.getSize());
        Page<Record> resultPage = null;
        System.out.println("111"+searchFormPatient);
        //无查询条件
        if(searchFormPatient.getRecordid()==null && searchFormPatient.getHospital().equals("") && searchFormPatient.getOffice().equals("")){
            QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("patientid", searchFormPatient.getPatientid());
            resultPage = this.recordMapper.selectPage(recordPage,queryWrapper);
        }else{  //有查询条件
            List<Record> recordList = recordMapper.selectRecordByPatient(searchFormPatient.getPatientid(),searchFormPatient.getRecordid(),searchFormPatient.getHospital(),searchFormPatient.getOffice());

            //无查询结果时
            if(recordList.size()==0){
                resultPage = new Page<>(0, 0, 0);
            }else { //有查询结果时
                int page = searchFormPatient.getPage();
                int size = searchFormPatient.getSize();
                int total = recordList.size();
                // 计算总页数
                int pages = (total + size - 1) / size;
                // 如果请求的页码大于总页数，则返回最后一页
                if (page > pages) {
                    page = pages;
                }
                // 计算当前页的记录起始索引和结束索引
                int startIndex = (page - 1) * size;
                int endIndex = Math.min(startIndex + size, total);
                // 构造分页对象
                resultPage = new Page<>(page, size, total);
                resultPage.setRecords(recordList.subList(startIndex, endIndex));
            }
        }

        List<Record> recordList = resultPage.getRecords();
        //VO转换
        List<RecordVO> recordVOList = new ArrayList<>();
        for(Record record : recordList){
            RecordVO recordVO = new RecordVO();
            Patient patient = this.patientMapper.selectById(record.getPatientid());
            Doctor doctor = this.doctorMapper.selectById(record.getDoctorid());
            recordVO.setId(record.getId());
            recordVO.setCreatetime(record.getCreatetime());
            recordVO.setName(patient.getName());
            recordVO.setPhone(patient.getPhone());
            recordVO.setSex(patient.getSex());
            //计算就诊时年龄(病历创建时间-出生日期)
            // 将字符串转换为LocalDate对象
            LocalDate birthday = LocalDate.parse(patient.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate createtime = LocalDate.parse(record.getCreatetime(), DateTimeFormatter.ISO_LOCAL_DATE);
            // 计算年龄
            Period period = Period.between(birthday, createtime);
            recordVO.setAge(period.getYears());

            recordVO.setOffice(doctor.getOffice());
            recordVO.setHospital(doctor.getHospital());
            recordVO.setAffirm(record.getAffirm());
            recordVO.setDescription(record.getDescription());
            recordVO.setRemark(record.getRemark());
            recordVOList.add(recordVO);
        }

        PageVO pageVO = new PageVO();
        pageVO.setData(recordVOList);
        pageVO.setTotal(resultPage.getTotal());
        return pageVO;
    }

    @Override
    public Boolean affirmupdate(Integer id) throws Exception {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id",id);
        updateWrapper.set("affirm",1);
        int update = baseMapper.update(null,updateWrapper);
        if(update != 1) return false;

        Record record = this.recordMapper.selectById(id);

        //确认后的record上链
        String configFile = "D:\\Desktop\\医疗记录存储系统\\back\\emrnew\\src\\main\\resources\\config-example.toml";
        BcosSDK sdk = BcosSDK.build(configFile);

//        String configFile = "src/main/resources/config-example.toml";
//        // 初始化BcosSDK对象
//        BcosSDK sdk = BcosSDK.build(configFile);
        // 获取Client对象，此处传入的群组ID为1
        Client client = sdk.getClient(Integer.valueOf(1));
        // 构造AssembleTransactionProcessor对象，需要传入client对象，CryptoKeyPair对象和abi、binary文件存放的路径。abi和binary文件需要在上一步复制到定义的文件夹中。
        CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();
        // 确保这些是正确的目录路径
        String abiDir = "D:\\Desktop\\医疗记录存储系统\\back\\emrnew\\src\\main\\resources\\abi\\";
        String binDir = "D:\\Desktop\\医疗记录存储系统\\back\\emrnew\\src\\main\\resources\\bin\\";  // 假设二进制文件也在 resources 目录下

// 创建 AssembleTransactionProcessor 对象
        AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(
                client, keyPair, abiDir, binDir);
//        AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(client, keyPair, "src/main/resources/abi/", "");
        //同步方式发送交易
        // 创建调用交易函数的参数
        String recordId = Integer.toString(id);
        String description = record.getDescription();
        String remark = record.getRemark();

        List<Object> params = new ArrayList<>();
        params.add(recordId);
        params.add(description);
        params.add(remark);

        // 调用record合约，调用函数名为『addRecord』，函数参数类型为params
        TransactionResponse transactionResponse = transactionProcessor.sendTransactionAndGetResponseByContractLoader("record", "0x0b8b1143cf95b61f9b96d8872d22161c9bd96c34", "addRecord", params);

        // 打印返回值
        List<Object> returnValues = transactionResponse.getReturnObject();
        if (returnValues != null) {
            for (Object value : returnValues) {
                System.out.println("上链返回值："+value.toString());
                if(value.toString().equals(1)) return false;
            }
        }

        return true;
    }

    @Override
    public DecryptVO decrypt(decryptForm decryptForm) throws Exception {
        DecryptVO decryptVO = new DecryptVO();
        String privateKey = decryptForm.getPrivatekey();
        try {
            String decryptedDescription = RSAUtil.decrypt(decryptForm.getDescription(), privateKey);
            decryptVO.setDescription(decryptedDescription);

            if (decryptForm.getRemark() != null && decryptForm.getRemark().length() != 0) {
                String decryptedRemark = RSAUtil.decrypt(decryptForm.getRemark(), privateKey);
                decryptVO.setRemark(decryptedRemark);
            }

            decryptVO.setSuccess(true); // 设置解密成功标志
        } catch (Exception e) {
            decryptVO.setSuccess(false); // 设置解密失败标志
        }
        System.out.println("description+"+decryptForm.getDescription());
        System.out.println("privatekey+"+decryptForm.getPrivatekey());
        System.out.println("success?+"+decryptVO.getSuccess());
        return decryptVO;
    }
}
