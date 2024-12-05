package com.ctsousa.mover.response;

import com.ctsousa.mover.enumeration.InspectionStatus;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class InspectionResponse {
    private Long id;
    private BigDecimal mileage;
    private LocalDate date;
    private InspectionStatus inspectionStatus;
    private ContractResponse contract;
    private Set<InspectionQuestionResponse> questions = new HashSet<>();
    private Set<InspectionPhotoResponse> photos = new HashSet<>();
}
