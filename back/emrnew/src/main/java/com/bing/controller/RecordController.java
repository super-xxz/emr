package com.bing.controller;

import com.bing.form.createForm;
import com.bing.form.decryptForm;
import com.bing.form.searchFormDoctor;
import com.bing.form.searchFormPatient;
import com.bing.service.RecordService;
import com.bing.util.ResultVOUtil;
import com.bing.vo.DecryptVO;
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
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    //新建病历
    @PostMapping("/create")
    public ResultVO create(@RequestBody createForm createForm) throws Exception {
        System.out.println("createForm:"+createForm);
        Boolean create = this.recordService.create(createForm);
        if(!create) return ResultVOUtil.fail();
        return ResultVOUtil.success(null);
    }

    //医生端初始化病历列表
    @GetMapping("/doctorlist")
    public ResultVO doctorlist(searchFormDoctor searchFormDoctor) throws Exception {
        return ResultVOUtil.success(this.recordService.doctorlist(searchFormDoctor));
    }

    //医生端查询病历
    @GetMapping("/doctorsearch")
    public ResultVO doctorsearch(searchFormDoctor searchFormDoctor){
        return ResultVOUtil.success(this.recordService.doctorsearch(searchFormDoctor));
    }

    //医生端查看已授权病历
    @GetMapping("/doctorview")
    public ResultVO doctorview(searchFormDoctor searchFormDoctor){
        return ResultVOUtil.success(this.recordService.doctorview(searchFormDoctor));
    }

    //患者端查看病历
    @GetMapping("/patientlist/{page}/{size}/{id}")
    public ResultVO patientlist(@PathVariable("page") Integer page,@PathVariable("size") Integer size,@PathVariable("id") Integer id) throws Exception {
        return ResultVOUtil.success(this.recordService.patientlist(page,size,id));
    }

    //患者端查询病历
    @GetMapping("/patientsearch")
    public ResultVO patientsearch(searchFormPatient searchFormPatient){
        return ResultVOUtil.success(this.recordService.patientsearch(searchFormPatient));
    }

    //确认病历
    @PutMapping("/affirmupdate/{id}")
    public ResultVO affirmupdate(@PathVariable("id") Integer id) throws Exception {
        Boolean update = this.recordService.affirmupdate(id);
        if(!update) return ResultVOUtil.fail();
        return ResultVOUtil.success(null);
    }

    //病历解密
    @GetMapping("/decrypt")
    public ResultVO decrypt(decryptForm decryptForm) throws Exception {
        return ResultVOUtil.success(this.recordService.decrypt(decryptForm));
    }
}

