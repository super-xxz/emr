package com.bing.form;

import lombok.Data;

@Data
public class searchFormPatient {
    private Integer patientid;
    private Integer recordid;
    private String name;
    private String phone;
    private String hospital;
    private String office;
    private Integer page;
    private Integer size;
}
