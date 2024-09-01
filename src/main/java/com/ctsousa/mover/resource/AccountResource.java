package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.AccountApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.domain.Account;
import com.ctsousa.mover.enumeration.BankIcon;
import com.ctsousa.mover.request.AccountRequest;
import com.ctsousa.mover.response.AccountResponse;
import com.ctsousa.mover.response.BankIconResponse;
import com.ctsousa.mover.response.BrandResponse;
import com.ctsousa.mover.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

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
    public ResponseEntity<AccountResponse> add(AccountRequest request) {
        Account account = toMapper(request, Account.class);
        AccountEntity entity = accountService.save(account.toEntity());
        return ResponseEntity.ok(toMapper(entity, AccountResponse.class));
    }

    @Override
    public ResponseEntity<AccountResponse> update(Long id, AccountRequest request) {
        accountService.existsById(id);
        Account account = toMapper(request, Account.class);
        AccountEntity entity = account.toEntity();
        accountService.save(entity);
        return ResponseEntity.ok(toMapper(entity, AccountResponse.class));
    }

    @Override
    public ResponseEntity<List<BankIconResponse>> findAllIcons() {
        List<BankIcon> icons = Stream.of(BankIcon.values())
                .sorted(Comparator.comparing(BankIcon::getBankName))
                .toList();
        return ResponseEntity.ok(toCollection(icons, BankIconResponse.class));
    }

    @Override
    public ResponseEntity<List<AccountResponse>> filterBy(String search) {
        List<AccountEntity> entities = accountService.filterBy(search);
        return ResponseEntity.ok(toCollection(entities, AccountResponse.class));
    }

    @Override
    public Class<?> responseClass() {
        return AccountResponse.class;
    }
}
