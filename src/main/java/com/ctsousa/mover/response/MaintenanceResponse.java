package com.ctsousa.mover.response;

import com.ctsousa.mover.enumeration.TypeMaintenance;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class MaintenanceResponse {
    private Long id;
    private Long vehicleId;
    private String vehicleName;
    private Long accountId;
    private Long cardId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate date;
    private Long mileage;
    private String establishment;
    private String type;
    private String detail;
    private BigDecimal value;
    private Boolean active;

    public void setType(String type) {
        this.type = TypeMaintenance.toDescription(type).getDescription();
    }
}
