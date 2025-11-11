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
import org.springframework.samples.petclinic.domain.entity.Specialty;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for SpecialtyMapper.
 *
 * @author Wick Dynex
 */
class SpecialtyMapperTests {

	private SpecialtyMapper specialtyMapper;

	@BeforeEach
	void setUp() {
		specialtyMapper = new SpecialtyMapper();
	}

	@Test
	void testToJpa() {
		// Given
		Specialty domainSpecialty = new Specialty();
		domainSpecialty.setId(1);
		domainSpecialty.setName("Radiology");

		// When
		org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Specialty jpaSpecialty = specialtyMapper.toJpa(domainSpecialty);

		// Then
		assertThat(jpaSpecialty).isNotNull();
		assertThat(jpaSpecialty.getId()).isEqualTo(1);
		assertThat(jpaSpecialty.getName()).isEqualTo("Radiology");
	}

	@Test
	void testToDomain() {
		// Given
		org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Specialty jpaSpecialty =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Specialty();
		jpaSpecialty.setId(2);
		jpaSpecialty.setName("Surgery");

		// When
		Specialty domainSpecialty = specialtyMapper.toDomain(jpaSpecialty);

		// Then
		assertThat(domainSpecialty).isNotNull();
		assertThat(domainSpecialty.getId()).isEqualTo(2);
		assertThat(domainSpecialty.getName()).isEqualTo("Surgery");
	}

	@Test
	void testToJpaWithNullName() {
		// Given
		Specialty domainSpecialty = new Specialty();
		domainSpecialty.setId(3);
		domainSpecialty.setName(null);

		// When
		org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Specialty jpaSpecialty = specialtyMapper.toJpa(domainSpecialty);

		// Then
		assertThat(jpaSpecialty).isNotNull();
		assertThat(jpaSpecialty.getId()).isEqualTo(3);
		assertThat(jpaSpecialty.getName()).isNull();
	}

}

