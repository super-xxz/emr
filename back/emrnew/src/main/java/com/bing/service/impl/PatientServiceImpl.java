package com.bing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bing.entity.Patient;
import com.bing.form.changePasswordForm;
import com.bing.form.changePhoneForm;
import com.bing.form.loginForm;
import com.bing.form.patientRegisterForm;
import com.bing.mapper.PatientMapper;
import com.bing.service.PatientService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bing.util.RSAUtil;
import com.bing.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2023-03-19
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Autowired
    private PatientMapper patientMapper;

    @Override
    public ResultVO patientregister(patientRegisterForm patientRegisterForm) throws NoSuchAlgorithmException {
        //判断用户（手机号）是否已存在
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone",patientRegisterForm.getPhone());
        Patient patient = this.patientMapper.selectOne(queryWrapper);

        ResultVO resultVO = new ResultVO();
        if(patient != null){
            resultVO.setCode(-1);
        }else{
            Patient patient2 = new Patient();
            patient2.setName(patientRegisterForm.getName());
            patient2.setPhone(patientRegisterForm.getPhone());
            patient2.setPassword(patientRegisterForm.getPassword());
            patient2.setSex(patientRegisterForm.getSex());
            //日期格式转换
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = dateFormat.format(patientRegisterForm.getDate());
            patient2.setDate(dateString);
            //生成密钥对
            Map<Integer, String> keyMap = RSAUtil.genKeyPair();
            patient2.setPublickey(keyMap.get(0));
            System.out.println("111"+keyMap.get(0));
            int insert = this.patientMapper.insert(patient2);
            if(insert != 1) resultVO.setCode(-2);
            resultVO.setCode(0);
            resultVO.setData(keyMap.get(1));//返回私钥
        }
        return resultVO;
    }

    @Override
    public ResultVO login(loginForm loginForm) {
        //1、判断账号（手机号）是否存在
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", loginForm.getPhone());
        Patient patient = this.patientMapper.selectOne(queryWrapper);
        //System.out.println(doctor);
        ResultVO resultVO = new ResultVO();
        if(patient == null){
            resultVO.setCode(-1);
        } else {
            //2、判断密码是否正确
            if(!patient.getPassword().equals(loginForm.getPassword())){
                resultVO.setCode(-2);
            } else {
                resultVO.setCode(0);
                resultVO.setData(patient);
            }
        }
        return resultVO;
    }

    @Override
    public Boolean updatephone(changePhoneForm changePhoneForm){
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id",changePhoneForm.getId());
        updateWrapper.set("phone",changePhoneForm.getPhone());
        int update = baseMapper.update(null,updateWrapper);
        if(update != 1) return false;
        return true;
    }

    @Override
    public Boolean updatepassword(changePasswordForm changePasswordForm) {
        //先校验旧密码是否匹配
        Patient patient = patientMapper.selectById(changePasswordForm.getId());
        if (patient == null) {
            return false;
        }
        if (!patient.getPassword().equals(changePasswordForm.getOldpassword())) {
            // 旧密码不匹配，返回false
            return false;
        }
        // 如果旧密码匹配，将新密码更新到数据库中
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id",changePasswordForm.getId());
        updateWrapper.set("password",changePasswordForm.getNewpassword());
        int update = baseMapper.update(null,updateWrapper);
        if(update != 1) return false;
        return true;
    }

    @Override
    public ResultVO createnewkey(Integer patientid) throws NoSuchAlgorithmException {
        ResultVO resultVO = new ResultVO();
        //生成密钥对
        Map<Integer, String> keyMap = RSAUtil.genKeyPair();

        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id",patientid);
        updateWrapper.set("publickey",keyMap.get(0));
        int update = baseMapper.update(null,updateWrapper);
        if(update != 1) resultVO.setCode(-1);;
        resultVO.setCode(0);;
        resultVO.setData(keyMap.get(1));//返回私钥

        return resultVO;
    }

}
