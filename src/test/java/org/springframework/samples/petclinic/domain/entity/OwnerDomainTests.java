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
package org.springframework.samples.petclinic.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for Owner domain model.
 *
 * @author Wick Dynex
 */
class OwnerDomainTests {

	private Owner owner;

	@BeforeEach
	void setUp() {
		owner = new Owner();
		owner.setId(1);
		owner.setFirstName("George");
		owner.setLastName("Franklin");
	}

	@Test
	void testGetPetByNameIgnoreNew() {
		// Given
		Pet savedPet = new Pet();
		savedPet.setName("Leo");
		savedPet.setBirthDate(LocalDate.of(2020, 9, 7));
		owner.addPet(savedPet);
		savedPet.setId(1);

		// When
		Pet result = owner.getPet("Leo", true);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getName()).isEqualTo("Leo");
	}

	@Test
	void testGetPetByNameCaseInsensitive() {
		// Given
		Pet savedPet = new Pet();
		savedPet.setName("Leo");
		savedPet.setBirthDate(LocalDate.of(2020, 9, 7));
		owner.addPet(savedPet);
		savedPet.setId(1);

		// When
		Pet result = owner.getPet("LEO", true);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getName()).isEqualTo("Leo");
	}

	@Test
	void testGetPetByNameNotFound() {
		// Given
		Pet savedPet = new Pet();
		savedPet.setId(1);
		savedPet.setName("Leo");
		savedPet.setBirthDate(LocalDate.of(2020, 9, 7));
		owner.addPet(savedPet);

		// When
		Pet result = owner.getPet("Max", true);

		// Then
		assertThat(result).isNull();
	}

	@Test
	void testGetPetByNameIncludeNew() {
		// Given
		Pet newPet = new Pet();
		newPet.setName("Basil");
		newPet.setBirthDate(LocalDate.of(2022, 8, 6));
		owner.addPet(newPet);

		// When - ignoreNew = false, should find new pets
		Pet result = owner.getPet("Basil", false);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getName()).isEqualTo("Basil");
	}

	@Test
	void testGetPetByNameIgnoreNewPet() {
		// Given
		Pet newPet = new Pet();
		newPet.setName("Basil");
		newPet.setBirthDate(LocalDate.of(2022, 8, 6));
		owner.addPet(newPet);

		// When - ignoreNew = true, should not find new pets
		Pet result = owner.getPet("Basil", true);

		// Then
		assertThat(result).isNull();
	}

	@Test
	void testGetPetByNameWithMultiplePets() {
		// Given
		Pet pet1 = new Pet();
		pet1.setName("Leo");
		owner.addPet(pet1);
		pet1.setId(1);

		Pet pet2 = new Pet();
		pet2.setName("Max");
		owner.addPet(pet2);
		pet2.setId(2);

		Pet pet3 = new Pet();
		pet3.setName("Basil");
		owner.addPet(pet3);
		pet3.setId(3);

		// When
		Pet result = owner.getPet("Max", true);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getName()).isEqualTo("Max");
		assertThat(result.getId()).isEqualTo(2);
	}

	@Test
	void testGetPetByNameWithNullName() {
		// Given
		Pet pet = new Pet();
		pet.setName(null);
		owner.addPet(pet);
		pet.setId(1);

		// When
		Pet result = owner.getPet("Leo", true);

		// Then
		assertThat(result).isNull();
	}

	@Test
	void testGetPetByNameWhenNoPets() {
		// When
		Pet result = owner.getPet("Leo", true);

		// Then
		assertThat(result).isNull();
	}

	@Test
	void testGetPetByNameMixedSavedAndNew() {
		// Given
		Pet savedPet = new Pet();
		savedPet.setName("Leo");
		owner.addPet(savedPet);
		savedPet.setId(1);

		Pet newPet = new Pet();
		newPet.setName("Basil");
		owner.addPet(newPet);

		// When - ignoreNew = true
		Pet result1 = owner.getPet("Leo", true);
		Pet result2 = owner.getPet("Basil", true);

		// Then
		assertThat(result1).isNotNull();
		assertThat(result1.getName()).isEqualTo("Leo");
		assertThat(result2).isNull(); // New pet should be ignored

		// When - ignoreNew = false
		Pet result3 = owner.getPet("Basil", false);

		// Then
		assertThat(result3).isNotNull();
		assertThat(result3.getName()).isEqualTo("Basil");
	}

}

