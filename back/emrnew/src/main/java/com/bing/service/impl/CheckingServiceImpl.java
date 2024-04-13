package com.bing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bing.entity.*;
import com.bing.entity.Record;
import com.bing.form.createForm;
import com.bing.form.searchFormChecking;
import com.bing.mapper.CheckingMapper;
import com.bing.service.CheckingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bing.vo.PageVO;
import com.bing.vo.RecordVO;
import com.bing.vo.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2024-03-15
 */
@Service
public class CheckingServiceImpl extends ServiceImpl<CheckingMapper, Checking> implements CheckingService {

    @Autowired
    private CheckingMapper checkingMapper;

    @Override
    public ResultVO create(createForm createForm) {
        ResultVO resultVO = new ResultVO();
        //判断是否有重复数据
        QueryWrapper<Checking> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hospital",createForm.getHospital())
                    .eq("office",createForm.getOffice());
        Checking checking = checkingMapper.selectOne(queryWrapper);
        if(checking != null){
            resultVO.setCode(-1);
            return resultVO;
        }

        Checking checking2 = new Checking();
        checking2.setHospital(createForm.getHospital());
        checking2.setOffice(createForm.getOffice());

        //生成七位随机数作为资质码
        Random random = new Random();
        Long randomNumber = 1000000L + random.nextInt(9000000);
        checking2.setChecking(randomNumber);

        int insert = this.checkingMapper.insert(checking2);
        if(insert != 1){
            resultVO.setCode(-2);
            return resultVO;
        }
        resultVO.setData(randomNumber);
        resultVO.setCode(0);
        return resultVO;
    }

    @Override
    public PageVO checkingsearch(searchFormChecking searchFormChecking) {
        Page<Checking> checkingPage = new Page<>(searchFormChecking.getPage(), searchFormChecking.getSize());
        Page<Checking> resultPage = null;

        // 如果医院和科室都为空，则展示全部记录
        if (StringUtils.isEmpty(searchFormChecking.getHospital()) && StringUtils.isEmpty(searchFormChecking.getOffice())) {
            resultPage = checkingMapper.selectPage(checkingPage, null);
        } else { // 医院和科室有值，则进行模糊查询
            QueryWrapper<Checking> queryWrapper = new QueryWrapper<>();
            if (StringUtils.isNotEmpty(searchFormChecking.getHospital())) {
                queryWrapper.like("hospital", searchFormChecking.getHospital());
            }
            if (StringUtils.isNotEmpty(searchFormChecking.getOffice())) {
                queryWrapper.like("office", searchFormChecking.getOffice());
            }
            resultPage = checkingMapper.selectPage(checkingPage, queryWrapper);
        }

        List<Checking> checkingList = resultPage.getRecords();
        PageVO pageVO = new PageVO();
        pageVO.setData(checkingList);
        pageVO.setTotal(resultPage.getTotal());
        return pageVO;
    }

    @Override
    public Boolean resetchecking(searchFormChecking searchFormChecking) {
        //生成七位随机数作为资质码
        Random random = new Random();
        Long randomNumber = 1000000L + random.nextInt(9000000);

        //更新数据
        UpdateWrapper<Checking> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("hospital", searchFormChecking.getHospital())
                     .eq("office", searchFormChecking.getOffice());
        updateWrapper.set("checking",randomNumber);
        int update = baseMapper.update(null,updateWrapper);
        if(update != 1) return false;
        return true;
    }
}
