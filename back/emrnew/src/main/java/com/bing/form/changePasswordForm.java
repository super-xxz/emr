package com.bing.form;

import lombok.Data;

@Data
public class changePasswordForm {
    private Integer id;
    private String oldpassword;
    private String newpassword;
}
