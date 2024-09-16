package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.enumeration.FuelType;
import com.ctsousa.mover.enumeration.Situation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleTest {

    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        vehicle = new Vehicle();
    }

    @Test
    void testToEntity() {
        vehicle.setId(1L);
        vehicle.setLicensePlate("abc1234");
        vehicle.setYearManufacture(2020);
        vehicle.setModelYear(2021);
        vehicle.setRenavam("12345678900");
        vehicle.setFipeValueAtAcquisition(BigDecimal.valueOf(50000));
        vehicle.setAcquisitionValue(BigDecimal.valueOf(45000));
        vehicle.setAcquisitionDate(LocalDate.of(2023, 1, 1));
        vehicle.setAvailabilityDate(LocalDate.of(2023, 6, 1));
        vehicle.setMileageAtAcquisition(BigDecimal.valueOf(10000));
        vehicle.setAuction(true);
        vehicle.setFipeDepreciation(BigDecimal.valueOf(5000));
        vehicle.setColor("red");
        vehicle.setSituation(Situation.IN_PROGRESS.getDescription());
        vehicle.setFuelType(FuelType.GASOLINE.getDescription());

        VehicleEntity entity = vehicle.toEntity();

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("ABC1234", entity.getLicensePlate());
        assertEquals(2020, entity.getYearManufacture());
        assertEquals(2021, entity.getModelYear());
        assertEquals("12345678900", entity.getRenavam());
        assertEquals(BigDecimal.valueOf(50000), entity.getFipeValueAtAcquisition());
        assertEquals(BigDecimal.valueOf(45000), entity.getAcquisitionValue());
        assertEquals(LocalDate.of(2023, 1, 1), entity.getAcquisitionDate());
        assertEquals(LocalDate.of(2023, 6, 1), entity.getAvailabilityDate());
        assertEquals(BigDecimal.valueOf(10000), entity.getMileageAtAcquisition());
        assertTrue(entity.getAuction());
        assertEquals(BigDecimal.valueOf(5000), entity.getFipeDepreciation());
        assertEquals("RED", entity.getColor());
        assertEquals("EM PROGRESSO", entity.getSituation());
        assertEquals("GASOLINA", entity.getFuelType());
    }
}
