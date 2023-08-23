package org.example.repository;

import org.example.entity.UserEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    Optional<UserEntity> findByLogin(String login);

    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByTelephone(String telephone);

    @Override
    <S extends UserEntity> List<S> findAll(Example<S> example);

    @Override
    <S extends UserEntity> S save(S entity);

    @Override
    Page<UserEntity> findAll(Pageable pageable);
    Page<UserEntity> findByLoginContainingIgnoreCase(String login, Pageable pageable);

    Page<UserEntity> findAllByOrderByIdAsc(Pageable pageable);

    Page<UserEntity> findAllByOrderByNameAsc(Pageable pageable);

    Page<UserEntity> findAllByOrderByEmailAsc(Pageable pageable);

    Page<UserEntity> findAllByOrderByLoginAsc(Pageable pageable);
    Long countBy();

    Long countByLoginContainingIgnoreCase(String login);

}
