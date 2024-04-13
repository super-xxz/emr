package com.bing.controller;


import com.bing.form.changePasswordForm;
import com.bing.form.changePhoneForm;
import com.bing.form.loginForm;
import com.bing.form.patientRegisterForm;
import com.bing.mapper.PatientMapper;
import com.bing.service.PatientService;
import com.bing.util.ResultVOUtil;
import com.bing.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2023-03-19
 */
@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    //注册
    @PostMapping("/patientregister")
    public ResultVO patientregister(@RequestBody patientRegisterForm patientRegisterForm) throws NoSuchAlgorithmException {
        ResultVO resultVO = this.patientService.patientregister(patientRegisterForm);
        return resultVO;
    }

    //登录
    @GetMapping("/login")
    public ResultVO login(loginForm loginForm){
        ResultVO resultVO = this.patientService.login(loginForm);
        return resultVO;
    }

    //修改电话
    @PutMapping("/updatephone")
    public ResultVO updatephone(@RequestBody changePhoneForm changePhoneForm){
        Boolean update = this.patientService.updatephone(changePhoneForm);
        if(!update) return ResultVOUtil.fail();
        return ResultVOUtil.success(null);
    }

    //修改密码
    @PutMapping("/updatepassword")
    public ResultVO updatepassword(@RequestBody changePasswordForm changePasswordForm){
        Boolean update = this.patientService.updatepassword(changePasswordForm);
        if(!update) return ResultVOUtil.fail();
        return ResultVOUtil.success(null);
    }

    //重置密钥
    @PostMapping("/createnewkey/{patientid}")
    public ResultVO createnewkey(@PathVariable("patientid") Integer patientid) throws NoSuchAlgorithmException {
        ResultVO resultVO = this.patientService.createnewkey(patientid);
        return resultVO;
    }

}

