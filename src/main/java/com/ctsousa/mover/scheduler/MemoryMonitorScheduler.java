package com.ctsousa.mover.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MemoryMonitorScheduler implements Scheduler{

    private static final long MAX_MEMORY_MB = 512;

    @Override
//    @Scheduled(cron = "0/10 * * * * *") // executa a cada 10 segundos
    public void process() {
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemoryMb = (totalMemory - freeMemory) / (1024L * 1024L);

        if (usedMemoryMb > MAX_MEMORY_MB) {
            log.info("Consumo de memoria alta {} MB. Forcando Garbage Collection.", usedMemoryMb);
            System.gc();
            System.runFinalization();
        } else {
            log.info("Memoria em uso: {}Mb", usedMemoryMb);
        }
    }
}
