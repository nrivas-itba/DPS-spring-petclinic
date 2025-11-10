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
package org.springframework.samples.petclinic.infrastructure.persistence.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.domain.model.Pet;
import org.springframework.samples.petclinic.domain.model.PetType;
import org.springframework.samples.petclinic.domain.model.Visit;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for PetMapper.
 *
 * @author Wick Dynex
 */
class PetMapperTests {

	private PetMapper petMapper;
	private PetTypeMapper petTypeMapper;
	private VisitMapper visitMapper;

	@BeforeEach
	void setUp() {
		petTypeMapper = new PetTypeMapper();
		visitMapper = new VisitMapper();
		petMapper = new PetMapper(petTypeMapper, visitMapper);
	}

	@Test
	void testToJpa() {
		// Given
		Pet domainPet = new Pet();
		domainPet.setId(1);
		domainPet.setName("Leo");
		domainPet.setBirthDate(LocalDate.of(2020, 9, 7));

		PetType petType = new PetType();
		petType.setId(1);
		petType.setName("cat");
		domainPet.setType(petType);

		Visit visit = new Visit();
		visit.setId(1);
		visit.setDate(LocalDate.of(2025, 11, 10));
		visit.setDescription("Rabies shot");
		domainPet.addVisit(visit);

		// When
		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet jpaPet = petMapper.toJpa(domainPet);

		// Then
		assertThat(jpaPet).isNotNull();
		assertThat(jpaPet.getId()).isEqualTo(1);
		assertThat(jpaPet.getName()).isEqualTo("Leo");
		assertThat(jpaPet.getBirthDate()).isEqualTo(LocalDate.of(2020, 9, 7));
		assertThat(jpaPet.getType()).isNotNull();
		assertThat(jpaPet.getType().getName()).isEqualTo("cat");
		assertThat(jpaPet.getVisits()).hasSize(1);
		assertThat(jpaPet.getVisits().iterator().next().getDescription()).isEqualTo("Rabies shot");
	}

	@Test
	void testToJpaWithNoVisits() {
		// Given
		Pet domainPet = new Pet();
		domainPet.setId(2);
		domainPet.setName("Basil");
		domainPet.setBirthDate(LocalDate.of(2022, 8, 6));

		PetType petType = new PetType();
		petType.setId(2);
		petType.setName("hamster");
		domainPet.setType(petType);

		// When
		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet jpaPet = petMapper.toJpa(domainPet);

		// Then
		assertThat(jpaPet).isNotNull();
		assertThat(jpaPet.getId()).isEqualTo(2);
		assertThat(jpaPet.getName()).isEqualTo("Basil");
		assertThat(jpaPet.getBirthDate()).isEqualTo(LocalDate.of(2022, 8, 6));
		assertThat(jpaPet.getType().getName()).isEqualTo("hamster");
		assertThat(jpaPet.getVisits()).isEmpty();
	}

	@Test
	void testToJpaWithMultipleVisits() {
		// Given
		Pet domainPet = new Pet();
		domainPet.setId(3);
		domainPet.setName("Max");
		domainPet.setBirthDate(LocalDate.of(2018, 5, 12));

		PetType petType = new PetType();
		petType.setId(3);
		petType.setName("dog");
		domainPet.setType(petType);

		Visit visit1 = new Visit();
		visit1.setId(1);
		visit1.setDate(LocalDate.of(2025, 10, 1));
		visit1.setDescription("Checkup");
		domainPet.addVisit(visit1);

		Visit visit2 = new Visit();
		visit2.setId(2);
		visit2.setDate(LocalDate.of(2025, 11, 1));
		visit2.setDescription("Neutered");
		domainPet.addVisit(visit2);

		// When
		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet jpaPet = petMapper.toJpa(domainPet);

		// Then
		assertThat(jpaPet).isNotNull();
		assertThat(jpaPet.getId()).isEqualTo(3);
		assertThat(jpaPet.getName()).isEqualTo("Max");
		assertThat(jpaPet.getVisits()).hasSize(2);
		assertThat(jpaPet.getVisits()).extracting("description").containsExactlyInAnyOrder("Checkup", "Neutered");
	}

	@Test
	void testToDomain() {
		// Given
		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet jpaPet = 
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet();
		jpaPet.setId(4);
		jpaPet.setName("Rosy");
		jpaPet.setBirthDate(LocalDate.of(2021, 4, 17));

		org.springframework.samples.petclinic.infrastructure.persistence.entity.PetType jpaPetType = 
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.PetType();
		jpaPetType.setId(4);
		jpaPetType.setName("bird");
		jpaPet.setType(jpaPetType);

		// When
		Pet domainPet = petMapper.toDomain(jpaPet);

		// Then
		assertThat(domainPet).isNotNull();
		assertThat(domainPet.getId()).isEqualTo(4);
		assertThat(domainPet.getName()).isEqualTo("Rosy");
		assertThat(domainPet.getBirthDate()).isEqualTo(LocalDate.of(2021, 4, 17));
		assertThat(domainPet.getType()).isNotNull();
		assertThat(domainPet.getType().getName()).isEqualTo("bird");
	}

	@Test
	void testToJpaWithNullBirthDate() {
		// Given
		Pet domainPet = new Pet();
		domainPet.setId(5);
		domainPet.setName("Lucky");
		domainPet.setBirthDate(null);

		PetType petType = new PetType();
		petType.setId(5);
		petType.setName("lizard");
		domainPet.setType(petType);

		// When
		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet jpaPet = petMapper.toJpa(domainPet);

		// Then
		assertThat(jpaPet).isNotNull();
		assertThat(jpaPet.getName()).isEqualTo("Lucky");
		assertThat(jpaPet.getBirthDate()).isNull();
	}

}

