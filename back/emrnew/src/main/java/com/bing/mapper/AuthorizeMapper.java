package com.bing.mapper;

import com.bing.entity.Authorize;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2023-03-19
 */
public interface AuthorizeMapper extends BaseMapper<Authorize> {

    @Select("<script> " +
            "SELECT * FROM authorize " +
            "WHERE recordid in (SELECT id FROM record WHERE patientid = #{patientid})" +
            "</script>")
    List<Authorize> myauthorize(@Param("patientid") Integer patientid);


    @Select("<script> " +
            "SELECT * FROM authorize " +
            "WHERE recordid = #{recordid} AND doctorid = #{doctorid}" +
            "</script>")
    Authorize selectByRecordidAndDoctorid(@Param("recordid") Integer recordid, @Param("doctorid") String doctorid);

}
