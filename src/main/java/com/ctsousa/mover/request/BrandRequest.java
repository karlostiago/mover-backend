package com.ctsousa.mover.request;

import com.ctsousa.mover.domain.Symbol;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandRequest {
    private Long id;
    private String name;
    private Symbol symbol;
    private Boolean active;
}
