package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.entity.ModelEntity;
import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.domain.Vehicle;
import com.ctsousa.mover.enumeration.FuelType;
import com.ctsousa.mover.enumeration.Situation;
import com.ctsousa.mover.request.VehicleRequest;
import com.ctsousa.mover.service.BrandService;
import com.ctsousa.mover.service.ModelService;
import com.ctsousa.mover.service.VehicleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.ctsousa.mover.core.mapper.Transform.toMapper;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VehicleResource.class)
public class VehicleResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleService vehicleService;

    @MockBean
    private BrandService brandService;

    @MockBean
    private ModelService modelService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldInsert() throws Exception {
        VehicleRequest request = getRequest();
        Vehicle domain = toMapper(request, Vehicle.class);
        VehicleEntity entity = domain.toEntity();

        entity.setId(1L);
        entity.setBrand(getBrandEntity());
        entity.setModel(getModelEntity());

        when(modelService.findById(domain.getModel().getId())).thenReturn(getModelEntity());
        when(brandService.findById(domain.getBrand().getId())).thenReturn(getBrandEntity());
        when(vehicleService.save(any(VehicleEntity.class))).thenReturn(entity);

        mockMvc.perform(post("/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.licensePlate").value("ABC1234"));

        ArgumentCaptor<VehicleEntity> captor = ArgumentCaptor.forClass(VehicleEntity.class);
        verify(vehicleService, times(1)).save(captor.capture());
    }

    @Test
    void shouldFindAll() throws Exception {
        VehicleRequest request = getRequest();
        Vehicle domain = toMapper(request, Vehicle.class);
        domain.setId(1L);

        List<VehicleEntity> entities = List.of(domain.toEntity());

        when(vehicleService.findAll()).thenReturn(entities);

        mockMvc.perform(get("/vehicles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].licensePlate").value("ABC1234"));

        verify(vehicleService, times(1)).findAll();
    }

    @Test
    void shouldFilterById() throws Exception {
        VehicleRequest request = getRequest();
        Vehicle domain = toMapper(request, Vehicle.class);
        domain.setId(1L);

        Long id = 1L;

        when(vehicleService.findById(id)).thenReturn(domain.toEntity());

        mockMvc.perform(get("/vehicles/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.licensePlate").value("ABC1234"));

        verify(vehicleService, times(1)).findById(id);
    }

    @Test
    void shouldDeleteById() throws Exception {
        Long id = 1L;

        VehicleRequest request = getRequest();
        Vehicle domain = toMapper(request, Vehicle.class);
        domain.setId(1L);

        when(vehicleService.findById(id)).thenReturn(domain.toEntity());
        doNothing().when(vehicleService).deleteById(id);

        mockMvc.perform(delete("/vehicles/{id}", id))
                .andExpect(status().isOk());

        verify(vehicleService, times(1)).findById(id);
        verify(vehicleService, times(1)).deleteById(id);
    }

    @Test
    void shouldUpdate() throws Exception {
        Long id = 1L;
        VehicleRequest request = getRequest();
        Vehicle domain = toMapper(request, Vehicle.class);
        domain.setId(1L);

        VehicleEntity entity = domain.toEntity();

        when(vehicleService.findById(id)).thenReturn(entity);
        when(vehicleService.save(any(VehicleEntity.class))).thenReturn(entity);

        mockMvc.perform(put("/vehicles/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        verify(vehicleService, times(1)).existsById(id);
        verify(vehicleService, times(1)).save(entity);
    }

    @Test
    void shouldFilteredWithParams() throws Exception {
        VehicleRequest request = getRequest();
        Vehicle domain = toMapper(request, Vehicle.class);

        List<VehicleEntity> entities = List.of(domain.toEntity());

        String params = "ABC1234";

        when(vehicleService.findBy(params)).thenReturn(entities);

        mockMvc.perform(get("/vehicles/findBy")
                        .param("search", params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].licensePlate").value("ABC1234"));

        verify(vehicleService, times(1)).findBy(params);
    }

    private BrandEntity getBrandEntity() {
        BrandEntity entity = new BrandEntity();
        entity.setId(1L);
        entity.setName("brandEntity");
        return entity;
    }

    private ModelEntity getModelEntity() {
        ModelEntity entity = new ModelEntity();
        entity.setId(1L);
        entity.setName("modelEntity");
        return entity;
    }

    private VehicleRequest getRequest() {
        VehicleRequest request = new VehicleRequest();
        request.setBrandId(1L);
        request.setModelId(1L);
        request.setLicensePlate("ABC1234");
        request.setYearManufacture(2020);
        request.setModelYear(2021);
        request.setRenavam("12345678901");
        request.setFipeValueAtAcquisition( new BigDecimal("45000.00"));
        request.setAcquisitionValue( new BigDecimal("45000.00"));
        request.setAcquisitionDate(LocalDate.of(2020, 5, 10));
        request.setAvailabilityDate(LocalDate.of(2020, 5, 10));
        request.setMileageAtAcquisition(new BigDecimal("10000.00"));
        request.setAuction(true);
        request.setFipeDepreciation(new BigDecimal("5000.00"));
        request.setColor("Red");
        request.setSituation(Situation.IN_PROGRESS.getDescription());
        request.setFuelType(FuelType.GASOLINE.getDescription());
        return request;
    }
}
