package com.ctsousa.mover.core.api;

import com.ctsousa.mover.request.ModelRequest;
import com.ctsousa.mover.response.ModelResponse;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface ModelApi extends Api<ModelRequest, ModelResponse> {

    @GetMapping(value = "/filterBy")
    ResponseEntity<List<ModelResponse>> filterBy(@Param("search") String search);
}
