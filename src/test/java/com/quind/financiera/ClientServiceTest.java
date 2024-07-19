package com.quind.financiera;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.quind.financiera.model.Client;
import com.quind.financiera.model.Client.IdType;
import com.quind.financiera.repository.ClientRepository;
import com.quind.financiera.service.ClientService;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientServiceTest {

	@Autowired
	private ClientService clientService;

	@MockBean
	private ClientRepository clientRepository;

	@Test
	public void testCreateClient() {
		Client client = new Client();
		client.setName("Sandra");
		client.setLastName("Yanza");
		client.setEmail("syanza@gmail.com");
		client.setBirthDate(LocalDate.of(1988, 1, 1));
		client.setIdType(IdType.CC);
		client.setIdentificationNumber("34321571");

		Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(client);

		Client cl = clientService.createClient(client);
		assertEquals("Sandra", cl.getName());
	}

	@Test
	public void testUpdateClient() {
		
		Client client = new Client();
		client.setName("Isabel");
		client.setLastName("Hernandez");
		client.setEmail("sandra.yanza@example.com");
		client.setBirthDate(LocalDate.of(1988, 1, 1));
		client.setIdType(IdType.CC);
		client.setIdentificationNumber("34321511");

		Mockito.when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
		Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(client);

		client.setName("John");
		Client cli = clientService.updateClient(1L, client);
		assertEquals("John", cli.getName());
	}

}
