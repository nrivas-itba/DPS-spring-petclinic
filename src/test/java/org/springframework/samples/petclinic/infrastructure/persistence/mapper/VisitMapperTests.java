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
import org.springframework.samples.petclinic.domain.model.Visit;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for VisitMapper.
 *
 * @author Wick Dynex
 */
class VisitMapperTests {

	private VisitMapper visitMapper;

	@BeforeEach
	void setUp() {
		visitMapper = new VisitMapper();
	}

	@Test
	void testToDomain() {
		// Given
		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Visit jpaVisit = 
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Visit();
		jpaVisit.setId(1);
		jpaVisit.setDate(LocalDate.of(2025, 11, 10));
		jpaVisit.setDescription("Rabies shot");

		// When
		Visit domainVisit = visitMapper.toDomain(jpaVisit);

		// Then
		assertThat(domainVisit).isNotNull();
		assertThat(domainVisit.getId()).isEqualTo(1);
		assertThat(domainVisit.getDate()).isEqualTo(LocalDate.of(2025, 11, 10));
		assertThat(domainVisit.getDescription()).isEqualTo("Rabies shot");
	}

	@Test
	void testToJpa() {
		// Given
		Visit domainVisit = new Visit();
		domainVisit.setId(2);
		domainVisit.setDate(LocalDate.of(2025, 12, 1));
		domainVisit.setDescription("Annual checkup");

		// When
		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Visit jpaVisit = visitMapper.toJpa(domainVisit);

		// Then
		assertThat(jpaVisit).isNotNull();
		assertThat(jpaVisit.getId()).isEqualTo(2);
		assertThat(jpaVisit.getDate()).isEqualTo(LocalDate.of(2025, 12, 1));
		assertThat(jpaVisit.getDescription()).isEqualTo("Annual checkup");
	}

	@Test
	void testToDomainWithNullDescription() {
		// Given
		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Visit jpaVisit = 
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Visit();
		jpaVisit.setId(3);
		jpaVisit.setDate(LocalDate.of(2025, 10, 15));
		jpaVisit.setDescription(null);

		// When
		Visit domainVisit = visitMapper.toDomain(jpaVisit);

		// Then
		assertThat(domainVisit).isNotNull();
		assertThat(domainVisit.getId()).isEqualTo(3);
		assertThat(domainVisit.getDescription()).isNull();
	}

	@Test
	void testToJpaWithNullDescription() {
		// Given
		Visit domainVisit = new Visit();
		domainVisit.setId(4);
		domainVisit.setDate(LocalDate.of(2025, 9, 20));
		domainVisit.setDescription(null);

		// When
		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Visit jpaVisit = visitMapper.toJpa(domainVisit);

		// Then
		assertThat(jpaVisit).isNotNull();
		assertThat(jpaVisit.getId()).isEqualTo(4);
		assertThat(jpaVisit.getDescription()).isNull();
	}

}

