package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.FineProjection;
import com.ctsousa.mover.core.entity.*;
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

import java.math.BigDecimal;
import java.util.List;

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
        transaction.setSubcategory(new SubCategory(getSubCategory().getId()));
        TransactionEntity savedTransaction = saveTransaction(transaction);
        fine.setSignature(savedTransaction.getSignature());
        FineEntity fineSaved;
        try {
            fineSaved = fineRepository.save(fine);
        } catch (Exception e) {
            transactionService.deleteById(savedTransaction.getId());
            throw new NotificationException("Erro ao salvar a multa: " + e.getMessage());
        }
        return fineSaved;
    }

    @Override
    public List<FineEntity> findAll() {
        return fineRepository.findAllWithProjection()
                .stream()
                .map(fineProjection -> {
                    FineEntity fine = new FineEntity();
                    fine.setId(fineProjection.getId());
                    fine.setDescription(fineProjection.getDescription());
                    fine.setValue(fineProjection.getValue());
                    fine.setDueDate(fineProjection.getDueDate());
                    fine.setDateTimeOfCommitment(fineProjection.getDateTimeOfCommitment());
                    fine.setPaid(fineProjection.getPaid().compareTo(BigDecimal.ZERO) > 0);
                    fine.setVehicle(getVehicle(fineProjection, fine));
                    return fine;
                })
                .toList();
    }

    private SubCategoryEntity getSubCategory() {
        CategoryEntity category = categoryService.filterBy(TypeCategory.EXPENSE)
                .stream().filter(c -> c.getDescription().equalsIgnoreCase("CARRO"))
                .findFirst().orElseThrow(() -> new NotificationException("Categoria não encontrada"));
        return category.getSubcategories()
                .stream().filter(s -> s.getDescription().equalsIgnoreCase("MULTA"))
                .findFirst().orElseThrow(() -> new NotificationException("Subcategoria não encontrada"));
    }

    private TransactionEntity saveTransaction(Transaction transaction) {
        try {
            return transactionService.save(transaction);
        } catch (Exception e) {
            throw new NotificationException("Erro ao salvar a multa, não foi possivel gerar o lançamento para pagamento. " + e.getMessage());
        }
    }

    private VehicleEntity getVehicle(FineProjection projection, FineEntity entity) {
        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setLicensePlate(projection.getLicensePlate());
        ModelEntity model = new ModelEntity();
        model.setName(projection.getModel());
        BrandEntity brand = new BrandEntity();
        brand.setName(projection.getBrand());
        vehicle.setModel(model);
        vehicle.setBrand(brand);
        return vehicle;
    }
}
