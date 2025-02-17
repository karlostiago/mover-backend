package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.AccountApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.security.Security;
import com.ctsousa.mover.domain.Account;
import com.ctsousa.mover.enumeration.BankIcon;
import com.ctsousa.mover.request.AccountRequest;
import com.ctsousa.mover.response.AccountResponse;
import com.ctsousa.mover.response.BankIconResponse;
import com.ctsousa.mover.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize(Security.PreAutorize.Account.REGISTER_ACCOUNTS)
    public ResponseEntity<AccountResponse> add(AccountRequest request) {
        Account account = toMapper(request, Account.class);
        AccountEntity entity = accountService.save(account.toEntity());
        return ResponseEntity.ok(toMapper(entity, AccountResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Account.UPDATE_ACCOUNTS)
    public ResponseEntity<AccountResponse> update(Long id, AccountRequest request) {
        accountService.existsById(id);
        Account account = toMapper(request, Account.class);
        AccountEntity entity = account.toEntity();
        accountService.save(entity);
        return ResponseEntity.ok(toMapper(entity, AccountResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Account.DELETE_ACCOUNTS)
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Account.FILTER_ACCOUNTS)
    public ResponseEntity<List<AccountResponse>> findAll() {
        return super.findAll();
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Account.SEARCH_ACCOUNTS_ICONS)
    public ResponseEntity<List<BankIconResponse>> findAllIcons() {
        List<BankIcon> icons = Stream.of(BankIcon.values())
                .sorted(Comparator.comparing(BankIcon::getBankName))
                .toList();
        return ResponseEntity.ok(toCollection(icons, BankIconResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Account.FILTER_ACCOUNTS)
    public ResponseEntity<List<AccountResponse>> filterBy(String search) {
        List<AccountEntity> entities = accountService.filterBy(search);
        return ResponseEntity.ok(toCollection(entities, AccountResponse.class));
    }

    @Override
    public Class<?> responseClass() {
        return AccountResponse.class;
    }
}
