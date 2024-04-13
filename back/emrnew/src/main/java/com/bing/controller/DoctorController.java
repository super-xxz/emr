package com.bing.controller;


import com.bing.form.changePasswordForm;
import com.bing.form.changePhoneForm;
import com.bing.form.doctorRegisterForm;
import com.bing.form.loginForm;
import com.bing.service.DoctorService;
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
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    //注册
    @PostMapping("/doctorregister")
    public ResultVO doctorregister(@RequestBody doctorRegisterForm doctorRegisterForm) throws NoSuchAlgorithmException {
        ResultVO resultVO = this.doctorService.doctorregister(doctorRegisterForm);
        return resultVO;
    }

    //登录
    @GetMapping("/login")
    public ResultVO login(loginForm loginForm){
        ResultVO resultVO = this.doctorService.login(loginForm);
        return resultVO;
    }

    //修改电话
    @PutMapping("/updatephone")
    public ResultVO updatephone(@RequestBody changePhoneForm changePhoneForm){
        Boolean update = this.doctorService.updatephone(changePhoneForm);
        if(!update) return ResultVOUtil.fail();
        return ResultVOUtil.success(null);
    }

    //修改密码
    @PutMapping("/updatepassword")
    public ResultVO updatepassword(@RequestBody changePasswordForm changePasswordForm){
        Boolean update = this.doctorService.updatepassword(changePasswordForm);
        if(!update) return ResultVOUtil.fail();
        return ResultVOUtil.success(null);
    }

    //重置密钥
    @PostMapping("/createnewkey/{doctorid}")
    public ResultVO createnewkey(@PathVariable("doctorid") Integer doctorid) throws NoSuchAlgorithmException {
        ResultVO resultVO = this.doctorService.createnewkey(doctorid);
        return resultVO;
    }

}

