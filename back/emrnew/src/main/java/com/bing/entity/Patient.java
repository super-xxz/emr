package com.bing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
    public class Patient implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * id
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 姓名
     */
      private String name;

      /**
     * 电话（登录账号）
     */
      private Long phone;

      /**
     * 登录密码
     */
      private String password;

      /**
     * 性别（1为女，2为男）
     */
      private Integer sex;

      /**
     * 出生日期
     */
      private String date;

      /**
     * 公钥
     */
      private String publickey;


}
