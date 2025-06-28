package com.ctsousa.mover.scheduler;

import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.entity.ModelEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.integration.parallelum.ParallelumGateway;
import com.ctsousa.mover.integration.parallelum.domain.Brand;
import com.ctsousa.mover.integration.parallelum.domain.Model;
import com.ctsousa.mover.service.BrandService;
import com.ctsousa.mover.service.ModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.ctsousa.mover.core.util.StringUtil.removeLastPoint;

@Slf4j
@Component
public class ModelScheduler implements Scheduler {

    public static final Queue<String> buffers = new ConcurrentLinkedQueue<>();

    private final BrandService brandService;
    private final ModelService modelService;
    private final ParallelumGateway gateway;

    public ModelScheduler(BrandService brandService, ModelService modelService, ParallelumGateway gateway) {
        this.brandService = brandService;
        this.modelService = modelService;
        this.gateway = gateway;
    }

    @Override
    @Scheduled(cron = "0/10 * * * * *") // executa a cada 10 segundos
    public void process() {

        if (buffers.isEmpty()) return;

        log.info("Iniciado processamento de insert de modelos periodo :: {} ", LocalDateTime.now());
        while (!buffers.isEmpty()) {
            String brandName = buffers.poll();
            BrandEntity brandEntity = brandService.filterByName(brandName)
                    .stream()
                    .findFirst().orElse(null);

            if (brandEntity != null) {
                Brand brand = gateway.findBrand(brandEntity.getName());
                if (brand != null) {
                    List<ModelEntity> entities = findModel(brandEntity, brand.getCode());
                    save(entities);
                }
            }
        }
        log.info("Finalizado processamento de insert de modelos periodo :: {} ", LocalDateTime.now());
    }

    private List<ModelEntity> findModel(BrandEntity brandEntity, String codeBrand) {
        List<ModelEntity> entities = new ArrayList<>();
        List<Model> models = gateway.listModels(codeBrand);
        for (Model model : models) {
            ModelEntity entity = new ModelEntity();
            entity.setName(removeLastPoint(model.getName().toUpperCase()));
            entity.setBrand(brandEntity);
            entities.add(entity);
        }
        return entities;
    }

    private void save(List<ModelEntity> entities) {
        log.info("Total de modelos encontrados {} ", entities.size());
        for (ModelEntity entity : entities) {
            try {
                modelService.save(entity);
            } catch (NotificationException e) {
                // nao precisa lancar erro
            }
        }
    }
}
