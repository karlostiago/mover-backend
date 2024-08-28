package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.AccountApi;
import com.ctsousa.mover.core.api.BrandApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.entity.SymbolEntity;
import com.ctsousa.mover.domain.Brand;
import com.ctsousa.mover.domain.Symbol;
import com.ctsousa.mover.request.AccountRequest;
import com.ctsousa.mover.request.BrandRequest;
import com.ctsousa.mover.response.AccountResponse;
import com.ctsousa.mover.response.BrandResponse;
import com.ctsousa.mover.scheduler.ModelScheduler;
import com.ctsousa.mover.service.AccountService;
import com.ctsousa.mover.service.BrandService;
import com.ctsousa.mover.service.SymbolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.util.List;

import static com.ctsousa.mover.core.mapper.Transform.toCollection;
import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@RestController
@RequestMapping("/accounts")
public class AccountResource extends BaseResource<AccountResponse, AccountRequest, AccountEntity> implements AccountApi {

    @Autowired
    private AccountService accountService;

    public AccountResource(AccountService accountService) {
        super(accountService);
    }

    @Override
    public ResponseEntity<AccountResponse> add(AccountRequest requestBody) {
        return null;
    }

    @Override
    public ResponseEntity<AccountResponse> update(Long id, AccountRequest requestBody) {
        return null;
    }

    @Override
    public Class<?> responseClass() {
        return AccountResponse.class;
    }
}
