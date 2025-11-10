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
import org.springframework.samples.petclinic.domain.model.PetType;
import org.springframework.samples.petclinic.domain.repository.PetTypeRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit tests for PetTypeServiceImpl.
 *
 * @author Wick Dynex
 */
@ExtendWith(MockitoExtension.class)
class PetTypeUseCaseImplTests {

	@Mock
	private PetTypeRepository petTypeRepository;

	private PetTypeUseCaseImpl petTypeService;

	@BeforeEach
	void setUp() {
		petTypeService = new PetTypeUseCaseImpl(petTypeRepository);
	}

	@Test
	void testFindAll() {
		// Given
		PetType cat = new PetType();
		cat.setId(1);
		cat.setName("cat");

		PetType dog = new PetType();
		dog.setId(2);
		dog.setName("dog");

		PetType hamster = new PetType();
		hamster.setId(3);
		hamster.setName("hamster");

		List<PetType> expectedPetTypes = Arrays.asList(cat, dog, hamster);
		when(petTypeRepository.findAll()).thenReturn(expectedPetTypes);

		// When
		List<PetType> result = petTypeService.findAll();

		// Then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(3);
		assertThat(result).containsExactly(cat, dog, hamster);
	}

	@Test
	void testFindAllWhenEmpty() {
		// Given
		when(petTypeRepository.findAll()).thenReturn(Arrays.asList());

		// When
		List<PetType> result = petTypeService.findAll();

		// Then
		assertThat(result).isNotNull();
		assertThat(result).isEmpty();
	}

}

