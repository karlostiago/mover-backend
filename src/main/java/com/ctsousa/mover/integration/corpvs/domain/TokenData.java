package com.ctsousa.mover.integration.corpvs.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenData {
    @JsonProperty("usr_id")
    private Integer userId;

    @JsonProperty("cli_id")
    private Integer cliId;
}
