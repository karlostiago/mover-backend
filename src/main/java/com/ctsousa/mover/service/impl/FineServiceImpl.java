package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.CategoryEntity;
import com.ctsousa.mover.core.entity.FineEntity;
import com.ctsousa.mover.core.entity.SubCategoryEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.domain.SubCategory;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.repository.FineRepository;
import com.ctsousa.mover.service.CategoryService;
import com.ctsousa.mover.service.FineService;
import com.ctsousa.mover.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FineServiceImpl extends BaseServiceImpl<FineEntity, Long> implements FineService {

    @Autowired
    private FineRepository fineRepository;
    private final TransactionService transactionService;
    private final CategoryService categoryService;

    public FineServiceImpl(FineRepository fineRepository, TransactionService transactionService, CategoryService categoryService) {
        super(fineRepository);
        this.transactionService = transactionService;
        this.categoryService = categoryService;
    }

    @Override
    public FineEntity save(FineEntity entity) {
        throw new NotificationException("Metodo não suportado");
    }

    @Override
    public FineEntity save(FineEntity fine, Transaction transaction) {
        fine.setSignature(transaction.getSignature());
        transaction.setSubcategory(new SubCategory(getSubCategory().getId()));
        FineEntity fineSaved;

        try {
            fineSaved = fineRepository.save(fine);
            transactionService.save(transaction);
        } catch (Exception e) {
            throw new NotificationException("Erro ao salvar a multa: " + e.getMessage());
        }

        return fineSaved;
    }

    private SubCategoryEntity getSubCategory() {
        CategoryEntity category = categoryService.filterBy(TypeCategory.EXPENSE)
                .stream().filter(c -> c.getDescription().equalsIgnoreCase("CARRO"))
                .findFirst().orElseThrow(() -> new NotificationException("Categoria não encontrada"));
        return category.getSubcategories()
                .stream().filter(s -> s.getDescription().equalsIgnoreCase("MULTA"))
                .findFirst().orElseThrow(() -> new NotificationException("Subcategoria não encontrada"));
    }
}
