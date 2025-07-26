package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.FineProjection;
import com.ctsousa.mover.core.entity.*;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.domain.SubCategory;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.repository.FineRepository;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.CategoryService;
import com.ctsousa.mover.service.FineService;
import com.ctsousa.mover.service.InvoiceService;
import com.ctsousa.mover.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Component
public class FineServiceImpl extends BaseServiceImpl<FineEntity, Long> implements FineService {

    @Autowired
    private FineRepository fineRepository;
    private final TransactionService transactionService;
    private final CategoryService categoryService;
    private final TransactionRepository transactionRepository;
    private final InvoiceService invoiceService;

    public FineServiceImpl(FineRepository fineRepository, TransactionService transactionService, CategoryService categoryService, TransactionRepository transactionRepository, InvoiceService invoiceService) {
        super(fineRepository);
        this.transactionService = transactionService;
        this.categoryService = categoryService;
        this.transactionRepository = transactionRepository;
        this.invoiceService = invoiceService;
    }

    @Override
    public FineEntity save(FineEntity entity) {
        throw new NotificationException("Metodo não suportado");
    }

    @Override
    public FineEntity update(FineEntity entity, Transaction transaction) {
        FineEntity existingFine = fineRepository.findById(entity.getId())
                .orElseThrow(() -> new NotificationException("Erro ao atualizar, multa não encontrada"));

        TransactionEntity existingTransaction = transactionRepository.findBySignature(existingFine.getSignature())
                .stream().findFirst()
                .orElseThrow(() -> new NotificationException("Erro ao atualizar, multa não esta sincronizada."));

        if (Objects.nonNull(existingTransaction.getPaid()) && existingTransaction.getPaid()) {
            throw new NotificationException("Multa já paga, não é possível atualizar.");
        }

        if (Objects.nonNull(entity.getCard()) && entity.getCard().getId() == null) {
            entity.setCard(null);
        }

        transaction.setId(existingTransaction.getId());
        transaction.setSubcategory(new SubCategory(getSubCategory().getId()));
        TransactionEntity updatedTransaction = transactionService.update(transaction);
        entity.setSignature(updatedTransaction.getSignature());
        fineRepository.save(entity);

        Boolean differentDueDate = !transaction.getDueDate().isEqual(existingTransaction.getDueDate())
                && Objects.nonNull(transaction.getCard());

        Boolean differentCard = Objects.nonNull(transaction.getCard()) && Objects.nonNull(existingTransaction.getCard())
            && !transaction.getCard().getId().equals(existingTransaction.getCard().getId());
        
        if (differentDueDate || differentCard) {
            invoiceService.updateBalance(existingTransaction.getDueDate(), existingTransaction.getCard());
        }

        return entity;
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
            throw new NotificationException("Erro ao salvar ou atualiza uma multa: " + e.getMessage());
        }
        return fineSaved;
    }

    @Override
    public FineEntity synchronize(FineEntity entity, Transaction transaction) {
        TransactionEntity existingTransaction = transactionRepository.findBySignature(entity.getSignature())
                .stream().findFirst()
                .orElse(null);

        if (existingTransaction != null) {
            throw new NotificationException("Esta multa já se encontra sincronizada.", Severity.WARNING);
        }

        transaction.setSubcategory(new SubCategory(getSubCategory().getId()));
        TransactionEntity savedTransaction = saveTransaction(transaction);
        entity.setSignature(savedTransaction.getSignature());
        fineRepository.save(entity);
        return entity;
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
                    fine.setVehicle(getVehicle(fineProjection));
                    fine.setTransactionId(fineProjection.getTransactionId());
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

    private VehicleEntity getVehicle(FineProjection projection) {
        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setLicensePlate(projection.getLicensePlate());
        vehicle.setModel(getModel(projection));
        vehicle.setBrand(getBrand(projection));
        return vehicle;
    }

    private ModelEntity getModel(FineProjection projection) {
        ModelEntity model = new ModelEntity();
        model.setName(projection.getModel());
        return model;
    }

    private BrandEntity getBrand(FineProjection projection) {
        BrandEntity brand = new BrandEntity();
        brand.setName(projection.getBrand());
        return brand;
    }
}
