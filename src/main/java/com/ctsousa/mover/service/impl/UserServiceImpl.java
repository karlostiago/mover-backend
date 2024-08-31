package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.core.validation.CpfValidator;
import com.ctsousa.mover.repository.ClientRepository;
import com.ctsousa.mover.repository.UserRepository;
import com.ctsousa.mover.service.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
public class UserServiceImpl extends BaseServiceImpl<UserEntity, Long> implements UserService {
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    public UserServiceImpl(JpaRepository<UserEntity, Long> repository, UserRepository userRepository, ClientRepository clientRepository) {
        super(repository);
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity login(String cpf, String password) {
        String formattedCpf = CpfValidator.formatCpf(cpf);
        ClientEntity client = null;//clientRepository.existsCpfCnpjRegisteredInApplication(formattedCpf);

        if (client == null) {
            throw new NotificationException("CPF ou senha inválidos");
        }

        List<UserEntity> userEntities = userRepository.findByClientIdAndPassword(client.getId(),password);
        return userEntities.stream()
                .findFirst()
                .orElseThrow(() -> new NotificationException("Cpf ou senha incorretos."));
    }
}
