package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.repository.AccountRepository;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeAll
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterAll
    void after() {
        accountRepository.deleteAll();
    }

    @Test
    @Order(1)
    void shouldSavedAccount() {
        AccountEntity savedEntity = accountService.save(getAccountEntity(null));
        Assertions.assertNotNull(savedEntity.getId());
    }

    @Test
    @Order(2)
    void shouldUpdatedAccount() {
        AccountEntity entity = accountService.findById(1L);
        entity.setActive(false);
        entity.setName("ACCOUNT NUMBER 2");

        AccountEntity updatedEntity = accountService.save(entity);

        Assertions.assertEquals("ACCOUNT NUMBER 2", updatedEntity.getName());
        Assertions.assertFalse(updatedEntity.getActive());
    }

    @Test
    @Order(3)
    void shouldFilterById() {
        AccountEntity entity = accountService.findById(1L);
        Assertions.assertNotNull(entity.getId());
    }

    @Test
    @Order(4)
    void shouldFilteredAll() {
        List<AccountEntity> entities = accountService.findAll();
        Assertions.assertEquals(1, entities.size());
    }

    @Test
    @Order(5)
    void shouldReturnAllWhenFilterdWithoutParameter() {
        String params = "";
        List<AccountEntity> entities = accountService.filterBy(params);
        Assertions.assertEquals(1, entities.size());

        params = null;
        entities = accountService.filterBy(params);
        Assertions.assertEquals(1, entities.size());
    }

    @Test
    @Order(6)
    void shouldReturnErrorWhenExistsAccount() {
        try {
            AccountEntity entity = getAccountEntity(null);
            accountService.save(entity);
            Assertions.fail();
        } catch (NotificationException e) {
            Assertions.assertEquals("Já existe uma conta cadastrada, com os dados informados.", e.getMessage());
        }
    }

    @Test
    @Order(7)
    void shouldReturnErrorWhenUpdated() {
        try {
            AccountEntity entity = getAccountEntity(null);
            entity.setName("ACCOUNT NUMBER 2");
            entity.setNumber("78946");
            entity.setHash("laksjdlakwjpalkdajlks====/");
            accountService.save(entity);

            AccountEntity filteredEntity = accountService.findById(2L);
            filteredEntity.setName("ACCOUNT NUMBER");
            accountService.save(filteredEntity);
        } catch (NotificationException e) {
            Assertions.assertEquals("Não foi possível atualizar, já tem uma conta, com os dados informado.", e.getMessage());
        }
    }

    @Test
    @Order(8)
    void shouldDeletedById() {
        accountService.deleteById(1L);
        accountService.deleteById(2L);
        List<AccountEntity> entities = accountService.findAll();
        Assertions.assertEquals(0, entities.size());
    }

    private AccountEntity getAccountEntity(Long id) {
        AccountEntity entity = new AccountEntity();
        entity.setId(id);
        entity.setHash("laksjdlakwjpalkdajlks====");
        entity.setName("Account Name");
        entity.setNumber("12346");
        entity.setIcon("BANK_WILL");
        entity.setInitialBalance(BigDecimal.valueOf(1000D));
        entity.setAvailableBalance(entity.getInitialBalance());
        entity.setActive(true);
        entity.setCaution(false);
        return entity;
    }
}
