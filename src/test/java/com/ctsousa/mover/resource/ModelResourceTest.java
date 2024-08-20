package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.entity.ModelEntity;
import com.ctsousa.mover.domain.Model;
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

import static com.ctsousa.mover.core.mapper.Transform.toCollection;
import static com.ctsousa.mover.core.mapper.Transform.toMapper;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldFindAll() throws Exception {
        ModelRequest request1 = getRequest(1L, "FASTBACK");
        ModelRequest request2 = getRequest(2L, "MOBI LIKE");

        Model domain1 = toMapper(request1, Model.class);
        Model domain2 = toMapper(request2, Model.class);

        List<ModelEntity> entities = List.of(domain1.toEntity(), domain2.toEntity());

        when(modelService.findAll()).thenReturn(entities);

        mockMvc.perform(get("/models")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("FASTBACK".toUpperCase()))
                .andExpect(jsonPath("$[1].name").value("MOBI LIKE".toUpperCase()));

        verify(modelService, times(1)).findAll();
    }

    @Test
    void shouldFilterById() throws Exception {
        ModelRequest request = getRequest(1L, "FASTBACK");
        Model domain = toMapper(request, Model.class);
        ModelEntity entity = domain.toEntity();

        Long id = 1L;

        when(modelService.findById(id)).thenReturn(entity);

        mockMvc.perform(get("/models/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("FASTBACK".toUpperCase()));

        verify(modelService, times(1)).findById(id);
    }

    @Test
    void shouldDeleteById() throws Exception {
        Long id = 1L;

        ModelRequest request = getRequest(1L, "FASTBACK");
        Model domain = toMapper(request, Model.class);

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

        Model domain1 = toMapper(request1, Model.class);
        Model domain2 = toMapper(request2, Model.class);

        List<ModelEntity> entities = Arrays.asList(domain1.toEntity(), domain2.toEntity());
        List<ModelResponse> response = toCollection(entities, ModelResponse.class);

        String params = "";

        when(modelService.findBy(params)).thenReturn(entities);

        mockMvc.perform(get("/models/filterBy")
                        .param("search", params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("FASTBACK".toUpperCase()))
                .andExpect(jsonPath("$[1].name").value("MOBI LIKE".toUpperCase()));

        verify(modelService, times(1)).findBy(params);
    }

    @Test
    void shouldUpdate() throws Exception {
        Long id = 1L;
        ModelRequest request = getRequest(1L, "FASTBACK");
        Model domain = toMapper(request, Model.class);
        ModelEntity entity = domain.toEntity();

        when(modelService.findById(id)).thenReturn(entity);
        when(modelService.save(any(ModelEntity.class))).thenReturn(entity);

        mockMvc.perform(put("/models/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        verify(modelService, times(1)).existsById(id);
        verify(modelService, times(1)).save(entity);
    }

    @Test
    void shouldInsert() throws Exception {
        ModelRequest request = getRequest(1L, "FASTBACK");
        Model domain = toMapper(request, Model.class);
        ModelEntity entity = domain.toEntity();

        when(modelService.save(any(ModelEntity.class))).thenReturn(entity);

        mockMvc.perform(post("/models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("FASTBACK"));

        verify(modelService, times(1)).save(entity);
    }

    private ModelRequest getRequest(Long id, String name) {
        ModelRequest request = new ModelRequest();
        request.setId(id);
        request.setActive(true);
        request.setName(name);
        request.setBrandId(1L);
        return request;
    }
}
