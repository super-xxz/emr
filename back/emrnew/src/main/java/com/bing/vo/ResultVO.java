package com.bing.vo;

import lombok.Data;

@Data

public class ResultVO<T> {
    private Integer code;
    private T data;  //泛型
}
