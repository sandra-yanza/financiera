package com.quind.financiera.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quind.financiera.exception.CustomException;
import com.quind.financiera.exception.ResourceNotFoundException;
import com.quind.financiera.model.Client;
import com.quind.financiera.repository.ClientRepository;

@Service
public class ClientService {
	
	@Autowired
    private ClientRepository clientRepository;

    public Client createClient(Client client) {
    	
    	if (client.getBirthDate().isAfter(LocalDate.now().minusYears(18))) {
            throw new CustomException("El cliente debe ser mayor de edad");
        }
    	
    	if (client.getName().length() < 2) {
            throw new CustomException("La extensión del nombre del cliente no puede ser menor a dos caracteres");
        }
    	
    	if (client.getLastName().length() < 2) {
            throw new CustomException("La extensión del apellido del cliente no puede ser menor a dos caracteres");
        }
    	
        client.setCreationDate(LocalDate.now());
        client.setModificationDate(LocalDate.now());
        return clientRepository.save(client);
    }

    public Client updateClient(Long id, Client modifyClient) {
    	
        Client client = clientRepository.findById(id)
        		.orElseThrow(() -> new ResourceNotFoundException("Cliente no existe - id: " + id));
        
        client.setName(modifyClient.getName());
        client.setLastName(modifyClient.getLastName());
        client.setEmail(modifyClient.getEmail());
        client.setModificationDate(LocalDate.now());
        return clientRepository.save(client);
    }

    public void deleteClient(Long id) {
    	
        Client client = clientRepository.findById(id)
        		.orElseThrow(() -> new ResourceNotFoundException("Cliente no existe - id: " + id));
        if (!client.getAccounts().isEmpty()) {
            throw new CustomException("No se pudo eliminar el cliente. Tiene cuentas asociadas");
        }
        clientRepository.delete(client);
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id)
        		.orElseThrow(() -> new ResourceNotFoundException("Cliente no existe - id: " + id));
    }


}
