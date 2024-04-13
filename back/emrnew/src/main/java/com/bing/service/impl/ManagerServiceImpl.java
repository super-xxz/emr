package com.bing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bing.entity.Doctor;
import com.bing.entity.Manager;
import com.bing.form.loginForm;
import com.bing.mapper.CheckingMapper;
import com.bing.mapper.DoctorMapper;
import com.bing.mapper.ManagerMapper;
import com.bing.mapper.PatientMapper;
import com.bing.service.ManagerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bing.vo.ResultVO;
import com.bing.vo.UserNumberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2024-03-20
 */
@Service
public class ManagerServiceImpl extends ServiceImpl<ManagerMapper, Manager> implements ManagerService {
    @Autowired
    private ManagerMapper managerMapper;
    @Autowired
    private PatientMapper patientMapper;
    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private CheckingMapper checkingMapper;

    @Override
    public ResultVO login(loginForm loginForm) {
        //1、判断账号（手机号）是否存在
        QueryWrapper<Manager> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", loginForm.getPhone())
                    .eq("password",loginForm.getPassword());
        Manager manager = this.managerMapper.selectOne(queryWrapper);

        ResultVO resultVO = new ResultVO();
        if(manager == null){
            resultVO.setCode(-1);
        } else {
            resultVO.setCode(0);
            resultVO.setData(manager);
        }
        return resultVO;
    }

    @Override
    public ResultVO getusernumber() {
        ResultVO resultVO = new ResultVO();
        try{
            UserNumberVO userNumberVO = new UserNumberVO();
            userNumberVO.setPatientNumber(patientMapper.selectCount(null));
            userNumberVO.setDoctorNumber(doctorMapper.selectCount(null));
            userNumberVO.setCheckingNumber(checkingMapper.selectCount(null));
            resultVO.setCode(0);
            resultVO.setData(userNumberVO);
        }catch (Exception e){
            resultVO.setCode(-1);
        }
        return resultVO;
    }
}
