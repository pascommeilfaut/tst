package com.iongroup.data.pos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PosRepo extends JpaRepository<PosEntity, Integer> {

    @Query("""
            select p from PosEntity p
            join fetch p.city
            join fetch p.connectionType
            """)
    Page<PosEntity> emptySearch(Pageable pageable);

    @Query("""
            select p from PosEntity p
            join fetch p.city
            join fetch p.connectionType
            where lower(p.name) like lower(concat('%', :searchTerm, '%'))
                or lower(p.telephone) like lower(concat('%', :searchTerm, '%'))
                or lower(p.cellphone) like lower(concat('%', :searchTerm, '%'))
                or lower(p.address) like lower(concat('%', :searchTerm, '%'))
                or lower(p.model) like lower(concat('%', :searchTerm, '%'))
                or lower(p.brand) like lower(concat('%', :searchTerm, '%'))
            """)
    Page<PosEntity> search(@Param("searchTerm") String searchTerm, Pageable pageable);
}
