package com.ctsousa.mover.core.api;

import com.ctsousa.mover.request.BrandRequest;
import com.ctsousa.mover.response.BrandResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BrandApi extends Api<BrandRequest, BrandResponse> {

    @PostMapping(value = "upload")
    ResponseEntity<Void> upload(@RequestParam("file") MultipartFile file, @RequestParam("filename") String filename);

    @GetMapping(value = "/filterBy")
    ResponseEntity<List<BrandResponse>> filterByName(@RequestParam("name") String name);
}
