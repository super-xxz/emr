package com.bing.vo;

import lombok.Data;


//授权管理视图
@Data
public class AuthorizeVO {
    private Integer id;
    private Integer doctorid;
    private Integer recordid;
    private String createtime;
    private String name;
    private String office;
    private String hospital;
    private Integer state;
}
