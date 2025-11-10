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
package org.springframework.samples.petclinic.infrastructure.persistence.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.domain.model.PetType;
import org.springframework.samples.petclinic.infrastructure.persistence.mapper.PetTypeMapper;
import org.springframework.samples.petclinic.infrastructure.persistence.repository.PetTypeJpaRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit tests for PetTypeRepositoryImpl.
 *
 * @author Wick Dynex
 */
@ExtendWith(MockitoExtension.class)
class PetTypeRepositoryImplTests {

	@Mock
	private PetTypeJpaRepository jpaRepository;

	@Mock
	private PetTypeMapper mapper;

	private PetTypeRepositoryImpl repository;

	@BeforeEach
	void setUp() {
		repository = new PetTypeRepositoryImpl(jpaRepository, mapper);
	}

	@Test
	void testFindAll() {
		// Given
		org.springframework.samples.petclinic.infrastructure.persistence.entity.PetType jpaCat =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.PetType();
		jpaCat.setId(1);
		jpaCat.setName("cat");

		org.springframework.samples.petclinic.infrastructure.persistence.entity.PetType jpaDog =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.PetType();
		jpaDog.setId(2);
		jpaDog.setName("dog");

		org.springframework.samples.petclinic.infrastructure.persistence.entity.PetType jpaHamster =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.PetType();
		jpaHamster.setId(3);
		jpaHamster.setName("hamster");

		List<org.springframework.samples.petclinic.infrastructure.persistence.entity.PetType> jpaPetTypes =
			Arrays.asList(jpaCat, jpaDog, jpaHamster);

		PetType domainCat = new PetType();
		domainCat.setId(1);
		domainCat.setName("cat");

		PetType domainDog = new PetType();
		domainDog.setId(2);
		domainDog.setName("dog");

		PetType domainHamster = new PetType();
		domainHamster.setId(3);
		domainHamster.setName("hamster");

		when(jpaRepository.findPetTypes()).thenReturn(jpaPetTypes);
		when(mapper.toDomain(jpaCat)).thenReturn(domainCat);
		when(mapper.toDomain(jpaDog)).thenReturn(domainDog);
		when(mapper.toDomain(jpaHamster)).thenReturn(domainHamster);

		// When
		List<PetType> result = repository.findAll();

		// Then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(3);
		assertThat(result).containsExactly(domainCat, domainDog, domainHamster);
	}

	@Test
	void testFindAllWhenEmpty() {
		// Given
		when(jpaRepository.findPetTypes()).thenReturn(Arrays.asList());

		// When
		List<PetType> result = repository.findAll();

		// Then
		assertThat(result).isNotNull();
		assertThat(result).isEmpty();
	}

}

