package com.ctsousa.mover.core.api;

import com.ctsousa.mover.request.UserRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface ChangePasswordApi {
    @PutMapping("/changepassword")
    ResponseEntity<Void> changePassword(@RequestBody @Valid UserRequest request);
}
