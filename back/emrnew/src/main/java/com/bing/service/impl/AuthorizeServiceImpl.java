package com.bing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bing.entity.Authorize;
import com.bing.entity.Doctor;
import com.bing.entity.Patient;
import com.bing.entity.Record;
import com.bing.form.applyForm;
import com.bing.form.authorizeForm;
import com.bing.mapper.AuthorizeMapper;
import com.bing.mapper.DoctorMapper;
import com.bing.mapper.PatientMapper;
import com.bing.mapper.RecordMapper;
import com.bing.service.AuthorizeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bing.util.CommonUtil;
import com.bing.util.RSAUtil;
import com.bing.vo.AuthorizeVO;
import com.bing.vo.PageVO;
import com.bing.vo.RecordVO;
import com.bing.vo.ResultVO;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class AuthorizeServiceImpl extends ServiceImpl<AuthorizeMapper, Authorize> implements AuthorizeService {

    @Autowired
    private AuthorizeMapper authorizeMapper;
    @Autowired
    private PatientMapper patientMapper;
    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private RecordMapper recordMapper;

    @Override
    public Boolean apply(applyForm applyForm){
        // 判断数据库中是否已有记录
        QueryWrapper<Authorize> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("recordid", applyForm.getRecordid())
                .eq("doctorid", applyForm.getDoctorid());
        Authorize existingAuthorize = this.authorizeMapper.selectOne(queryWrapper);
        if (existingAuthorize != null) {
            return true;
        }
        //如果不存在则插入
        Authorize authorize = new Authorize();
        authorize.setRecordid(applyForm.getRecordid());
        authorize.setDoctorid(applyForm.getDoctorid());
        authorize.setCreatetime(CommonUtil.createDate());
        authorize.setState(0);
        Record record = this.recordMapper.selectById(applyForm.getRecordid());
        authorize.setDescription("");
        authorize.setRemark("");
        int insert = this.authorizeMapper.insert(authorize);
        if(insert != 1) return false;
        else return true;
    }

    @Override
    public PageVO list(Integer page, Integer size, Integer id){
        Page<Authorize> authorizePage = new Page<>(page,size);
        Page<Authorize> resultPage = null;
        List<Authorize> authorizeList = this.authorizeMapper.myauthorize(id);
        //无查询结果时
        if(authorizeList.size()==0){
            resultPage = new Page<>(0, 0, 0);
        }else { //有查询结果时
            int total = authorizeList.size();
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
            resultPage.setRecords(authorizeList.subList(startIndex, endIndex));

            System.out.println(resultPage);
        }
        authorizeList = resultPage.getRecords();
        //VO转换
        List<AuthorizeVO> authorizeVOList = new ArrayList<>();
        for(Authorize authorize : authorizeList){
            AuthorizeVO authorizeVO = new AuthorizeVO();
            Doctor doctor = this.doctorMapper.selectById(authorize.getDoctorid());
            authorizeVO.setId(authorize.getId());
            authorizeVO.setDoctorid(authorize.getDoctorid());
            authorizeVO.setRecordid(authorize.getRecordid());
            authorizeVO.setCreatetime(authorize.getCreatetime());
            authorizeVO.setName(doctor.getName());
            authorizeVO.setOffice(doctor.getOffice());
            authorizeVO.setHospital(doctor.getHospital());
            authorizeVO.setState(authorize.getState());
            authorizeVOList.add(authorizeVO);
        }

        PageVO pageVO = new PageVO();
        pageVO.setData(authorizeVOList);
        pageVO.setTotal(resultPage.getTotal());
        return pageVO;
    }

    @Override
    //这里的rowid是authorize表的id
    public Boolean applyupdate(authorizeForm authorizeForm) throws Exception {
        Authorize authorize = this.authorizeMapper.selectById(authorizeForm.getRowId());
        Integer recordid = authorize.getRecordid();
        Integer doctorid = authorize.getDoctorid();
        Record record = this.recordMapper.selectById(recordid);
        Doctor doctor = this.doctorMapper.selectById(doctorid);

        //从区块链获取密文
        String configFile = "src/main/resources/config-example.toml";
        // 初始化BcosSDK对象
        BcosSDK sdk = BcosSDK.build(configFile);
        // 获取Client对象，此处传入的群组ID为1
        Client client = sdk.getClient(Integer.valueOf(1));
        // 构造AssembleTransactionProcessor对象，需要传入client对象，CryptoKeyPair对象和abi、binary文件存放的路径。abi和binary文件需要在上一步复制到定义的文件夹中。
        CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();
        AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(client, keyPair, "src/main/resources/abi/", "");
        //查询交易
        List<Object> params = new ArrayList<>();
        params.add(Integer.toString(recordid));
        //调用record合约的『getRecord』函数，参数为recordid
        TransactionResponse transactionResponse = transactionProcessor.sendTransactionAndGetResponseByContractLoader("record", "0x0b8b1143cf95b61f9b96d8872d22161c9bd96c34", "getRecord", params);
        // 打印返回值
        List<Object> returnValues = transactionResponse.getReturnObject();
        String chaindescription = "";
        String chainremark = "";
        if (returnValues != null) {
            // 检查返回值的长度是否正确
            if (returnValues.size() == 2) {
                chaindescription = (String) returnValues.get(0);
                chainremark = (String) returnValues.get(1);
                System.out.println("chainDescription: " + chaindescription);
                System.out.println("chainRemark: " + chainremark);
            } else {
                System.out.println("返回值长度不正确");
                return false;
            }
        }else{
            System.out.println("没有返回值");
            return false;
        }

        //密文解密再加密
        //患者私钥
        String privateKey = authorizeForm.getPrivateKey();
        //医生公钥
        String publicKey = doctor.getPublickey();

        String endescription = "";
        String enremark = "";
        try{
            String description = RSAUtil.decrypt(chaindescription,privateKey);
            endescription = RSAUtil.encrypt(description,publicKey);
            if(record.getRemark() !=null && record.getRemark().length() != 0){
                String remark = RSAUtil.decrypt(chainremark,privateKey);
                enremark = RSAUtil.encrypt(remark,publicKey);
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        System.out.println("endescription:"+endescription);
        System.out.println("enremark:"+enremark);

        //更新表
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id",authorizeForm.getRowId());
        updateWrapper.set("state",1);
        updateWrapper.set("description",endescription);
        updateWrapper.set("remark",enremark);
        int update = baseMapper.update(null,updateWrapper);
        if(update != 1) return false;
        return true;
    }

    @Override
    public Boolean cancelupdate(Integer id){
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id",id);
        updateWrapper.set("state",0);
        updateWrapper.set("description","");
        updateWrapper.set("remark","");
        int update = baseMapper.update(null,updateWrapper);
        if(update != 1) return false;
        return true;
    }
}
