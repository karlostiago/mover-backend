package com.ctsousa.mover.integration.corpvs.response;

import com.ctsousa.mover.integration.corpvs.domain.Resume;
import com.ctsousa.mover.integration.corpvs.domain.TripData;
import com.ctsousa.mover.integration.corpvs.domain.Vehicle;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CorpvsResponse {
    private boolean success;
    private List<TripData> data;
    private List<Vehicle> vehicles;
    private Resume resume;
}
