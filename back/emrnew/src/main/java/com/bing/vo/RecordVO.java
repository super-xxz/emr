package com.bing.vo;

import lombok.Data;


//查看档案视图
@Data
public class RecordVO {
    private Integer id;
    private String createtime;
    private String name;
    private Long phone;
    private Integer sex;
    private Integer age;
    private String office;
    //弹窗中所需数据
    private String hospital;
    private String description;
    private String remark;
    private Integer affirm;
    private Integer haveauthority;
}
