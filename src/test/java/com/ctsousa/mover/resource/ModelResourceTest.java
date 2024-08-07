package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.entity.ModelEntity;
import com.ctsousa.mover.domain.Model;
import com.ctsousa.mover.mapper.ModelMapper;
import com.ctsousa.mover.request.ModelRequest;
import com.ctsousa.mover.response.ModelResponse;
import com.ctsousa.mover.service.ModelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ModelResource.class)
public class ModelResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ModelService modelService;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldFindAll() throws Exception {
        ModelRequest request1 = getRequest(1L, "FASTBACK");
        ModelRequest request2 = getRequest(2L, "MOBI LIKE");

        Model domain1 = new ModelMapper().toDomain(request1);
        Model domain2 = new ModelMapper().toDomain(request2);

        List<ModelEntity> entities = Arrays.asList(domain1.toEntity(), domain2.toEntity());
        List<ModelResponse> response = new ModelMapper().toCollections(entities);

        when(modelService.findAll()).thenReturn(entities);
        when(modelMapper.toCollections(entities)).thenReturn(response);

        mockMvc.perform(get("/models")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("FASTBACK".toUpperCase()))
                .andExpect(jsonPath("$[1].name").value("MOBI LIKE".toUpperCase()));

        verify(modelService, times(1)).findAll();
        verify(modelMapper, times(1)).toCollections(entities);
    }

    @Test
    void shouldFilterById() throws Exception {
        ModelRequest request = getRequest(1L, "FASTBACK");
        Model domain = new ModelMapper().toDomain(request);
        ModelEntity entity = domain.toEntity();
        ModelResponse response = new ModelMapper().toResponse(entity);

        Long id = 1L;

        when(modelService.findById(id)).thenReturn(entity);
        when(modelMapper.toResponse(entity)).thenReturn(response);

        mockMvc.perform(get("/models/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("FASTBACK".toUpperCase()));

        verify(modelService, times(1)).findById(id);
        verify(modelMapper, times(1)).toResponse(entity);
    }

    @Test
    void shouldDeleteById() throws Exception {
        Long id = 1L;

        ModelRequest request = getRequest(1L, "FASTBACK");
        Model domain = new ModelMapper().toDomain(request);

        when(modelService.findById(id)).thenReturn(domain.toEntity());
        doNothing().when(modelService).deleteById(id);

        mockMvc.perform(delete("/models/{id}", id))
                .andExpect(status().isOk());

        verify(modelService, times(1)).findById(id);
        verify(modelService, times(1)).deleteById(id);
    }

    @Test
    void shouldFilteredWithParams() throws Exception {
        ModelRequest request1 = getRequest(1L, "FASTBACK");
        ModelRequest request2 = getRequest(2L, "MOBI LIKE");

        Model domain1 = new ModelMapper().toDomain(request1);
        Model domain2 = new ModelMapper().toDomain(request2);

        List<ModelEntity> entities = Arrays.asList(domain1.toEntity(), domain2.toEntity());
        List<ModelResponse> response = new ModelMapper().toCollections(entities);

        String params = "";

        when(modelService.findBy(params)).thenReturn(entities);
        when(modelMapper.toCollections(entities)).thenReturn(response);

        mockMvc.perform(get("/models/filterBy")
                        .param("search", params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("FASTBACK".toUpperCase()))
                .andExpect(jsonPath("$[1].name").value("MOBI LIKE".toUpperCase()));

        verify(modelService, times(1)).findBy(params);
        verify(modelMapper, times(1)).toCollections(entities);
    }

    @Test
    void shouldUpdate() throws Exception {
        Long id = 1L;
        ModelRequest request = getRequest(1L, "FASTBACK");
        Model domain = new ModelMapper().toDomain(request);
        ModelEntity entity = domain.toEntity();

        when(modelService.findById(id)).thenReturn(entity);
        when(modelMapper.toDomain(refEq(request))).thenReturn(domain);
        when(modelMapper.toResponse(entity)).thenReturn(new ModelMapper().toResponse(entity));
        when(modelService.save(entity)).thenReturn(entity);

        mockMvc.perform(put("/models/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        verify(modelService, times(1)).findById(id);
        verify(modelService, times(1)).save(entity);
        verify(modelMapper, times(1)).toDomain(refEq(request));
        verify(modelMapper, times(1)).toResponse(entity);
    }

    @Test
    void shouldInsert() throws Exception {
        ModelRequest request = getRequest(1L, "FASTBACK");
        Model domain = new ModelMapper().toDomain(request);
        ModelEntity entity = domain.toEntity();
        ModelResponse response = new ModelMapper().toResponse(entity);

        when(modelMapper.toDomain(refEq(request))).thenReturn(domain);
        when(modelService.save(any(ModelEntity.class))).thenReturn(entity);
        when(modelMapper.toResponse(refEq(entity))).thenReturn(response);

        mockMvc.perform(post("/models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("FASTBACK"));

        verify(modelService, times(1)).save(entity);
        verify(modelMapper, times(1)).toDomain(refEq(request));
        verify(modelMapper, times(1)).toResponse(entity);
    }

    private ModelRequest getRequest(Long id, String name) {
        ModelRequest request = new ModelRequest();
        request.setId(id);
        request.setActive(true);
        request.setName(name);
        request.setColor("BRANCO");
        request.setYearModel(2025);
        request.setYearManufacture(2025);
        request.setBrandId(1L);
        return request;
    }
}
