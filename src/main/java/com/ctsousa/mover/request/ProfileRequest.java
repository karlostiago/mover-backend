package com.ctsousa.mover.request;

import com.ctsousa.mover.core.annotation.NotEmpty;
import com.ctsousa.mover.response.FuncionalityResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfileRequest {
    private Long id;

    @NotEmpty(message = "Descrição da conta não pode ser vázio")
    private String description;

    private List<FuncionalityResponse> permissions;

    private Boolean active;
}
