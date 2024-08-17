package com.ctsousa.mover.core.api;

import com.ctsousa.mover.request.VehicleRequest;
import com.ctsousa.mover.response.VehicleResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface VehicleApi extends Api<VehicleRequest, VehicleResponse> {

    @GetMapping("/findBy")
    ResponseEntity<List<VehicleResponse>> findBy(@RequestParam("search") String search);
}
