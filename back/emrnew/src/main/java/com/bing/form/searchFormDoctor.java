package com.bing.form;

import lombok.Data;

@Data
public class searchFormDoctor {
    private String doctorid;
    private Integer recordid;
    private String name;
    private String phone;
    private String hospital;
    private String office;
    private Integer page;
    private Integer size;
}
