package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.notification.NotificationNotFoundException;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.core.validation.CpfValidator;
import com.ctsousa.mover.repository.ClientRepository;
import com.ctsousa.mover.repository.UserRepository;
import com.ctsousa.mover.service.ClientService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.ctsousa.mover.core.validation.CpfValidator.validateAndFormatCpf;

@Component
public class ClientServiceImpl extends BaseServiceImpl<ClientEntity, Long> implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    public ClientServiceImpl(JpaRepository<ClientEntity, Long> repository, ClientRepository clientRepository, UserRepository userRepository) {
        super(repository);
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ClientEntity existsCpfRegistered(String cpf) {

        String formattedCpf = validateAndFormatCpf(cpf);
        ClientEntity client = clientRepository.existsCpfRegisteredInApplication(formattedCpf);
        if (client == null) {
            throw new NotificationNotFoundException("Dados do cliente não encontrados.");
        }

        return client;
    }




    @Override
    @Transactional
    public ClientEntity registerClient(ClientEntity client, String password) {

        if(clientRepository.existsClientEntityByEmail(client.getEmail())) {
            throw new NotificationException("Já existe um cliente registrado com esse email");
        }

        if (clientRepository.existsCpfRegisteredInApplication(client.getCpf()) != null) {
            throw new NotificationException("Já existe um cliente registrado com esse CPF");
        }

        if (userRepository.existsUserEntityByEmail(client.getEmail())) {
            throw new NotificationException("Já existe um usuário registrado com esse email");
        }

        if (userRepository.existsUserEntityByLogin(client.getEmail())) {
            throw new NotificationException("Já existe um usuário registrado com esse login");
        }

        ClientEntity savedClient = clientRepository.save(client);
        createUserForClient(savedClient, password);

        return savedClient;
    }

    @Override
    @Transactional
    public ClientEntity updateClient(Long clientId, ClientEntity clientUpdates) {
        ClientEntity existingClient = clientRepository.findById(clientId)
                .orElseThrow(() -> new NotificationNotFoundException("Cliente não encontrado"));

        if (!existingClient.getCpf().equals(clientUpdates.getCpf()) &&
                clientRepository.existsCpfRegisteredInApplication(clientUpdates.getCpf()) != null) {
            throw new NotificationException("Já existe um cliente registrado com esse CPF");
        }

        if (!existingClient.getEmail().equals(clientUpdates.getEmail()) &&
                userRepository.existsUserEntityByEmail(clientUpdates.getEmail())) {
            throw new NotificationException("Já existe um usuário registrado com esse email");
        }

        if (!existingClient.getEmail().equals(clientUpdates.getEmail()) &&
                clientRepository.existsClientEntityByEmail(clientUpdates.getEmail())) {
            throw new NotificationException("Já existe um cliente registrado com esse email");
        }

        if (!existingClient.getUser().getLogin().equals(clientUpdates.getUser().getLogin()) &&
                userRepository.existsUserEntityByLogin(clientUpdates.getUser().getLogin())) {
            throw new NotificationException("Já existe um usuário registrado com esse login");
        }

        existingClient.setName(clientUpdates.getName());
        existingClient.setRg(clientUpdates.getRg());
        existingClient.setCpf(clientUpdates.getCpf());
        existingClient.setBirthDate(clientUpdates.getBirthDate());
        existingClient.setEmail(clientUpdates.getEmail());
        existingClient.setMotherName(clientUpdates.getMotherName());
        existingClient.setPublicPlace(clientUpdates.getPublicPlace());
        existingClient.setNumber(clientUpdates.getNumber());
        existingClient.setComplement(clientUpdates.getComplement());
        existingClient.setDistrict(clientUpdates.getDistrict());
        existingClient.setState(clientUpdates.getState());
        existingClient.setCep(clientUpdates.getCep());
        existingClient.setAttachment(clientUpdates.getAttachment());

        UserEntity existingUser = existingClient.getUser();

        if (existingUser != null) {
            existingUser.setName(clientUpdates.getUser().getName());
            existingUser.setEmail(clientUpdates.getUser().getEmail());
            if (StringUtils.isNotBlank(clientUpdates.getUser().getPassword())) {
                existingUser.setPassword(clientUpdates.getUser().getPassword());
            }
        }

        return clientRepository.save(existingClient);
    }


    private void createUserForClient(ClientEntity savedClient, String password) {
        UserEntity user = createUserFromClient(savedClient, password);
        savedClient.setUser(user);
        userRepository.save(user);
    }

    private UserEntity createUserFromClient(ClientEntity client, String password) {
        UserEntity user = new UserEntity();
        user.setName(client.getName());
        user.setEmail(client.getEmail().toLowerCase());
        user.setLogin(client.getEmail().toLowerCase());
        user.setClientId(client.getId());
        user.setPassword(password);
        return user;
    }
}
