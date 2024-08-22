package com.ctsousa.mover.request;

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
    @NotEmpty(message = "Campo marca não pode ser vázio")
    private Long brandId;

    @NotEmpty(message = "Campo modelo não pode ser vázio")
    private Long modelId;

    @NotEmpty(message = "Campo placa não pode ser vázio")
    private String licensePlate;

    @NotEmpty(message = "Campo ano de fabricação não pode ser vázio")
    private Integer yearManufacture;

    @NotEmpty(message = "Campo ano do modelo não pode ser vázio")
    private Integer modelYear;

    @NotEmpty(message = "Campo renavam não pode ser vázio")
    private String renavam;

    @NotEmpty(message = "Campo valor fipe de aquisição não pode ser vázio")
    private BigDecimal fipeValueAtAcquisition;

    @NotEmpty(message = "Campo valor adiquisição não pode ser vázio")
    private BigDecimal acquisitionValue;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @NotEmpty(message = "Campo data de aquisição não pode ser vázio")
    private LocalDate acquisitionDate;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate availabilityDate;

    @NotEmpty(message = "Campo quilometragem de aquisição não pode ser vázio")
    private BigDecimal mileageAtAcquisition;

    @NotEmpty(message = "Campo leilão não pode ser vázio")
    private Boolean auction;

    private BigDecimal fipeDepreciation;

    @NotEmpty(message = "Campo cor não pode ser vázio")
    private String color;

    @NotEmpty(message = "Campo situação não pode ser vázio")
    private String situation;

    @NotEmpty(message = "Campo tipo de combustivel não pode ser vázio")
    private String fuelType;

    private Boolean active;
}
