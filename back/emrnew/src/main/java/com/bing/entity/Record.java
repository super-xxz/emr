package com.bing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author admin
 * @since 2024-03
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    public class Record implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * id
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 医生id
     */
      private Integer doctorid;

      /**
     * 患者id
     */
      private Integer patientid;

    /**
     * 就诊医院
     */
    private String hospital;

    /**
     * 就诊科室
     */
    private String office;

      /**
     * 档案创建时间
     */
      private String createtime;

      /**
     * 医生诊断
     */
      private String description;

      /**
     * 备注
     */
      private String remark;


    /**
     * 确认情况（0未确认，1已确认）
     */
    private Integer affirm;
}
