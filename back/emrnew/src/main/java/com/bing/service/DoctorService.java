package com.bing.service;

import com.bing.entity.Doctor;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bing.form.changePasswordForm;
import com.bing.form.changePhoneForm;
import com.bing.form.doctorRegisterForm;
import com.bing.form.loginForm;
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
public interface DoctorService extends IService<Doctor> {
    public ResultVO doctorregister(doctorRegisterForm doctorRegisterForm) throws NoSuchAlgorithmException;

    public ResultVO login(loginForm loginForm);

    public Boolean updatephone(changePhoneForm changePhoneForm);

    public Boolean updatepassword(changePasswordForm changePasswordForm);

    public ResultVO createnewkey(Integer doctorid) throws NoSuchAlgorithmException;
}
