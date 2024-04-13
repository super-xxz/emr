package com.bing.form;

import lombok.Data;

import java.util.Date;

@Data
public class patientRegisterForm {
    private String name;
    private Long phone;
    private String password;
    private Date date;
    private Integer sex;
}
