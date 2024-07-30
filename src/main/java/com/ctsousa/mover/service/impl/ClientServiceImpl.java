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
import static com.ctsousa.mover.core.validation.PasswordValidator.defaultPasswordMover;

@Component
public class ClientServiceImpl extends AbstractServiceImpl<ClientEntity, Long> implements ClientService {

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
    public ClientEntity registerClient(ClientEntity client, String password) {
        ClientEntity existingClient = removeExistingClientIfDefaultPassword(client);

        ClientEntity savedClient = clientRepository.save(client);
        UserEntity user = client.getUser();

        if (user == null) {
            createUserForClient(savedClient, password);
        } else {
            updateUserIfExists(user, existingClient, password);
        }

        return savedClient;
    }

    private ClientEntity removeExistingClientIfDefaultPassword(ClientEntity client) {
        ClientEntity existingClient = clientRepository.findByEmail(client.getEmail());

        if (existingClient != null && defaultPasswordMover().equals(existingClient.getUser().getPassword())) {
            clientRepository.delete(existingClient);
            existingClient = null;
        }

        return existingClient;
    }

    private void createUserForClient(ClientEntity savedClient, String password) {
        String defaultPassword = defaultPasswordMover();
        UserEntity user = createUserFromClient(savedClient, defaultPassword);
        user.setPassword(password);
        savedClient.setUser(user);
        userRepository.save(user);
    }

    private void updateUserIfExists(UserEntity user, ClientEntity existingClient, String password) {
        if (existingClient != null && existingClient.getUser() != null) {
            user.setId(existingClient.getUser().getId());
        }
        if (StringUtils.isNotBlank(password)) {
            user.setPassword(password);
        }
    }

    private UserEntity createUserFromClient(ClientEntity client, String password) {
        UserEntity user = new UserEntity();
        user.setName(client.getName());
        user.setEmail(client.getEmail());
        user.setLogin(client.getEmail());
        user.setClientId(client.getId());
        user.setPassword(password);

        return user;
    }
}
