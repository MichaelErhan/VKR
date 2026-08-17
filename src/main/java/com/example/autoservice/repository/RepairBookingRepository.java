package com.example.autoservice.repository;

import com.example.autoservice.model.RepairBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairBookingRepository extends JpaRepository<RepairBooking, Long> {
    @Query("SELECT p FROM RepairBooking p WHERE "
            + "(COALESCE(:title, '') = '' OR LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND "
            + "(COALESCE(:publicationDate, '') = '' OR LOWER(CAST(p.publicationDate AS string)) LIKE LOWER(CONCAT('%', :publicationDate, '%'))) AND "
            + "(COALESCE(:author, '') = '' OR LOWER(p.author.userName) LIKE LOWER(CONCAT('%', :author, '%'))) AND "
            + "(COALESCE(:content, '') = '' OR LOWER(p.content) LIKE LOWER(CONCAT('%', :content, '%'))) AND "
            + "(COALESCE(:mechanicName, '') = '' OR LOWER(p.mechanicName) LIKE LOWER(CONCAT('%', :mechanicName, '%')))")
    List<RepairBooking> searchByQuery(@Param("title") String title,
                                      @Param("publicationDate") String publicationDate,
                                      @Param("author") String author,
                                      @Param("content") String content,
                                      @Param("mechanicName") String mechanicName);
}