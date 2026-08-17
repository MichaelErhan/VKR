package com.example.autoservice.service;

import com.example.autoservice.model.PartsDelivery;
import com.example.autoservice.repository.PartsDeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PartsDeliveryService {

    private final PartsDeliveryRepository partsDeliveryRepository;

    @Autowired
    public PartsDeliveryService(PartsDeliveryRepository partsDeliveryRepository) {
        this.partsDeliveryRepository = partsDeliveryRepository;
    }

    // Сохраняем доставку запчастей в базе данных
    public void savePartsDelivery(PartsDelivery partsDelivery) {
        partsDeliveryRepository.save(partsDelivery);
    }

    // Находим доставку запчастей по ID
    public Optional<PartsDelivery> findPartsDeliveryById(Long id) {
        return partsDeliveryRepository.findById(id);
    }

    // Находим все доставки, отсортированные по дате
    public List<PartsDelivery> findAllPartsDeliveriesSortedByDate() {
        return partsDeliveryRepository.findAllByOrderByDeliveryDateAsc();
    }

    public List<PartsDelivery> findAllPartsDeliveriesSortedByDateDesc() {
        return partsDeliveryRepository.findAllByOrderByDeliveryDateDesc();
    }

    // Удалить доставку запчастей из таблицы по ID
    public void deletePartsDelivery(Long id) {
        partsDeliveryRepository.deleteById(id);
    }

    // Поиск доставки запчастей
    public List<PartsDelivery> searchPartsDeliveries(String partName, String partDescription, String supplierCity, String shipmentDate, String deliveryCity, String deliveryDate) {
        return partsDeliveryRepository.searchByQuery(partName, partDescription, supplierCity, shipmentDate, deliveryCity, deliveryDate);
    }

    // Счётчик доставок запчастей в день
    public Map<LocalDate, Long> getPartsDeliveryCountByDay() {
        List<PartsDelivery> sessions = partsDeliveryRepository.findAll();
        return sessions.stream()
                .collect(Collectors.groupingBy(
                        session -> session.getShipmentDate(),
                        Collectors.counting()
                ));
    }
}