package com.bing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bing.entity.Checking;
import com.bing.entity.Doctor;
import com.bing.entity.Patient;
import com.bing.form.changePasswordForm;
import com.bing.form.changePhoneForm;
import com.bing.form.doctorRegisterForm;
import com.bing.form.loginForm;
import com.bing.mapper.CheckingMapper;
import com.bing.mapper.DoctorMapper;
import com.bing.service.DoctorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bing.util.RSAUtil;
import com.bing.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
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
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor> implements DoctorService {
    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private CheckingMapper checkingMapper;

    @Override
    public ResultVO doctorregister(doctorRegisterForm doctorRegisterForm) throws NoSuchAlgorithmException {
        //判断用户（手机号）是否已存在
        QueryWrapper<Doctor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone",doctorRegisterForm.getPhone());
        Doctor doctor = this.doctorMapper.selectOne(queryWrapper);
        ResultVO resultVO = new ResultVO();
        if(doctor != null){
            resultVO.setCode(-1);
            return resultVO;
        }else{
            //判断资质码是否正确
            QueryWrapper<Checking> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("hospital", doctorRegisterForm.getHospital()).eq("office", doctorRegisterForm.getOffice()).eq("checking", doctorRegisterForm.getChecking());
            Checking checking = this.checkingMapper.selectOne(queryWrapper2);
            System.out.println("111"+checking);
            if(checking == null){
                resultVO.setCode(-3);
                return resultVO;
            }else{
                Doctor doctor2 = new Doctor();
                doctor2.setName(doctorRegisterForm.getName());
                doctor2.setPhone(doctorRegisterForm.getPhone());
                doctor2.setPassword(doctorRegisterForm.getPassword());
                doctor2.setHospital(doctorRegisterForm.getHospital());
                doctor2.setOffice(doctorRegisterForm.getOffice());
                //生成密钥对
                Map<Integer, String> keyMap = RSAUtil.genKeyPair();
                doctor2.setPublickey(keyMap.get(0));
                //System.out.println("111"+keyMap.get(0));
                int insert = this.doctorMapper.insert(doctor2);
                if(insert != 1){
                    resultVO.setCode(-2);
                    return resultVO;
                }
                resultVO.setCode(0);
                resultVO.setData(keyMap.get(1));//返回私钥
                return resultVO;
            }
        }
    }

    @Override
    public ResultVO login(loginForm loginForm) {
        //1、判断账号（手机号）是否存在
        QueryWrapper<Doctor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", loginForm.getPhone());
        Doctor doctor = this.doctorMapper.selectOne(queryWrapper);
        //System.out.println(doctor);
        ResultVO resultVO = new ResultVO();
        if(doctor == null){
            resultVO.setCode(-1);
        } else {
            //2、判断密码是否正确
            if(!doctor.getPassword().equals(loginForm.getPassword())){
                resultVO.setCode(-2);
            } else {
                resultVO.setCode(0);
                resultVO.setData(doctor);
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
        Doctor doctor = doctorMapper.selectById(changePasswordForm.getId());
        if (doctor == null) {
            return false;
        }
        if (!doctor.getPassword().equals(changePasswordForm.getOldpassword())) {
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
    public ResultVO createnewkey(Integer doctorid) throws NoSuchAlgorithmException {
        ResultVO resultVO = new ResultVO();
        //生成密钥对
        Map<Integer, String> keyMap = RSAUtil.genKeyPair();

        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id",doctorid);
        updateWrapper.set("publickey",keyMap.get(0));
        int update = baseMapper.update(null,updateWrapper);
        if(update != 1) resultVO.setCode(-1);;
        resultVO.setCode(0);;
        resultVO.setData(keyMap.get(1));//返回私钥

        return resultVO;
    }
}
