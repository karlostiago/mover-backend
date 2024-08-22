package com.ctsousa.mover.scheduler;

import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.entity.ModelEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumBrandEntity;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumModelEntity;
import com.ctsousa.mover.integration.fipe.parallelum.gateway.FipeParallelumGateway;
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
public class ModelScheduler implements Scheduler{

    public static final Queue<String> buffers = new ConcurrentLinkedQueue<>();

    private final BrandService brandService;
    private final ModelService modelService;
    private final FipeParallelumGateway gateway;

    public ModelScheduler(BrandService brandService, ModelService modelService, FipeParallelumGateway gateway) {
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
                FipeParallelumBrandEntity fipeBrandEntity = gateway.findByBrand(brandEntity.getName());
                if (fipeBrandEntity != null) {
                    List<ModelEntity> entities = findModel(brandEntity,fipeBrandEntity.getCode());
                    save(entities);
                }
            }
        }
        log.info("Finalizado processamento de insert de modelos periodo :: {} ", LocalDateTime.now());
    }

    private List<ModelEntity> findModel(BrandEntity brandEntity, String codeBrand) {
        List<ModelEntity> entities = new ArrayList<>();
        List<FipeParallelumModelEntity> fipeParallelumModelEntities = gateway.listModels(codeBrand);
        for (FipeParallelumModelEntity fipeParallelumModel : fipeParallelumModelEntities) {
            ModelEntity entity = new ModelEntity();
            entity.setName(removeLastPoint(fipeParallelumModel.getName().toUpperCase()));
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
