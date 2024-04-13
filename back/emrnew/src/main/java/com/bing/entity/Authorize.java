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
    public class Authorize implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 申请人（医生）id
     */
      private Integer doctorid;

      /**
       * 档案id
       */
      private Integer recordid;

      /**
       * 档案内容
       */
      private String description;

      /**
       * 备注
       */
      private String remark;

      /**
     * 授权状态
     */
      private Integer state;

      /**
     * 申请授权时间
     */
      private String createtime;


}
