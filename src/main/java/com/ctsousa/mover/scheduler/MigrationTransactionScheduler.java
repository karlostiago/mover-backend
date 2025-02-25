package com.ctsousa.mover.scheduler;

import com.ctsousa.mover.core.entity.*;
import com.ctsousa.mover.core.util.DateUtil;
import com.ctsousa.mover.core.util.StringUtil;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;

@Slf4j
@Component
public class MigrationTransactionScheduler implements Scheduler {

    protected final TransactionRepository repository;
    protected final SubCategoryRepository subCategoryRepository;
    protected final VehicleRepository vehicleRepository;
    protected final AccountRepository accountRepository;
    protected final CardRepository cardRepository;
    protected final PartnerRepository partnerRepository;
    protected final ContractRepository contractRepository;

    public MigrationTransactionScheduler(TransactionRepository repository, SubCategoryRepository subCategoryRepository, VehicleRepository vehicleRepository, AccountRepository accountRepository, CardRepository cardRepository, PartnerRepository partnerRepository, ContractRepository contractRepository) {
        this.repository = repository;
        this.subCategoryRepository = subCategoryRepository;
        this.vehicleRepository = vehicleRepository;
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
        this.partnerRepository = partnerRepository;
        this.contractRepository = contractRepository;
    }

    @Override
//    @Scheduled(cron = "0/1 * * * * *")
    public void process() {
        List<TransactionEntity> trancations = new ArrayList<>();

        List<SubCategoryEntity> subcategories = subCategoryRepository.findAll();
        List<VehicleEntity> vehicles = vehicleRepository.findAll();
        List<AccountEntity> accounts = accountRepository.findAll();
        List<CardEntity> cards = cardRepository.findAll();
        List<PartnerEntity> partners = partnerRepository.findAll();
        List<ContractEntity> contracts = contractRepository.findAll();

        try {
            ClassPathResource resource = new ClassPathResource("transactions.csv");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                reader.readLine();

                String line;
                while ((line = reader.readLine()) != null) {
                    var colums = line.split(";");

                    TransactionEntity entity = new TransactionEntity();

                    TypeCategory typeCategory = TypeCategory.toDescription(colums[0].toUpperCase());
                    entity.setCategoryType(typeCategory.name());
                    entity.setTransactionType(typeCategory.getTransactionType().name());

                    entity.setDescription(colums[2].toUpperCase());

                    if (!colums[3].isEmpty()) {
                        vehicles.stream().filter(v -> v.getLicensePlate().equalsIgnoreCase(colums[3]))
                                .findFirst().ifPresent(entity::setVehicle);
                    }

                    if (!colums[4].isEmpty()) {
                        accounts.stream().filter(acc -> acc.getName().equalsIgnoreCase(colums[4]))
                                .findFirst().ifPresent(entity::setAccount);
                    }

                    if (!colums[5].isEmpty()) {
                        cards.stream().filter(acc -> acc.getName().equalsIgnoreCase(colums[5]))
                                .findFirst().ifPresent(entity::setCard);
                        if (entity.getCard() == null) {
                            cards.stream().filter(acc -> acc.getName().equalsIgnoreCase(StringUtil.normalizer(colums[5])))
                                    .findFirst().ifPresent(entity::setCard);
                        }
                    }

                    if (!colums[6].isEmpty()) {
                        contracts.stream().filter(acc -> acc.getNumber().equalsIgnoreCase(colums[6].replaceAll("^\"+|\"+$", "")))
                                .findFirst().ifPresent(entity::setContract);
                    }

                    if (!colums[7].isEmpty()) {
                        partners.stream().filter(acc -> acc.getName().equalsIgnoreCase(colums[7]))
                                .findFirst().ifPresent(entity::setPartner);
                    }

                    entity.setDueDate(DateUtil.parseToLocalDate(colums[9].replaceAll("^\"+|\"+$", "")));

                    if (!colums[10].isEmpty()) {
                        entity.setPaymentDate(DateUtil.parseToLocalDate(colums[10].replaceAll("^\"+|\"+$", "")));
                    }

                    subcategories.stream().filter(s -> s.getDescription().equalsIgnoreCase(colums[8]))
                            .findFirst().ifPresent(entity::setSubcategory);

                    if (entity.getSubcategory() == null) {
                        subcategories.stream().filter(s -> s.getDescription().equalsIgnoreCase(StringUtil.normalizer(colums[8])))
                                .findFirst().ifPresent(entity::setSubcategory);
                    }

                    entity.setLastInstallment(false);
                    entity.setFrequency(null);
                    entity.setHour(LocalTime.now());
                    entity.setInstallment(0);
                    entity.setValue(new BigDecimal(colums[11]));
                    entity.setPaid(colums[12].equals("1"));
                    entity.setPredicted(colums[13].equals("1"));
                    entity.setPaymentType(null);
                    entity.setRefund(false);
                    entity.setActive(true);
                    entity.setSignature(String.valueOf(randomUUID()));

                    trancations.add(entity);
                }

                List<TransactionEntity> accountIsNull = trancations.stream()
                        .filter(t -> t.getCard() == null)
                        .toList();

                repository.saveAll(trancations);
                System.out.println("finalizado...");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
