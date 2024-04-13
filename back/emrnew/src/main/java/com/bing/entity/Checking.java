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
public class Checking implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 医院
     */
    private String hospital;

    /**
     * 科室
     */
    private String office;

    /**
     * 资质码
     */
    private Long checking;


}
