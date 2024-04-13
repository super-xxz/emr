package com.bing.controller;


import com.bing.form.createForm;
import com.bing.form.searchFormChecking;
import com.bing.form.searchFormDoctor;
import com.bing.service.CheckingService;
import com.bing.util.ResultVOUtil;
import com.bing.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2024-03-15
 */
@RestController
@RequestMapping("/checking")
public class CheckingController {
    @Autowired
    private CheckingService checkingService;

    //新建资质码
    @PostMapping("/create")
    public ResultVO create(@RequestBody createForm createForm) throws Exception {
        ResultVO resultVO = this.checkingService.create(createForm);
        return resultVO;
    }

    //查看资质码
    @GetMapping("/checkingsearch")
    public ResultVO checkingsearch(searchFormChecking searchFormChecking){
        return ResultVOUtil.success(this.checkingService.checkingsearch(searchFormChecking));
    }

    //重置资质码
    @PostMapping("/resetchecking")
    public ResultVO resetchecking(@RequestBody searchFormChecking searchFormChecking){
        Boolean update = this.checkingService.resetchecking(searchFormChecking);
        if(!update) return ResultVOUtil.fail();
        return ResultVOUtil.success(null);
    }
}

