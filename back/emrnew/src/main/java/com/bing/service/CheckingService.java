package com.bing.service;

import com.bing.entity.Checking;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bing.form.createForm;
import com.bing.form.searchFormChecking;
import com.bing.vo.PageVO;
import com.bing.vo.ResultVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2024-03-15
 */
public interface CheckingService extends IService<Checking> {

    public ResultVO create(createForm createForm);

    public PageVO checkingsearch(searchFormChecking searchFormChecking);

    public Boolean resetchecking(searchFormChecking searchFormChecking);
}
