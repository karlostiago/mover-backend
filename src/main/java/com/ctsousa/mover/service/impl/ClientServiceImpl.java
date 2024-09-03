package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.notification.NotificationNotFoundException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.core.validation.CpfValidator;
import com.ctsousa.mover.enumeration.BrazilianStates;
import com.ctsousa.mover.integration.viacep.entity.ViaCepEntity;
import com.ctsousa.mover.integration.viacep.gateway.ViaCepGateway;
import com.ctsousa.mover.repository.ClientRepository;
import com.ctsousa.mover.repository.UserRepository;
import com.ctsousa.mover.service.ClientService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;
import static com.ctsousa.mover.core.validation.PasswordValidator.defaultPasswordMover;

@Component
public class ClientServiceImpl extends BaseServiceImpl<ClientEntity, Long> implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ViaCepGateway viaCepGateway;

    public ClientServiceImpl(JpaRepository<ClientEntity, Long> repository, ClientRepository clientRepository, UserRepository userRepository, ViaCepGateway viaCepGateway) {
        super(repository);
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.viaCepGateway = viaCepGateway;
    }

    @Override
    public ClientEntity save(ClientEntity entity) {
        if (entity.isNew()) {
            if (clientRepository.existsByEmail(entity.getEmail())) {
                throw new NotificationException("Já existe um cliente com o e-mail informado.");
            }
            if (clientRepository.existsByCpfCnpj(entity.getCpfCnpj())) {
                throw new NotificationException("Já existe um cliente com o CPF ou CNPJ informado.");
            }
        }
        else {
            if (clientRepository.existsByEmailAndCpfCnpjNotId(entity.getEmail(), entity.getCpfCnpj(), entity.getId())) {
                throw new NotificationException("Não foi possível atualizar, pois já tem um cliente, com email ou cpf informado.", Severity.WARNING);
            }
        }
        ClientEntity savedEntity = super.save(entity);
        associateClientWithUser(savedEntity, null);
        return savedEntity;
    }

    @Override
    public List<ClientEntity> filterBy(String search) {
        if (search == null || search.isEmpty()) return clientRepository.findAll();
        return clientRepository.findBy(toUppercase(search));
    }

    @Override
    public ClientEntity findByAddress(Integer postalCode) {
        ViaCepEntity viaCepEntity = viaCepGateway.findByPostalCode(postalCode);

        if (viaCepEntity.isErro()) {
            return null;
        }

        ClientEntity entity = new ClientEntity();
        entity.setPublicPlace(toUppercase(viaCepEntity.getLogradouro()));
        entity.setNeighborhood(toUppercase(viaCepEntity.getBairro()));
        entity.setCity(toUppercase(viaCepEntity.getLocalidade()));

        BrazilianStates state = BrazilianStates.toName(viaCepEntity.getUf());
        entity.setState(state.name());

        return entity;
    }

    @Override
    public ClientEntity existsCpfRegistered(String cpf) {
        if (StringUtils.isBlank(cpf)) {
            throw new NotificationException("CPF não fornecido corretamente");
        }
        String formattedCpf = CpfValidator.validateAndFormatCpf(cpf);

        if (!CpfValidator.isValid(formattedCpf)) {
            throw new NotificationException("CPF inválido");
        }

        ClientEntity client = clientRepository.findByCpfCnpj(formattedCpf);
        if (client == null) {
            throw new NotificationNotFoundException("CPF não encontrado no sistema");
        }

        return client;
    }

    @Override
    @Transactional
    public ClientEntity registerClient(ClientEntity client, String password) {
        if (userRepository.existsUserEntityByEmail(client.getEmail())) {
            throw new NotificationException("Já existe um usuário registrado com esse email");
        }

        if (userRepository.existsUserEntityByLogin(client.getEmail())) {
            throw new NotificationException("Já existe um usuário registrado com esse login");
        }

        associateClientWithUser(client, password);

        return client;
    }

    private void associateClientWithUser(ClientEntity client, String password) {
        UserEntity user = client.getUser();
        if (user == null) {
            user = createUser(client, password);
            client.setUser(user);
        }
        else {
            user.setPassword(password == null ? user.getPassword() : password);
            client.setUser(user);
        }
        userRepository.save(user);
        clientRepository.save(client);
    }

    private UserEntity createUser(ClientEntity client, String password) {
        UserEntity user = new UserEntity();
        user.setName(client.getName());
        user.setEmail(toUppercase(client.getEmail()));
        user.setLogin(toUppercase(client.getEmail()));
        user.setClientId(client.getId());
        user.setPassword(password == null ? defaultPasswordMover() : password);
        return user;
    }
}