package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.notification.NotificationNotFoundException;
import com.ctsousa.mover.core.service.impl.AbstractServiceImpl;
import com.ctsousa.mover.core.validation.CpfValidator;
import com.ctsousa.mover.repository.ClientRepository;
import com.ctsousa.mover.repository.UserRepository;
import com.ctsousa.mover.service.ClientService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
public class ClientServiceImpl extends AbstractServiceImpl <ClientEntity, Long> implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    public ClientServiceImpl(JpaRepository<ClientEntity, Long> repository, ClientRepository clientRepository, UserRepository userRepository) {
        super(repository);
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ClientEntity existsCpfRegistered(String cpf) {
        if (StringUtils.isBlank(cpf)) {
            throw new NotificationException("CPF não fornecido corretamente");
        }
        String formattedCpf = CpfValidator.formatCpf(cpf);

        if (!CpfValidator.isValid(formattedCpf)) {
            throw new NotificationException("CPF inválido");
        }

        ClientEntity client = clientRepository.existsCpfRegisteredInApplication(formattedCpf);
        if (client == null) {
            throw new NotificationNotFoundException("CPF não encontrado no sistema");
        }

        return client;
    }

    @Override
    @Transactional
    public ClientEntity registerClientAndUser(ClientEntity client, UserEntity user) {
        ClientEntity savedClient = repository.save(client);
        user.setClientId(savedClient.getId());

        updateOrSaveUser(user);
        return savedClient;
    }

    private void updateOrSaveUser(UserEntity user) {
        List<UserEntity> usersWithEmail = userRepository.findByEmail(user.getEmail());
        List<UserEntity> usersWithLogin = userRepository.findByLogin(user.getLogin());

        updateExistingUsers(usersWithEmail, user);
        updateExistingUsers(usersWithLogin, user);

        if (usersWithEmail.isEmpty() && usersWithLogin.isEmpty()) {
            userRepository.save(user);
        }
    }

    private void updateExistingUsers(List<UserEntity> existingUsers, UserEntity userEntity) {
        existingUsers.stream()
                .filter(existingUser -> !existingUser.getId().equals(userEntity.getId()))
                .forEach(existingUser -> {
                    updateUserDetails(existingUser, userEntity);
                    userRepository.save(existingUser);
                });
    }

    private void updateUserDetails(UserEntity existingUser, UserEntity entity) {
        existingUser.setLogin(entity.getLogin());
        existingUser.setName(entity.getName());
        existingUser.setPassword(entity.getPassword());
        existingUser.setClientId(entity.getClientId());
    }
}

