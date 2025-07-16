package com.ctsousa.mover.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class CardDashboardResponse {
    private String description;
    private Integer quantity;
    private BigDecimal value;
    private String iconPath;
    private boolean loading;
    private boolean paid;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dueDate;
}
