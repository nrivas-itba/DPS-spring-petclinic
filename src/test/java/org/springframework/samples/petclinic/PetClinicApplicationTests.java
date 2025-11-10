package org.springframework.samples.petclinic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.PetClinicApplication;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PetClinicApplicationTests {

	@Test
	void contextLoads() {
		// Test to ensure the application context loads successfully
		assertNotNull(PetClinicApplication.class);
	}
}
