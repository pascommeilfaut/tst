package com.iongroup.data.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByLogin(String login);

    @Query("select u from UserEntity u join fetch u.type")
    Page<UserEntity> findAllFetched(Pageable pageable);

    @Query("select u from UserEntity u " +
            "join fetch u.type " +
            "where lower(u.name) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(u.email) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(u.login) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(u.telephone) like lower(concat('%', :searchTerm, '%'))")
    Page<UserEntity> search(@Param("searchTerm") String searchTerm, Pageable pageable);
}
