package com.bing.service;

import com.bing.entity.Manager;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bing.form.loginForm;
import com.bing.vo.ResultVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2024-03-20
 */
public interface ManagerService extends IService<Manager> {

    public ResultVO login(loginForm loginForm);

    public ResultVO getusernumber();
}
