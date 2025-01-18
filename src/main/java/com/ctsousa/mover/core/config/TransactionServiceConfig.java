package com.ctsousa.mover.core.config;

import com.ctsousa.mover.core.command.TransactionCommand;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TransactionServiceConfig {

    @Bean
    public Map<TypeCategory, TransactionCommand> services(
            IncomeService incomeService,
            ExpenseService expenseService,
            InvestimentService investimentService,
            CorporateCapitalService corporateCapitalService,
            TransferService transferService) {

        Map<TypeCategory, TransactionCommand> serviceMap = new HashMap<>();
        serviceMap.put(TypeCategory.INCOME, incomeService);
        serviceMap.put(TypeCategory.EXPENSE, expenseService);
        serviceMap.put(TypeCategory.INVESTMENT, investimentService);
        serviceMap.put(TypeCategory.CORPORATE_CAPITAL, corporateCapitalService);
        serviceMap.put(TypeCategory.TRANSFER, transferService);
        return serviceMap;
    }
}
