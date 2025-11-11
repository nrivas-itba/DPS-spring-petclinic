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
package org.springframework.samples.petclinic.infrastructure.persistence.jpa.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.domain.entity.Vet;
import org.springframework.samples.petclinic.infrastructure.persistence.mapper.VetMapper;
import org.springframework.samples.petclinic.infrastructure.persistence.jpa.VetJpaRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for VetRepositoryImpl.
 *
 * @author Wick Dynex
 */
@ExtendWith(MockitoExtension.class)
class VetRepositoryImplTests {

	@Mock
	private VetJpaRepository jpaRepository;

	@Mock
	private VetMapper mapper;

	private VetRepositoryImpl repository;

	@BeforeEach
	void setUp() {
		repository = new VetRepositoryImpl(jpaRepository, mapper);
	}

	@Test
	void testFindAllPageable() {
		// Given
		org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Vet jpaVet1 =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Vet();
		jpaVet1.setId(1);
		jpaVet1.setFirstName("James");
		jpaVet1.setLastName("Carter");

		org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Vet jpaVet2 =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Vet();
		jpaVet2.setId(2);
		jpaVet2.setFirstName("Helen");
		jpaVet2.setLastName("Leary");

		List<org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Vet> jpaVets =
			Arrays.asList(jpaVet1, jpaVet2);
		Pageable pageable = PageRequest.of(0, 2);
		Page<org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Vet> jpaPage =
			new PageImpl<>(jpaVets, pageable, 5);

		Vet domainVet1 = new Vet();
		domainVet1.setId(1);
		domainVet1.setFirstName("James");
		domainVet1.setLastName("Carter");

		Vet domainVet2 = new Vet();
		domainVet2.setId(2);
		domainVet2.setFirstName("Helen");
		domainVet2.setLastName("Leary");

		when(jpaRepository.findAll(any(Pageable.class))).thenReturn(jpaPage);
		when(mapper.toDomain(jpaVet1)).thenReturn(domainVet1);
		when(mapper.toDomain(jpaVet2)).thenReturn(domainVet2);

		// When
		Page<Vet> result = repository.findAll(pageable);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getContent()).hasSize(2);
		assertThat(result.getTotalElements()).isEqualTo(5);
		assertThat(result.getNumber()).isEqualTo(0);
		assertThat(result.getSize()).isEqualTo(2);

		List<Vet> vets = result.getContent();
		assertThat(vets.get(0).getFirstName()).isEqualTo("James");
		assertThat(vets.get(0).getLastName()).isEqualTo("Carter");
		assertThat(vets.get(1).getFirstName()).isEqualTo("Helen");
		assertThat(vets.get(1).getLastName()).isEqualTo("Leary");
	}

	@Test
	void testFindAllPageableWhenEmpty() {
		// Given
		Pageable pageable = PageRequest.of(0, 10);
		Page<org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Vet> emptyPage =
			new PageImpl<>(Arrays.asList(), pageable, 0);

		when(jpaRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

		// When
		Page<Vet> result = repository.findAll(pageable);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getContent()).isEmpty();
		assertThat(result.getTotalElements()).isEqualTo(0);
	}

	@Test
	void testFindAllPageableWithDifferentPageSizes() {
		// Given - Page 1 with size 3
		org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Vet jpaVet3 =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Vet();
		jpaVet3.setId(3);
		jpaVet3.setFirstName("Linda");
		jpaVet3.setLastName("Douglas");

		org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Vet jpaVet4 =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Vet();
		jpaVet4.setId(4);
		jpaVet4.setFirstName("Rafael");
		jpaVet4.setLastName("Ortega");

		List<org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Vet> jpaVets =
			Arrays.asList(jpaVet3, jpaVet4);
		Pageable pageable = PageRequest.of(1, 2);
		Page<org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Vet> jpaPage =
			new PageImpl<>(jpaVets, pageable, 6);

		Vet domainVet3 = new Vet();
		domainVet3.setId(3);
		domainVet3.setFirstName("Linda");
		domainVet3.setLastName("Douglas");

		Vet domainVet4 = new Vet();
		domainVet4.setId(4);
		domainVet4.setFirstName("Rafael");
		domainVet4.setLastName("Ortega");

		when(jpaRepository.findAll(any(Pageable.class))).thenReturn(jpaPage);
		when(mapper.toDomain(jpaVet3)).thenReturn(domainVet3);
		when(mapper.toDomain(jpaVet4)).thenReturn(domainVet4);

		// When
		Page<Vet> result = repository.findAll(pageable);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getContent()).hasSize(2);
		assertThat(result.getTotalElements()).isEqualTo(6);
		assertThat(result.getNumber()).isEqualTo(1); // Second page (0-indexed)
		assertThat(result.getSize()).isEqualTo(2);
	}

}

