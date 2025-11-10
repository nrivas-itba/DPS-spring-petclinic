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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.domain.model.Specialty;
import org.springframework.samples.petclinic.domain.model.Vet;
import org.springframework.samples.petclinic.domain.repository.VetRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for VetServiceImpl.
 *
 * @author Wick Dynex
 */
@ExtendWith(MockitoExtension.class)
class VetUseCaseImplTests {

	@Mock
	private VetRepository vetRepository;

	private VetUseCaseImpl vetService;

	@BeforeEach
	void setUp() {
		vetService = new VetUseCaseImpl(vetRepository);
	}

	@Test
	void testFindAllCollection() {
		// Given
		Vet vet1 = new Vet();
		vet1.setId(1);
		vet1.setFirstName("James");
		vet1.setLastName("Carter");

		Vet vet2 = new Vet();
		vet2.setId(2);
		vet2.setFirstName("Helen");
		vet2.setLastName("Leary");

		Specialty specialty = new Specialty();
		specialty.setId(1);
		specialty.setName("Radiology");
		vet2.addSpecialty(specialty);

		List<Vet> expectedVets = Arrays.asList(vet1, vet2);
		when(vetRepository.findAll()).thenReturn(expectedVets);

		// When
		Collection<Vet> result = vetService.findAll();

		// Then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(2);
		assertThat(result).containsExactly(vet1, vet2);
	}

	@Test
	void testFindAllPageable() {
		// Given
		Vet vet1 = new Vet();
		vet1.setId(1);
		vet1.setFirstName("James");
		vet1.setLastName("Carter");

		Vet vet2 = new Vet();
		vet2.setId(2);
		vet2.setFirstName("Helen");
		vet2.setLastName("Leary");

		Vet vet3 = new Vet();
		vet3.setId(3);
		vet3.setFirstName("Linda");
		vet3.setLastName("Douglas");

		List<Vet> vets = Arrays.asList(vet1, vet2);
		Pageable pageable = PageRequest.of(0, 2);
		Page<Vet> expectedPage = new PageImpl<>(vets, pageable, 3);

		when(vetRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

		// When
		Page<Vet> result = vetService.findAll(pageable);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getContent()).hasSize(2);
		assertThat(result.getTotalElements()).isEqualTo(3);
		assertThat(result.getNumber()).isEqualTo(0);
		assertThat(result.getSize()).isEqualTo(2);
	}

	@Test
	void testFindAllCollectionWhenEmpty() {
		// Given
		when(vetRepository.findAll()).thenReturn(Arrays.asList());

		// When
		Collection<Vet> result = vetService.findAll();

		// Then
		assertThat(result).isNotNull();
		assertThat(result).isEmpty();
	}

	@Test
	void testFindAllPageableWhenEmpty() {
		// Given
		Pageable pageable = PageRequest.of(0, 10);
		Page<Vet> emptyPage = new PageImpl<>(Arrays.asList(), pageable, 0);

		when(vetRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

		// When
		Page<Vet> result = vetService.findAll(pageable);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getContent()).isEmpty();
		assertThat(result.getTotalElements()).isEqualTo(0);
	}

}

