package com.bing.controller;


import com.bing.form.applyForm;
import com.bing.form.authorizeForm;
import com.bing.service.AuthorizeService;
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
 * @since 2023-03-19
 */
@RestController
@RequestMapping("/authorize")
public class AuthorizeController {
    @Autowired
    private AuthorizeService authorizeService;

    //申请授权
    @GetMapping("/apply")
    public ResultVO apply(applyForm applyForm){
        System.out.println(applyForm);
        Boolean apply = this.authorizeService.apply(applyForm);
        if(!apply) return ResultVOUtil.fail();
        return ResultVOUtil.success(null);
    }

    //授权管理
    @GetMapping("/list/{page}/{size}/{id}")
    public ResultVO list(@PathVariable("page") Integer page, @PathVariable("size") Integer size,@PathVariable("id") Integer id){
        return ResultVOUtil.success(this.authorizeService.list(page,size,id));
    }

    //授权
    @PutMapping("/applyupdate")
    public ResultVO applyupdate(@RequestBody authorizeForm authorizeForm) throws Exception {
        //System.out.println("privateKey:"+authorizeForm.getPrivateKey());
        //System.out.println("rowId:"+authorizeForm.getRowId());
        Boolean update = this.authorizeService.applyupdate(authorizeForm);
        if(!update) return ResultVOUtil.fail();
        return ResultVOUtil.success(null);
    }

    //取消授权
    @PutMapping("/cancelupdate/{id}")
    public ResultVO cancelupdate(@PathVariable("id") Integer id){
        //System.out.println(id);
        Boolean update = this.authorizeService.cancelupdate(id);
        if(!update) return ResultVOUtil.fail();
        return ResultVOUtil.success(null);
    }

}

