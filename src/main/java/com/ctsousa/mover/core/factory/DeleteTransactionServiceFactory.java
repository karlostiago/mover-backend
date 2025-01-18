package com.ctsousa.mover.core.factory;

import com.ctsousa.mover.core.command.TransactionCommand;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.service.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DeleteTransactionServiceFactory implements TransactionExecutor {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final InvestimentService investimentService;
    private final CorporateCapitalService corporateCapitalService;
    private final TransferService transferService;

    private Boolean batchDelete = Boolean.FALSE;

    public DeleteTransactionServiceFactory(IncomeService incomeService, ExpenseService expenseService, InvestimentService investimentService, CorporateCapitalService corporateCapitalService, TransferService transferService) {
        this.incomeService = incomeService;
        this.expenseService = expenseService;
        this.investimentService = investimentService;
        this.corporateCapitalService = corporateCapitalService;
        this.transferService = transferService;
    }

    @Override
    public TransactionEntity execute(final TypeCategory type, final Transaction transaction) {
        Map<TypeCategory, TransactionCommand> mapServicies = new HashMap<>();
        mapServicies.put(TypeCategory.INCOME, incomeService);
        mapServicies.put(TypeCategory.EXPENSE, expenseService);
        mapServicies.put(TypeCategory.INVESTMENT, investimentService);
        mapServicies.put(TypeCategory.CORPORATE_CAPITAL, corporateCapitalService);
        mapServicies.put(TypeCategory.TRANSFER, transferService);
        if (batchDelete) {
            mapServicies.get(type).batchDelete(transaction.getId());
        } else {
            mapServicies.get(type).delete(transaction.getId());
        }
        return new TransactionEntity();
    }

    public void batchDelete(boolean batchDelete) {
        this.batchDelete = batchDelete;
    }
}
