package com.bing.service;

import com.bing.entity.Patient;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bing.form.changePasswordForm;
import com.bing.form.changePhoneForm;
import com.bing.form.loginForm;
import com.bing.form.patientRegisterForm;
import com.bing.vo.ResultVO;

import java.security.NoSuchAlgorithmException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2023-03-19
 */
public interface PatientService extends IService<Patient> {
    public ResultVO patientregister(patientRegisterForm patientRegisterForm) throws NoSuchAlgorithmException;

    public ResultVO login(loginForm loginForm);

    public Boolean updatephone(changePhoneForm changePhoneForm);

    public Boolean updatepassword(changePasswordForm changePasswordForm);

    public ResultVO createnewkey(Integer patientid) throws NoSuchAlgorithmException;
}
