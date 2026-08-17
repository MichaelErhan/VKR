package com.example.autoservice.service;

import com.example.autoservice.model.Clients;
import com.example.autoservice.repository.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientsService {

    private final ClientsRepository clientsRepository;

    @Autowired
    public ClientsService(ClientsRepository clientsRepository) {
        this.clientsRepository = clientsRepository;
    }

    public void saveClients(Clients clients) { clientsRepository.save(clients); }

    public Optional<Clients> findClientsById(Long id) {
        return clientsRepository.findById(id);
    }

    public List<Clients> findAllClients() {
        return clientsRepository.findAll();
    }

    public void deleteClients(Long id) {
        clientsRepository.deleteById(id);
    }

    public List<Clients> searchClients(String fullName, String carBrandAndPlate, String phoneNumber) {
        return clientsRepository.searchByQuery(fullName, carBrandAndPlate, phoneNumber);
    }
}