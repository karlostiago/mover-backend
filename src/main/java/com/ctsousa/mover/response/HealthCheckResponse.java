package com.ctsousa.mover.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HealthCheckResponse {

    private boolean running;
}
