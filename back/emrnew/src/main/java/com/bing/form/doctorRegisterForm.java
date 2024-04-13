package com.bing.form;

import lombok.Data;

import java.util.Date;

@Data
public class doctorRegisterForm {
    private String name;
    private Long phone;
    private String password;
    private String hospital;
    private String office;
    private Long checking;
}
