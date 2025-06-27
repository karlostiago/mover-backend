package com.ctsousa.mover.integration.corpvs;

import java.time.LocalDate;

public interface CorpvsGateway {

    Double totalOdometer(String cliId, String veiId, LocalDate date);
}
