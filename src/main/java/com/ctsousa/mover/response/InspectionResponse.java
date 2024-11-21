package com.ctsousa.mover.response;

import com.ctsousa.mover.enumeration.InspectionStatus;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class InspectionResponse {
    private Long id;
    private BigDecimal mileage;
    private LocalDate date;
    private InspectionStatus inspectionStatus;
    private ContractResponse contract;
}
