package com.example.autoservice.repository;

import com.example.autoservice.model.PartsDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartsDeliveryRepository extends JpaRepository<PartsDelivery, Long> {
    @Query("SELECT p FROM PartsDelivery p WHERE "
            + "(COALESCE(:partName, '') = '' OR LOWER(p.partName) LIKE LOWER(CONCAT('%', :partName, '%'))) AND "
            + "(COALESCE(:partDescription, '') = '' OR LOWER(p.partDescription) LIKE LOWER(CONCAT('%', :partDescription, '%'))) AND "
            + "(COALESCE(:supplierCity, '') = '' OR LOWER(p.supplierCity) LIKE LOWER(CONCAT('%', :supplierCity, '%'))) AND "
            + "(COALESCE(:shipmentDate, '') = '' OR LOWER(CAST(p.shipmentDate AS string)) LIKE LOWER(CONCAT('%', :shipmentDate, '%'))) AND "
            + "(COALESCE(:deliveryCity, '') = '' OR LOWER(p.deliveryCity) LIKE LOWER(CONCAT('%', :deliveryCity, '%'))) AND "
            + "(COALESCE(:deliveryDate, '') = '' OR LOWER(CAST(p.deliveryDate AS string)) LIKE LOWER(CONCAT('%', :deliveryDate, '%')))")
    List<PartsDelivery> searchByQuery(@Param("partName") String partName,
                                      @Param("partDescription") String partDescription,
                                      @Param("supplierCity") String supplierCity,
                                      @Param("shipmentDate") String shipmentDate,
                                      @Param("deliveryCity") String deliveryCity,
                                      @Param("deliveryDate") String deliveryDate);

    List<PartsDelivery> findAllByOrderByDeliveryDateAsc();
    List<PartsDelivery> findAllByOrderByDeliveryDateDesc();
}