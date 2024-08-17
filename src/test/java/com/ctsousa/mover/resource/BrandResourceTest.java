package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.entity.SymbolEntity;
import com.ctsousa.mover.domain.Brand;
import com.ctsousa.mover.domain.Symbol;
import com.ctsousa.mover.request.BrandRequest;
import com.ctsousa.mover.service.BrandService;
import com.ctsousa.mover.service.SymbolService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BrandResource.class)
public class BrandResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BrandService brandService;

    @MockBean
    private SymbolService symbolService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldFilterByName() throws Exception {

        BrandEntity brand1 = new BrandEntity("Brand1", new SymbolEntity("", ""));
        BrandEntity brand2 = new BrandEntity("Brand2", new SymbolEntity("", ""));
        List<BrandEntity> brands = Arrays.asList(brand1, brand2);

        when(brandService.filterByName("Brand")).thenReturn(brands);

        mockMvc.perform(get("/brands/filterBy")
                        .param("name", "Brand")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Brand1"))
                .andExpect(jsonPath("$[1].name").value("Brand2"));

        verify(brandService, times(1)).filterByName("Brand");
    }

    @Test
    void shouldFindAll() throws Exception {

        BrandEntity brand1 = new BrandEntity("Brand1", new SymbolEntity("", ""));
        BrandEntity brand2 = new BrandEntity("Brand2", new SymbolEntity("", ""));
        List<BrandEntity> brands = Arrays.asList(brand1, brand2);

        when(brandService.findAll()).thenReturn(brands);

        mockMvc.perform(get("/brands")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Brand1"))
                .andExpect(jsonPath("$[1].name").value("Brand2"));

        verify(brandService, times(1)).findAll();
    }

    @Test
    void shouldFindById() throws Exception {

        Long id = 1L;
        BrandEntity brand = new BrandEntity("Brand", new SymbolEntity("Symbol", ""));
        brand.setId(id);

        when(brandService.findById(id)).thenReturn(brand);

        mockMvc.perform(get("/brands/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Brand"));

        verify(brandService, times(1)).findById(id);
    }

    @Test
    void shouldInsert() throws Exception {
        BrandRequest request = new BrandRequest();
        request.setName("Brand");
        request.setActive(true);

        Brand brand = getBrand();
        Symbol symbol = getSymbol();

        brand.setSymbol(symbol);
        request.setSymbol(symbol);

        BrandEntity entity = brand.toEntity();
        entity.setSymbol(symbol.toEntity());

        when(brandService.save(any(BrandEntity.class))).thenReturn(entity);

        mockMvc.perform(post("/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("BRAND"));

        verify(brandService, times(1)).save(any(BrandEntity.class));
    }

    @Test
    void shouldDelete() throws Exception {
        Long brandId = 1L;
        Long symbolId = 2L;

        BrandEntity mockBrandEntity = new BrandEntity();
        SymbolEntity mockSymbolEntity = new SymbolEntity();
        mockSymbolEntity.setId(symbolId);
        mockBrandEntity.setSymbol(mockSymbolEntity);

        when(brandService.findById(brandId)).thenReturn(mockBrandEntity);
        doNothing().when(brandService).deleteById(brandId);
        doNothing().when(symbolService).deleteById(symbolId);

        mockMvc.perform(delete("/brands/{id}", brandId))
                .andExpect(status().isOk());

        verify(brandService, times(1)).deleteById(brandId);
    }

    @Test
    void shouldUpdate() throws Exception {
        Long id = 1L;

        BrandRequest request = new BrandRequest();
        request.setId(id);
        request.setName("Brand");
        request.setActive(true);

        Brand brand = getBrand();
        Symbol symbol = getSymbol();

        brand.setSymbol(symbol);
        request.setSymbol(symbol);

        BrandEntity entity = brand.toEntity();
        entity.setSymbol(symbol.toEntity());

        when(brandService.findById(id)).thenReturn(entity);
        when(brandService.save(any(BrandEntity.class))).thenReturn(entity);

        mockMvc.perform(put("/brands/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        verify(brandService, times(1)).existsById(id);
        verify(brandService, times(1)).save(entity);
    }

    @Test
    void shouldUpload() throws Exception {
        String filename = "test-image.png";
        String imageBase64 = "base64string";
        BufferedImage mockImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);

        when(brandService.upload(any(MultipartFile.class))).thenReturn(mockImage);
        when(brandService.toBase64(mockImage)).thenReturn(imageBase64);

        MockMultipartFile file = new MockMultipartFile("file", "test-image.png", MediaType.IMAGE_PNG_VALUE, new byte[1]);

        mockMvc.perform(multipart("/brands/upload")
                        .file(file)
                        .param("filename", filename))
                .andExpect(status().isOk());

        verify(brandService, times(1)).upload(file);
    }

    private Brand getBrand() {
        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("Brand");
        brand.setActive(true);
        return brand;
    }

    private Symbol getSymbol() {
        Symbol symbol = new Symbol("Symbol", "das54d5457===");
        symbol.setId(1L);
        return symbol;
    }
}
