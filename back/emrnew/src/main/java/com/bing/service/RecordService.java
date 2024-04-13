package com.bing.service;

import com.bing.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bing.form.createForm;
import com.bing.form.decryptForm;
import com.bing.form.searchFormDoctor;
import com.bing.form.searchFormPatient;
import com.bing.vo.DecryptVO;
import com.bing.vo.PageVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2023-03-19
 */
public interface RecordService extends IService<Record> {

    public Boolean create(createForm createForm) throws Exception;

    //查询返回pageVO（数据+分页信息）
    public PageVO doctorlist(searchFormDoctor searchFormDoctor) throws Exception;
    public PageVO doctorsearch(searchFormDoctor searchFormDoctor);
    public PageVO doctorview(searchFormDoctor searchFormDoctor);

    public PageVO patientlist(Integer page, Integer size, Integer id) throws Exception;
    public PageVO patientsearch(searchFormPatient searchFormPatient);

    public Boolean affirmupdate(Integer id) throws Exception;

    public DecryptVO decrypt(decryptForm decryptForm) throws Exception;
}
