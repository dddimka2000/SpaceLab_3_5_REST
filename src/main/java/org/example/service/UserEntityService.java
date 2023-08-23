package org.example.service;

import lombok.extern.log4j.Log4j2;
import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Log4j2
@Service
public class UserEntityService {
    final
    UserRepository userRepository;

    @Autowired
    public UserEntityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserEntity> findByEmail(String email) {
        log.info("UserEntity-findByEmail start: " + email);
        Optional<UserEntity> user;
        user = userRepository.findByEmail(email);
        log.info("UserEntity-findByEmail successful");

        return user;
    }

    public Optional<UserEntity> findByTelephone(String telephone) {
        log.info("UserEntity-findByTelephone start: " + telephone);
        Optional<UserEntity> user;
        user = userRepository.findByTelephone(telephone);
        log.info("UserEntity-findByTelephone successful");

        return user;
    }

    public Optional<UserEntity> findByLogin(String login) {
        log.info("UserEntity-findByLogin start: " + login);
        Optional<UserEntity> user = userRepository.findByLogin(login);
        log.info("UserEntity-findByLogin successful");

        return user;
    }

    public Optional<UserEntity> findById(Integer id) {
        log.info("UserEntity-findById start: " + id);
        Optional<UserEntity> user = userRepository.findById(id);
        log.info("UserEntity-findById successful");
        return user;
    }

    @Transactional
    public void save(UserEntity userEntity) {
//        try (Session session = sessionFactory.openSession()) {

        log.info("UserEntity-saveByLogin start: " + userEntity.getId());
        userRepository.save(userEntity);
        log.info("UserEntity-findByLogin successful");
//        } catch (Exception e) {
//            log.error(e);
//        }

    }

    public void delete(UserEntity userEntity) {
        log.info("UserEntity-delete start: " + userEntity.getId());
        userRepository.delete(userEntity);
        log.info("UserEntity-delete successful");

    }

    public List<UserEntity> findAllUsers() {
        log.info("UserEntity-findAllUsers start");
        List<UserEntity> list = userRepository.findAll();
        if (list.isEmpty()) {
            log.info("UserEntity-findAllUsersPage empty list");
        } else {
            log.info("UserEntity-findAllUsersPage successfully");
        }
        return list;
    }

    public Page<UserEntity> findAllUsersPage(Integer pageNumber, Integer pageSize) {
        log.info("UserEntity-findAllUsersPage start");
        Page<UserEntity> page = null;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        page = userRepository.findAll(pageable);
        log.info("Users with " + pageNumber + " has been found");
        log.info("UserEntity-findAllUsersPage successfully");
        return page;
    }

    public Page<UserEntity> findPageAllUsersBySearchName(String searchName, String sortName
            , Integer pageNumber, Integer pageSize) {
        log.info("UserEntity-findPageAllUsersBySearchName start");
        Page<UserEntity> page = null;
        Pageable pageable = null;
        if (sortName != null && !sortName.isEmpty()) {
            String[] sortParams = sortName.split(",");
            String sortBy = sortParams[0];
            Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC : Sort.Direction.ASC;

            switch (sortBy.toLowerCase()) {
                case "id":
                    pageable = PageRequest.of(pageNumber, pageSize, direction, "id");
                    break;
                case "username":
                    pageable = PageRequest.of(pageNumber, pageSize, direction, "username");
                    break;
                case "email":
                    pageable = PageRequest.of(pageNumber, pageSize, direction, "email");
                    break;
                case "login":
                    pageable = PageRequest.of(pageNumber, pageSize, direction, "login");
                    break;
                default:
                    pageable = PageRequest.of(pageNumber, pageSize);
                    break;
            }
        } else {
            pageable = PageRequest.of(pageNumber, pageSize);
        }
        Page<UserEntity> resultPage;
        if (searchName != null && !searchName.isEmpty()) {
            resultPage = userRepository.findByLoginContainingIgnoreCase(searchName, pageable);
        } else {
            resultPage = userRepository.findAll(pageable);
        }
        return resultPage;
    }

    public long countBy() {
        log.info("UserEntity-countBy");
        long count = userRepository.countBy();
        log.info("UserEntity-countBy get: " + count);
        return count;
    }

    public long countByLogin(String login) {
        log.info("UserEntity-countByLogin " + login);
        long count = userRepository.countByLoginContainingIgnoreCase(login);
        log.info("UserEntity-countByLogin get: " + count);
        return count;
    }
}