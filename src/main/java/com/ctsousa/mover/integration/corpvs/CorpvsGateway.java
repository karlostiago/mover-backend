package com.ctsousa.mover.integration.corpvs;

import com.ctsousa.mover.integration.corpvs.domain.Vehicle;

import java.time.LocalDate;
import java.util.List;

public interface CorpvsGateway {

    Double totalOdometer(Integer veiId, LocalDate date);

    List<Vehicle> allVehicles();
}
