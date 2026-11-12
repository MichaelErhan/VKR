package com.example.autoservice.repository;

import com.example.autoservice.model.RepairBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairBookingRepository extends JpaRepository<RepairBooking, Long> {
    @Query("SELECT DISTINCT p FROM RepairBooking p "
            + "JOIN p.author a "
            + "LEFT JOIN p.mechanic m "
            + "LEFT JOIN p.services s WHERE "
            + "(COALESCE(:title, '') = '' OR LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND "
            + "(COALESCE(:publicationDate, '') = '' OR LOWER(CAST(p.publicationDate AS string)) LIKE LOWER(CONCAT('%', :publicationDate, '%'))) AND "
            + "(COALESCE(:author, '') = '' OR LOWER(a.userName) LIKE LOWER(CONCAT('%', :author, '%'))) AND "
            + "(COALESCE(:serviceName, '') = '' OR LOWER(s.name) LIKE LOWER(CONCAT('%', :serviceName, '%'))) AND "
            + "(COALESCE(:mechanicName, '') = '' OR LOWER(m.fullName) LIKE LOWER(CONCAT('%', :mechanicName, '%')))")
    List<RepairBooking> searchByQuery(@Param("title") String title,
                                      @Param("publicationDate") String publicationDate,
                                      @Param("author") String author,
                                      @Param("serviceName") String serviceName,
                                      @Param("mechanicName") String mechanicName);
}