package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.domain.Account;
import com.ctsousa.mover.request.AccountRequest;
import com.ctsousa.mover.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static com.ctsousa.mover.core.mapper.Transform.toMapper;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountResource.class)
public class ClientResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldFilterByName() throws Exception {
        List<AccountEntity> entities = List.of(getAccountEntity(1L, "Account Name 1"), getAccountEntity(2L, "Account Name 2"));

        when(accountService.filterBy("Account Name 1")).thenReturn(entities);

        mockMvc.perform(get("/accounts/filterBy")
                        .param("search", "Account Name 1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Account Name 1"));

        verify(accountService, times(1)).filterBy("Account Name 1");
    }

    @Test
    void shouldFindAll() throws Exception {

        List<AccountEntity> entities = List.of(getAccountEntity(1L, "Account Name 1"), getAccountEntity(2L, "Account Name 2"));

        when(accountService.findAll()).thenReturn(entities);

        mockMvc.perform(get("/accounts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Account Name 1"))
                .andExpect(jsonPath("$[1].name").value("Account Name 2"));

        verify(accountService, times(1)).findAll();
    }

    @Test
    void shouldFindAllIcons() throws Exception {

        mockMvc.perform(get("/accounts/icons")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bankName").value("AGI Bank"))
                .andExpect(jsonPath("$[1].bankName").value("B2B"));
    }

    @Test
    void shouldFindById() throws Exception {
        Long id = 1L;
        AccountEntity entity = getAccountEntity(1L, "Account Name");

        when(accountService.findById(id)).thenReturn(entity);

        mockMvc.perform(get("/accounts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Account Name"));

        verify(accountService, times(1)).findById(id);
    }

    @Test
    void shouldInsert() throws Exception {
        AccountRequest request = getRequest();

        AccountEntity entity = getAccountEntity(1L, "Account Name");

        when(accountService.save(any(AccountEntity.class))).thenReturn(entity);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Account Name"));

        verify(accountService, times(1)).save(any(AccountEntity.class));
    }

    @Test
    void shouldDelete() throws Exception {
        Long accountId = 1L;

        AccountRequest request = getRequest();
        Account domain = toMapper(request, Account.class);

        when(accountService.findById(accountId)).thenReturn(domain.toEntity());
        doNothing().when(accountService).deleteById(accountId);

        mockMvc.perform(delete("/accounts/{id}", accountId))
                .andExpect(status().isOk());

        verify(accountService, times(1)).existsById(accountId);
        verify(accountService, times(1)).deleteById(accountId);
    }

    @Test
    void shouldUpdate() throws Exception {
        Long id = 1L;

        AccountRequest request = getRequest();
        Account domain = toMapper(request, Account.class);
        AccountEntity entity = domain.toEntity();

        when(accountService.findById(id)).thenReturn(entity);
        when(accountService.save(any(AccountEntity.class))).thenReturn(entity);

        mockMvc.perform(put("/accounts/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        verify(accountService, times(1)).existsById(id);
        verify(accountService, times(1)).save(entity);
    }

    private AccountRequest getRequest() {
        AccountRequest request = new AccountRequest();
        request.setId(1L);
        request.setInitialBalance(BigDecimal.valueOf(1000D));
        request.setCodeIcon(22);
        request.setName("Account Name");
        request.setNumber("12346");
        request.setActive(true);
        request.setCaution(false);
        return request;
    }

    private AccountEntity getAccountEntity(Long id, String accountName) {
        AccountEntity entity = new AccountEntity();
        entity.setId(id);
        entity.setName(accountName);
        entity.setNumber("12346");
        entity.setIcon("BANK_WILL");
        entity.setInitialBalance(BigDecimal.valueOf(1000D));
        entity.setActive(true);
        entity.setCaution(false);
        return entity;
    }
}
