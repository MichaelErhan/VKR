package com.example.autoservice.service;

import com.example.autoservice.model.ServiceItem;
import com.example.autoservice.repository.ServiceItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceItemService {

    private final ServiceItemRepository serviceItemRepository;

    public ServiceItemService(ServiceItemRepository serviceItemRepository) {
        this.serviceItemRepository = serviceItemRepository;
    }

    public void saveServiceItem(ServiceItem serviceItem) {
        serviceItemRepository.save(serviceItem);
    }

    public Optional<ServiceItem> findServiceItemById(Long id) {
        return serviceItemRepository.findById(id);
    }

    public List<ServiceItem> findAllServiceItems() {
        return serviceItemRepository.findAll();
    }

    public List<ServiceItem> findServiceItemsByIds(List<Long> ids) {
        return serviceItemRepository.findAllById(ids);
    }

    public void deleteServiceItem(Long id) {
        serviceItemRepository.deleteById(id);
    }
}