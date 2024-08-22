package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.entity.SymbolEntity;
import com.ctsousa.mover.repository.SymbolRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SymbolResource.class)
public class SymbolResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SymbolRepository symbolRepository;

    @InjectMocks
    private SymbolResource symbolResource;

    @Test
    void shouldReturnAnSuccessWhenFindAll() throws Exception {
        SymbolEntity symbol1 = new SymbolEntity("Symbol1", "dajlksjdlaks");
        SymbolEntity symbol2 = new SymbolEntity("Symbol2", "açlsdçaldj====");
        List<SymbolEntity> symbols = Arrays.asList(symbol1, symbol2);

        when(symbolRepository.findAll()).thenReturn(symbols);

        mockMvc.perform(get("/symbols"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Symbol1"))
                .andExpect(jsonPath("$[1].description").value("Symbol2"));

        verify(symbolRepository, times(1)).findAll();
    }
}
