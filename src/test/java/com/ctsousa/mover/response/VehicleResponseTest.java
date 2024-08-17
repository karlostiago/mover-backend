package com.ctsousa.mover.response;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VehicleResponseTest {

    @Test
    void testResponseSettersAndGetters() {
        VehicleResponse response = new VehicleResponse();
        Long expectedId = 1L;
        Long expectedBrandId = 10L;
        String expectedBrandName = "Ford";
        Long expectedModelId = 20L;
        String expectedModelName = "Fiesta";
        String expectedLicensePlate = "XYZ1234";
        Integer expectedYearManufacture = 2022;
        Integer expectedModelYear = 2023;
        String expectedRenavam = "654321789";
        BigDecimal expectedFipeValueAtAcquisition = new BigDecimal("45000.00");
        BigDecimal expectedAcquisitionValue = new BigDecimal("43000.00");
        LocalDate expectedAcquisitionDate = LocalDate.of(2023, 5, 10);
        LocalDate expectedAvailabilityDate = LocalDate.of(2023, 6, 1);
        BigDecimal expectedMileageAtAcquisition = new BigDecimal("15000");
        Boolean expectedAuction = true;
        BigDecimal expectedFipeDepreciation = new BigDecimal("2000.00");
        String expectedColor = "Red";
        String expectedSituation = "Available";

        response.setId(expectedId);
        response.setBrandId(expectedBrandId);
        response.setBrandName(expectedBrandName);
        response.setModelId(expectedModelId);
        response.setModelName(expectedModelName);
        response.setLicensePlate(expectedLicensePlate);
        response.setYearManufacture(expectedYearManufacture);
        response.setModelYear(expectedModelYear);
        response.setRenavam(expectedRenavam);
        response.setFipeValueAtAcquisition(expectedFipeValueAtAcquisition);
        response.setAcquisitionValue(expectedAcquisitionValue);
        response.setAcquisitionDate(expectedAcquisitionDate);
        response.setAvailabilityDate(expectedAvailabilityDate);
        response.setMileageAtAcquisition(expectedMileageAtAcquisition);
        response.setAuction(expectedAuction);
        response.setFipeDepreciation(expectedFipeDepreciation);
        response.setColor(expectedColor);
        response.setSituation(expectedSituation);

        assertEquals(expectedId, response.getId());
        assertEquals(expectedBrandId, response.getBrandId());
        assertEquals(expectedBrandName, response.getBrandName());
        assertEquals(expectedModelId, response.getModelId());
        assertEquals(expectedModelName, response.getModelName());
        assertEquals(expectedLicensePlate, response.getLicensePlate());
        assertEquals(expectedYearManufacture, response.getYearManufacture());
        assertEquals(expectedModelYear, response.getModelYear());
        assertEquals(expectedRenavam, response.getRenavam());
        assertEquals(expectedFipeValueAtAcquisition, response.getFipeValueAtAcquisition());
        assertEquals(expectedAcquisitionValue, response.getAcquisitionValue());
        assertEquals(expectedAcquisitionDate, response.getAcquisitionDate());
        assertEquals(expectedAvailabilityDate, response.getAvailabilityDate());
        assertEquals(expectedMileageAtAcquisition, response.getMileageAtAcquisition());
        assertEquals(expectedAuction, response.getAuction());
        assertEquals(expectedFipeDepreciation, response.getFipeDepreciation());
        assertEquals(expectedColor, response.getColor());
        assertEquals(expectedSituation, response.getSituation());
    }
}
