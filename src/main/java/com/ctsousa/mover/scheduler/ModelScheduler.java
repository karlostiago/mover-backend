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
import java.util.List;

@Slf4j
@Component
public class ModelScheduler implements Scheduler{

    private final BrandService brandService;
    private final ModelService modelService;
    private final FipeParallelumGateway gateway;

    public ModelScheduler(BrandService brandService, ModelService modelService, FipeParallelumGateway gateway) {
        this.brandService = brandService;
        this.modelService = modelService;
        this.gateway = gateway;
    }

    @Override
    @Scheduled(cron = "0 5 6 1 * *") // executa 1 vez por mes as 06h05 horas da manha
    public void process() {
        log.info("Iniciado processamento de insert de modelos periodo :: {} ", LocalDateTime.now());
        List<BrandEntity> entities = brandService.findAll();
        List<FipeParallelumBrandEntity> fipeParallelumBrandEntities = gateway.listBrands();

        for (BrandEntity entity : entities) {
            for (FipeParallelumBrandEntity fipeBrandEntity : fipeParallelumBrandEntities) {
                String brandNameSystem = entity.getName().toUpperCase();
                String brandNameApi = fipeBrandEntity.getName().toUpperCase();
                if (brandNameApi.contains(brandNameSystem)) {
                    findModel(entity, fipeBrandEntity.getCode());
                    break;
                }
            }
        }
        log.info("Finalizado processamento de insert de modelos periodo :: {} ", LocalDateTime.now());
    }

    private void findModel(BrandEntity brandEntity, String codeBrand) {
        List<FipeParallelumModelEntity> fipeParallelumModelEntities = gateway.listModels(codeBrand);
        for (FipeParallelumModelEntity fipeParallelumModel : fipeParallelumModelEntities) {
            ModelEntity entity = new ModelEntity();
            entity.setName(fipeParallelumModel.getName().toUpperCase());
            entity.setBrand(brandEntity);
            saveModel(entity);
        }
    }

    private void saveModel(ModelEntity entity) {
        try {
            modelService.save(entity);
        } catch (NotificationException e) {
            // nao precisa lancar erro
        }
    }
}
