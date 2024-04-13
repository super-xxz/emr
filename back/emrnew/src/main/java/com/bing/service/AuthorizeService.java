package com.bing.service;

import com.bing.entity.Authorize;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bing.form.applyForm;
import com.bing.form.authorizeForm;
import com.bing.vo.PageVO;
import com.bing.vo.ResultVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2023-03-19
 */
public interface AuthorizeService extends IService<Authorize> {
    public Boolean apply(applyForm applyForm);

    //查询返回pageVO（数据+分页信息）
    public PageVO list(Integer page, Integer size, Integer id);

    public Boolean applyupdate(authorizeForm authorizeForm) throws Exception;
    public Boolean cancelupdate(Integer id);
}
