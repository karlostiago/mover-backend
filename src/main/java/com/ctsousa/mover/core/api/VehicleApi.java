package com.ctsousa.mover.core.api;

import com.ctsousa.mover.request.VehicleRequest;
import com.ctsousa.mover.response.VehicleResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface VehicleApi extends Api<VehicleRequest, VehicleResponse> {

    @GetMapping("/{licensePlate}/license-plate")
    ResponseEntity<VehicleResponse> findByLicensePlate(@PathVariable("licensePlate") String licensePlate);

    @GetMapping("/{renavam}/renavam")
    ResponseEntity<VehicleResponse> findByRenavam(@PathVariable("renavam") String renavam);
}
