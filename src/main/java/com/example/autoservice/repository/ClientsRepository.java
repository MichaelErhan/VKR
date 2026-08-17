package com.example.autoservice.repository;

import com.example.autoservice.model.Clients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientsRepository extends JpaRepository<Clients, Long> {
    @Query("SELECT p FROM Clients p WHERE "
            + "(COALESCE(:fullName, '') = '' OR LOWER(p.fullName) LIKE LOWER(CONCAT('%', :fullName, '%'))) AND "
            + "(COALESCE(:carBrandAndPlate, '') = '' OR LOWER(CAST(p.carBrandAndPlate AS string)) LIKE LOWER(CONCAT('%', :carBrandAndPlate, '%'))) AND "
            + "(COALESCE(:phoneNumber, '') = '' OR LOWER(p.phoneNumber) LIKE LOWER(CONCAT('%', :phoneNumber, '%')))")
    List<Clients> searchByQuery(@Param("fullName") String fullName,
                                @Param("carBrandAndPlate") String carBrandAndPlate,
                                @Param("phoneNumber") String phoneNumber);
}