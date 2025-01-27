package com.ctsousa.mover.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankIconResponse {

    private Integer code;
    private String prefix;
    private String bankName;
    private String urlImage;
}
