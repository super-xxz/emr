package com.bing.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author admin
 * @since 2024-03-20
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    public class Manager implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 手机号
     */
        private Long phone;

      /**
     * 密码
     */
      private String password;


}
