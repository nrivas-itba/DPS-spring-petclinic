/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.application.usecases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.domain.model.Owner;
import org.springframework.samples.petclinic.domain.model.Pet;
import org.springframework.samples.petclinic.domain.model.PetType;
import org.springframework.samples.petclinic.domain.repository.OwnerRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for PetServiceImpl.
 *
 * @author Wick Dynex
 */
@ExtendWith(MockitoExtension.class)
class PetUseCaseImplTests {

	@Mock
	private OwnerRepository ownerRepository;

	private PetUseCaseImpl petService;

	@BeforeEach
	void setUp() {
		petService = new PetUseCaseImpl(ownerRepository);
	}

	@Test
	void testCreatePet() {
		// Given
		Owner owner = new Owner();
		owner.setId(1);
		owner.setFirstName("George");
		owner.setLastName("Franklin");

		Pet pet = new Pet();
		pet.setName("Leo");
		pet.setBirthDate(LocalDate.of(2020, 9, 7));

		PetType petType = new PetType();
		petType.setId(1);
		petType.setName("cat");
		pet.setType(petType);

		when(ownerRepository.save(any(Owner.class))).thenReturn(owner);

		// When
		Owner result = petService.createPet(owner, pet);

		// Then
		assertThat(result).isNotNull();
		assertThat(owner.getPets()).hasSize(1);
		assertThat(owner.getPets().get(0)).isEqualTo(pet);
	}

	@Test
	void testUpdatePet() {
		// Given
		Owner owner = new Owner();
		owner.setId(1);
		owner.setFirstName("George");
		owner.setLastName("Franklin");

		Pet existingPet = new Pet();
		existingPet.setName("Leo");
		existingPet.setBirthDate(LocalDate.of(2020, 9, 7));

		PetType oldType = new PetType();
		oldType.setId(1);
		oldType.setName("cat");
		existingPet.setType(oldType);

		owner.addPet(existingPet);
		existingPet.setId(1);

		Pet updatedPet = new Pet();
		updatedPet.setId(1);
		updatedPet.setName("Leo Updated");
		updatedPet.setBirthDate(LocalDate.of(2020, 9, 10));

		PetType newType = new PetType();
		newType.setId(2);
		newType.setName("dog");
		updatedPet.setType(newType);

		when(ownerRepository.save(any(Owner.class))).thenReturn(owner);

		// When
		Owner result = petService.updatePet(owner, updatedPet);

		// Then
		assertThat(result).isNotNull();
		assertThat(owner.getPets()).hasSize(1);
		Pet pet = owner.getPets().get(0);
		assertThat(pet.getName()).isEqualTo("Leo Updated");
		assertThat(pet.getBirthDate()).isEqualTo(LocalDate.of(2020, 9, 10));
		assertThat(pet.getType().getName()).isEqualTo("dog");
	}

	@Test
	void testUpdatePetWhenPetDoesNotExist() {
		// Given
		Owner owner = new Owner();
		owner.setId(1);
		owner.setFirstName("George");
		owner.setLastName("Franklin");

		Pet newPet = new Pet();
		newPet.setName("New Pet");
		newPet.setBirthDate(LocalDate.of(2021, 1, 1));

		PetType petType = new PetType();
		petType.setId(3);
		petType.setName("bird");
		newPet.setType(petType);

		when(ownerRepository.save(any(Owner.class))).thenReturn(owner);

		// When
		Owner result = petService.updatePet(owner, newPet);
		newPet.setId(99);

		// Then
		assertThat(result).isNotNull();
		assertThat(owner.getPets()).hasSize(1);
		assertThat(owner.getPets().get(0)).isEqualTo(newPet);
	}

	@Test
	void testIsPetNameUniqueForOwner_WhenUnique() {
		// Given
		Owner owner = new Owner();
		owner.setId(1);

		Pet existingPet = new Pet();
		existingPet.setId(1);
		existingPet.setName("Leo");
		owner.addPet(existingPet);

		// When
		boolean result = petService.isPetNameUniqueForOwner(owner, "Max", null);

		// Then
		assertThat(result).isTrue();
	}

	@Test
	void testIsPetNameUniqueForOwner_WhenNotUnique() {
		// Given
		Owner owner = new Owner();
		owner.setId(1);

		Pet existingPet = new Pet();
		existingPet.setName("Leo");
		owner.addPet(existingPet);
		existingPet.setId(1);

		// When
		boolean result = petService.isPetNameUniqueForOwner(owner, "Leo", 2);

		// Then
		assertThat(result).isFalse();
	}

