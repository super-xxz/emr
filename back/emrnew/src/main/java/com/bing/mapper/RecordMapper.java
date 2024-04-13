package com.bing.mapper;

import com.bing.entity.Authorize;
import com.bing.entity.Record;
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
public interface RecordMapper extends BaseMapper<Record> {

    @Select("<script> " +
            "SELECT * FROM record " +
            "WHERE 1=1 " +
            "<if test='recordid != null'>" +
            "   AND id LIKE CONCAT('%', #{recordid}, '%')" +
            "</if>" +
            "<if test='name != null and name.trim() != \"\"'>" +
            "   AND patientid in (SELECT id FROM patient WHERE name LIKE CONCAT('%', #{name}, '%')) " +
            "</if>" +
            "<if test='phone != null and phone.trim() != \"\"'>" +
            "   AND patientid in (SELECT id FROM patient WHERE phone LIKE CONCAT('%', #{phone}, '%')) " +
            "</if>" +
            "<if test='hospital != null and hospital.trim() != \"\"'>" +
            "   AND hospital LIKE CONCAT('%', #{hospital}, '%')" +
            "</if>" +
            "<if test='office != null and office.trim() != \"\"'>" +
            "   AND office LIKE CONCAT('%', #{office}, '%')" +
            "</if>" +
            "   AND affirm = 1" +
            "</script>")
    List<Record> selectRecordByDoctor(@Param("recordid") Integer recordid,
                                      @Param("name") String name,
                                      @Param("phone") String phone,
                                      @Param("hospital") String hospital,
                                      @Param("office") String office);

    @Select("<script> " +
            "SELECT * FROM record " +
            "WHERE 1=1 " +
            "   AND patientid = #{patientid}" +
            "<if test='recordid != null'>" +
            "   AND id LIKE CONCAT('%', #{recordid}, '%')" +
            "</if>" +
            "<if test='hospital != null and hospital.trim() != \"\"'>" +
            "   AND hospital LIKE CONCAT('%', #{hospital}, '%')" +
            "</if>" +
            "<if test='office != null and office.trim() != \"\"'>" +
            "   AND office LIKE CONCAT('%', #{office}, '%')" +
            "</if>" +
            "</script>")
    List<Record> selectRecordByPatient(@Param("patientid") Integer patientid,
                                       @Param("recordid") Integer recordid,
                                       @Param("hospital") String hospital,
                                       @Param("office") String office);


    @Select("<script> " +
            "SELECT * FROM authorize " +
            "WHERE doctorid = #{doctorid}" +
            "   AND recordid = #{recordid}" +
            "   AND state = 1" +
            "</script>")
    Authorize haveAutority(@Param("doctorid") Integer doctorid, @Param("recordid") Integer recordid);

}
