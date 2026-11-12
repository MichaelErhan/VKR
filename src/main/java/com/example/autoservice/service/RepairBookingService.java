package com.example.autoservice.service;

import com.example.autoservice.model.RepairBooking;
import com.example.autoservice.repository.RepairBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepairBookingService {

    private final RepairBookingRepository repairBookingRepository;

    @Autowired
    public RepairBookingService(RepairBookingRepository repairBookingRepository) {
        this.repairBookingRepository = repairBookingRepository;
    }

    public void saveRepairBooking(RepairBooking repairBooking) {
        repairBookingRepository.save(repairBooking);
    }

    public Optional<RepairBooking> findRepairBookingById(Long id) {
        return repairBookingRepository.findById(id);
    }

    public List<RepairBooking> findAllRepairBookings() {
        return repairBookingRepository.findAll();
    }

    public void deleteRepairBooking(Long id) {
        repairBookingRepository.deleteById(id);
    }

    public List<RepairBooking> searchRepairBookings(String title, String publicationDate, String author, String serviceName, String mechanicName) {
        return repairBookingRepository.searchByQuery(title, publicationDate, author, serviceName, mechanicName);
    }
}