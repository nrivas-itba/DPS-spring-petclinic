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
import org.springframework.samples.petclinic.domain.model.Specialty;
import org.springframework.samples.petclinic.domain.model.Vet;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for VetMapper.
 *
 * @author Wick Dynex
 */
class VetMapperTests {

	private VetMapper vetMapper;
	private SpecialtyMapper specialtyMapper;

	@BeforeEach
	void setUp() {
		specialtyMapper = new SpecialtyMapper();
		vetMapper = new VetMapper(specialtyMapper);
	}

	@Test
	void testToJpa() {
		// Given
		Vet domainVet = new Vet();
		domainVet.setId(1);
		domainVet.setFirstName("James");
		domainVet.setLastName("Carter");

		Specialty specialty1 = new Specialty();
		specialty1.setId(1);
		specialty1.setName("Radiology");
		domainVet.addSpecialty(specialty1);

		Specialty specialty2 = new Specialty();
		specialty2.setId(2);
		specialty2.setName("Surgery");
		domainVet.addSpecialty(specialty2);

		// When
		org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Vet jpaVet = vetMapper.toJpa(domainVet);

		// Then
		assertThat(jpaVet).isNotNull();
		assertThat(jpaVet.getId()).isEqualTo(1);
		assertThat(jpaVet.getFirstName()).isEqualTo("James");
		assertThat(jpaVet.getLastName()).isEqualTo("Carter");
		assertThat(jpaVet.getSpecialties()).hasSize(2);
		assertThat(jpaVet.getSpecialties()).extracting("name").containsExactlyInAnyOrder("Radiology", "Surgery");
	}

	@Test
	void testToJpaWithNoSpecialties() {
		// Given
		Vet domainVet = new Vet();
		domainVet.setId(2);
		domainVet.setFirstName("Helen");
		domainVet.setLastName("Leary");

		// When
		org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Vet jpaVet = vetMapper.toJpa(domainVet);

		// Then
		assertThat(jpaVet).isNotNull();
		assertThat(jpaVet.getId()).isEqualTo(2);
		assertThat(jpaVet.getFirstName()).isEqualTo("Helen");
		assertThat(jpaVet.getLastName()).isEqualTo("Leary");
		assertThat(jpaVet.getSpecialties()).isEmpty();
	}

	@Test
	void testToDomain() {
		// Given
		org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Vet jpaVet = 
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Vet();
		jpaVet.setId(3);
		jpaVet.setFirstName("Linda");
		jpaVet.setLastName("Douglas");

		org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Specialty jpaSpecialty1 = 
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Specialty();
		jpaSpecialty1.setId(1);
		jpaSpecialty1.setName("Dentistry");
		jpaVet.addSpecialty(jpaSpecialty1);

		// When
		Vet domainVet = vetMapper.toDomain(jpaVet);

		// Then
		assertThat(domainVet).isNotNull();
		assertThat(domainVet.getId()).isEqualTo(3);
		assertThat(domainVet.getFirstName()).isEqualTo("Linda");
		assertThat(domainVet.getLastName()).isEqualTo("Douglas");
		assertThat(domainVet.getSpecialties()).hasSize(1);
		assertThat(domainVet.getSpecialties().get(0).getName()).isEqualTo("Dentistry");
	}

	@Test
	void testToJpaWithSingleSpecialty() {
		// Given
		Vet domainVet = new Vet();
		domainVet.setId(4);
		domainVet.setFirstName("Rafael");
		domainVet.setLastName("Ortega");

		Specialty specialty = new Specialty();
		specialty.setId(3);
		specialty.setName("Ophthalmology");
		domainVet.addSpecialty(specialty);

		// When
		org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Vet jpaVet = vetMapper.toJpa(domainVet);

		// Then
		assertThat(jpaVet).isNotNull();
		assertThat(jpaVet.getId()).isEqualTo(4);
		assertThat(jpaVet.getSpecialties()).hasSize(1);
		assertThat(jpaVet.getSpecialties().iterator().next().getName()).isEqualTo("Ophthalmology");
	}

}

