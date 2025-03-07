package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.core.validation.CpfValidator;
import com.ctsousa.mover.repository.ClientRepository;
import com.ctsousa.mover.repository.UserRepository;
import com.ctsousa.mover.service.CustomUserDetailService;
import com.ctsousa.mover.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

@Component
public class UserServiceImpl extends BaseServiceImpl<UserEntity, Long> implements UserService {

    @Autowired
    private UserRepository userRepository;

    private final ClientRepository clientRepository;
    private final CustomUserDetailService customUserDetailService;

    public UserServiceImpl(JpaRepository<UserEntity, Long> repository, ClientRepository clientRepository, CustomUserDetailService customUserDetailService) {
        super(repository);
        this.clientRepository = clientRepository;
        this.customUserDetailService = customUserDetailService;
    }

    @Override
    public void changePassword(UserEntity entity) {
        existsById(entity.getId());
        userRepository.updatePassword(entity.getId(), entity.getPassword());
    }

    @Override
    public UserEntity save(UserEntity entity) {
        if (entity.isNew()) {
            if (userRepository.existsUserEntityByEmail(toUppercase(entity.getEmail())) ||
                    userRepository.existsUserEntityByLogin(entity.getLogin())) {
                throw new NotificationException("Já existe um usuário com o email informado.");
            }
            return super.save(entity);
        } else {
            userRepository.updateNameAndEmail(entity.getId(), entity.getName(), entity.getEmail(), entity.getActive());
            UserEntity entityFound = findById(entity.getId());
            entityFound.setProfiles(entity.getProfiles());
            userRepository.save(entityFound);
            return entityFound;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity login(String cpf, String password) {
        String formattedCpf = CpfValidator.validateAndFormatCpf(cpf);
        ClientEntity client = clientRepository.findByCpfCnpj(formattedCpf);

        if (client == null) {
            throw new NotificationException("CPF ou senha inválidos");
        }

        List<UserEntity> userEntities = userRepository.findByClientIdAndPassword(client.getId(),password);
        return userEntities.stream()
                .findFirst()
                .orElseThrow(() -> new NotificationException("Cpf ou senha incorretos."));
    }

    @Override
    public List<UserEntity> filterBy(String search) {
        List<UserEntity> entities = userRepository.findAll();

        if (search == null || search.isEmpty()) return entities;

        return entities.stream()
                .filter(user -> user.getName().contains(toUppercase(search)) ||
                        user.getEmail().contains(toUppercase(search)))
                .toList();
    }

    @Override
    public UserEntity getUser() {
        return customUserDetailService.getUser();
    }
}