	@Test
	void testIsPetNameUniqueForOwner_WhenUpdatingSamePet() {
		// Given
		Owner owner = new Owner();
		owner.setId(1);

		Pet existingPet = new Pet();
		existingPet.setName("Leo");
		owner.addPet(existingPet);
		existingPet.setId(1);

		// When
		boolean result = petService.isPetNameUniqueForOwner(owner, "Leo", 1);

		// Then
		assertThat(result).isTrue();
	}

	@Test
	void testIsPetNameUniqueForOwner_WhenEmptyName() {
		// Given
		Owner owner = new Owner();
		owner.setId(1);

		// When
		boolean result = petService.isPetNameUniqueForOwner(owner, "", null);

		// Then
		assertThat(result).isTrue();
	}

	@Test
	void testIsPetNameUniqueForOwner_WhenNullName() {
		// Given
		Owner owner = new Owner();
		owner.setId(1);

		// When
		boolean result = petService.isPetNameUniqueForOwner(owner, null, null);

		// Then
		assertThat(result).isTrue();
	}

	@Test
	void testIsBirthDateValid_WhenPastDate() {
		// Given
		LocalDate pastDate = LocalDate.of(2020, 1, 1);

		// When
		boolean result = petService.isBirthDateValid(pastDate);

		// Then
		assertThat(result).isTrue();
	}

	@Test
	void testIsBirthDateValid_WhenTodayDate() {
		// Given
		LocalDate today = LocalDate.now();

		// When
		boolean result = petService.isBirthDateValid(today);

		// Then
		assertThat(result).isTrue();
	}

	@Test
	void testIsBirthDateValid_WhenFutureDate() {
		// Given
		LocalDate futureDate = LocalDate.now().plusDays(1);

		// When
		boolean result = petService.isBirthDateValid(futureDate);

		// Then
		assertThat(result).isFalse();
	}

	@Test
	void testIsBirthDateValid_WhenNull() {
		// When
		boolean result = petService.isBirthDateValid(null);

		// Then
		assertThat(result).isTrue();
	}

	@Test
	void testUpdatePetProperties_AllPropertiesUpdated() {
		// Given - Testing updatePetProperties indirectly through updatePet
		Owner owner = new Owner();
		owner.setId(1);

		Pet existingPet = new Pet();

		existingPet.setName("OldName");
		existingPet.setBirthDate(LocalDate.of(2020, 1, 1));

		PetType oldType = new PetType();
		oldType.setId(1);
		oldType.setName("cat");
		existingPet.setType(oldType);

		owner.addPet(existingPet);
		existingPet.setId(10);

		Pet updatedPet = new Pet();
		updatedPet.setId(10);
		updatedPet.setName("NewName");
		updatedPet.setBirthDate(LocalDate.of(2021, 12, 31));

		PetType newType = new PetType();
		newType.setId(2);
		newType.setName("dog");
		updatedPet.setType(newType);

		when(ownerRepository.save(any(Owner.class))).thenReturn(owner);

		// When
		petService.updatePet(owner, updatedPet);

		// Then - Verify all properties were updated (updatePetProperties was called)
		Pet resultPet = owner.getPets().get(0);
		assertThat(resultPet.getId()).isEqualTo(10); // ID stays the same
		assertThat(resultPet.getName()).isEqualTo("NewName"); // Name updated
		assertThat(resultPet.getBirthDate()).isEqualTo(LocalDate.of(2021, 12, 31)); // BirthDate updated
		assertThat(resultPet.getType()).isEqualTo(newType); // Type updated
		assertThat(resultPet.getType().getName()).isEqualTo("dog");
	}

	@Test
	void testUpdatePetProperties_OnlyNameUpdated() {
		// Given - Testing partial property update through updatePet
		Owner owner = new Owner();

		Pet existingPet = new Pet();
		existingPet.setName("OldName");
		existingPet.setBirthDate(LocalDate.of(2020, 5, 15));

		PetType petType = new PetType();
		petType.setId(3);
		petType.setName("bird");
		existingPet.setType(petType);

		owner.addPet(existingPet);
		existingPet.setId(15);

		Pet updatedPet = new Pet();
		updatedPet.setId(15);
		updatedPet.setName("UpdatedName");
		updatedPet.setBirthDate(LocalDate.of(2020, 5, 15)); // Same date
		updatedPet.setType(petType); // Same type

		owner.setId(1);

		when(ownerRepository.save(any(Owner.class))).thenReturn(owner);

		// When
		petService.updatePet(owner, updatedPet);

		// Then - Only name should change
		Pet resultPet = owner.getPets().get(0);
		assertThat(resultPet.getName()).isEqualTo("UpdatedName");
		assertThat(resultPet.getBirthDate()).isEqualTo(LocalDate.of(2020, 5, 15));
		assertThat(resultPet.getType().getName()).isEqualTo("bird");
	}

}

