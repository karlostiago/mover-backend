package com.ctsousa.mover.core.api;

import com.ctsousa.mover.request.BrandRequest;
import com.ctsousa.mover.response.BrandResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface BrandApi extends Api<BrandRequest, BrandResponse> {

    @PostMapping(value = "upload")
    ResponseEntity<Void> upload(@RequestParam("file") MultipartFile file, @RequestParam("filename") String filename);
}
