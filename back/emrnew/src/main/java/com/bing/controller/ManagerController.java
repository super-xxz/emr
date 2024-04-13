package com.bing.controller;


import com.bing.form.loginForm;
import com.bing.mapper.ManagerMapper;
import com.bing.service.ManagerService;
import com.bing.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2024-03-20
 */
@RestController
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private ManagerService managerService;

    //登录
    @GetMapping("/login")
    public ResultVO login(loginForm loginForm){
        ResultVO resultVO = this.managerService.login(loginForm);
        return resultVO;
    }

    //获取系统数据
    @GetMapping("/getusernumber")
    public ResultVO getusernumber(){
        ResultVO resultVO = this.managerService.getusernumber();
        return  resultVO;
    }
}

