package com.ctsousa.mover.request;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VehicleRequestTest {

    @Test
    void shouldRequestSettersAndGetters() {
        VehicleRequest request = new VehicleRequest();
        Long expectedBrandId = 1L;
        Long expectedModelId = 1L;
        String expectedLicensePlate = "ABC1234";
        Integer expectedYearManufacture = 2020;
        Integer expectedModelYear = 2021;
        String expectedRenavam = "12345678901";
        BigDecimal expectedFipeValueAtAcquisition = new BigDecimal("45000.00");
        BigDecimal expectedAcquisitionValue = new BigDecimal("43000.00");
        LocalDate expectedAcquisitionDate = LocalDate.of(2020, 5, 10);
        LocalDate expectedAvailabilityDate = LocalDate.of(2020, 5, 20);
        BigDecimal expectedMileageAtAcquisition = new BigDecimal("10000.00");
        Boolean expectedAuction = true;
        BigDecimal expectedFipeDepreciation = new BigDecimal("5000.00");
        String expectedColor = "Red";
        Integer expectedCodeSituation = 1;

        request.setBrandId(expectedBrandId);
        request.setModelId(expectedModelId);
        request.setLicensePlate(expectedLicensePlate);
        request.setYearManufacture(expectedYearManufacture);
        request.setModelYear(expectedModelYear);
        request.setRenavam(expectedRenavam);
        request.setFipeValueAtAcquisition(expectedFipeValueAtAcquisition);
        request.setAcquisitionValue(expectedAcquisitionValue);
        request.setAcquisitionDate(expectedAcquisitionDate);
        request.setAvailabilityDate(expectedAvailabilityDate);
        request.setMileageAtAcquisition(expectedMileageAtAcquisition);
        request.setAuction(expectedAuction);
        request.setFipeDepreciation(expectedFipeDepreciation);
        request.setColor(expectedColor);
        request.setCodeSituation(expectedCodeSituation);

        assertEquals(expectedBrandId, request.getBrandId());
        assertEquals(expectedModelId, request.getModelId());
        assertEquals(expectedLicensePlate, request.getLicensePlate());
        assertEquals(expectedYearManufacture, request.getYearManufacture());
        assertEquals(expectedModelYear, request.getModelYear());
        assertEquals(expectedRenavam, request.getRenavam());
        assertEquals(expectedFipeValueAtAcquisition, request.getFipeValueAtAcquisition());
        assertEquals(expectedAcquisitionValue, request.getAcquisitionValue());
        assertEquals(expectedAcquisitionDate, request.getAcquisitionDate());
        assertEquals(expectedAvailabilityDate, request.getAvailabilityDate());
        assertEquals(expectedMileageAtAcquisition, request.getMileageAtAcquisition());
        assertEquals(expectedAuction, request.getAuction());
        assertEquals(expectedFipeDepreciation, request.getFipeDepreciation());
        assertEquals(expectedColor, request.getColor());
        assertEquals(expectedCodeSituation, request.getCodeSituation());
    }
}
