package com.ctsousa.mover.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InspectionEntity extends AbstractEntity {

    private ContractEntity contract;
    private LocalDate date;
    private Long km;
    private Boolean key;
    private Boolean radio;
    private Boolean monkey;
    private Boolean triangle;
    private Boolean steppe;
    private Boolean glass;
    private Boolean plate;
    private Boolean rearViewMirror;
    private Boolean lighthouse;
}
