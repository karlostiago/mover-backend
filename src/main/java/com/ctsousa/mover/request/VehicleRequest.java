package com.ctsousa.mover.request;

import com.ctsousa.mover.core.annotation.DateFormat;
import com.ctsousa.mover.core.annotation.NotEmpty;
import com.ctsousa.mover.core.deserializer.LocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class VehicleRequest {
    @NotEmpty(message = "Marca não pode ser vázio")
    private Long brandId;

    @NotEmpty(message = "Modelo não pode ser vázio")
    private Long modelId;

    @NotEmpty(message = "Placa não pode ser vázio")
    private String licensePlate;

    @NotEmpty(message = "Ano de fabricação não pode ser vázio")
    private Integer yearManufacture;

    @NotEmpty(message = "Ano do modelo não pode ser vázio")
    private Integer modelYear;

    @NotEmpty(message = "Renavam não pode ser vázio")
    private String renavam;

    @NotEmpty(message = "Valor fipe de aquisição não pode ser vázio")
    private BigDecimal fipeValueAtAcquisition;

    @NotEmpty(message = "Valor adiquisição não pode ser vázio")
    private BigDecimal acquisitionValue;

    @DateFormat(message = "Data aquisição inválida")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @NotEmpty(message = "Campo data de aquisição não pode ser vázio")
    private LocalDate acquisitionDate;

    @DateFormat(message = "Data disponibilidade inválida")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate availabilityDate;

    @NotEmpty(message = "Quilometragem de aquisição não pode ser vázio")
    private BigDecimal mileageAtAcquisition;

    @NotEmpty(message = "Leilão não pode ser vázio")
    private Boolean auction;

    private BigDecimal fipeDepreciation;

    @NotEmpty(message = "Cor não pode ser vázio")
    private String color;

    @NotEmpty(message = "Situação não pode ser vázio")
    private String situation;

    @NotEmpty(message = "Tipo de combustivel não pode ser vázio")
    private String fuelType;

    private Boolean active;
}
