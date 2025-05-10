package com.example.inventorygenius.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.inventorygenius.entity.Bom;
import com.example.inventorygenius.entity.Client;
import com.example.inventorygenius.repository.ClientRepository;

@Service
public class ClientService {
    
	@Autowired
	ClientRepository clientrepo;
	
	public Client getUserDetails(String email) {
		return clientrepo.findByEmail(email);
	}

	public Client updateUserDetails(Client client) {
		// TODO Auto-generated method stub
		
		Client updatedUser = clientrepo.findByEmail(client.getEmail());
		System.out.println("in client service -----"+updatedUser);
                //.orElseThrow(() -> new NoSuchElementException("User not found with Email id: " + client.getEmail()));
		if(updatedUser==null) {
			clientrepo.save(client);
		}else {
		updatedUser.setCompanyName(client.getCompanyName());
		updatedUser.setFirstName(client.getFirstName());
		updatedUser.setLastName(client.getLastName());
		updatedUser.setPhone(client.getPhone());
		clientrepo.save(updatedUser);
		}
		return updatedUser;
	}
}
