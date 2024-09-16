package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.enumeration.BankIcon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountTest {

    private Account account;
    private final String NAME = "Test Account";
    private final Integer CODE_ICON = 1;
    private final String NUMBER = "12345";
    private final BigDecimal INITIAL_BALANCE = new BigDecimal("1000.00");
    private final Boolean CAUTION = true;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setName(NAME);
        account.setCodeIcon(CODE_ICON);
        account.setNumber(NUMBER);
        account.setInitialBalance(INITIAL_BALANCE);
        account.setCaution(CAUTION);
        account.setActive(true);
    }

    @Test
    void testToEntity() {
        AccountEntity entity = account.toEntity();

        assertNotNull(entity);
        assertEquals(account.getId(), entity.getId());
        assertEquals(NAME.toUpperCase(), entity.getName());
        assertEquals(NUMBER, entity.getNumber());
        assertEquals(INITIAL_BALANCE, entity.getInitialBalance());
        assertEquals(CAUTION, entity.getCaution());
        assertEquals("SIM", entity.getActive() ? "SIM" : "NAO");
        assertEquals("SIM", entity.getCaution() ? "SIM" : "NAO");
        assertEquals("3ad39f97cc47fc4fd2219d8af1591fbff17c6c4eb1036a5c36c00aa337e3b308", entity.getHash());
        assertEquals(BankIcon.BANK_BB.name(), entity.getIcon());
    }
}
