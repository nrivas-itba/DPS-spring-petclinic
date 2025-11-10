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
package org.springframework.samples.petclinic.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.domain.model.Owner;
import org.springframework.samples.petclinic.domain.model.Pet;
import org.springframework.samples.petclinic.domain.model.Visit;
import org.springframework.samples.petclinic.domain.repository.OwnerRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;

/**
 * Unit tests for VisitServiceImpl.
 *
 * @author Wick Dynex
 */
@ExtendWith(MockitoExtension.class)
class VisitServiceImplTests {

	@Mock
	private OwnerRepository ownerRepository;

	private VisitServiceImpl visitService;

	@BeforeEach
	void setUp() {
		visitService = new VisitServiceImpl(ownerRepository);
	}

	@Test
	void testCreateVisit() {
		// Given
		Owner owner = new Owner();
		owner.setId(1);
		owner.setFirstName("George");
		owner.setLastName("Franklin");

		Pet pet = new Pet();
		pet.setId(1);
		pet.setName("Leo");
		owner.addPet(pet);

		Visit visit = new Visit();
		visit.setDate(LocalDate.of(2025, 11, 10));
		visit.setDescription("Rabies shot");

		Owner savedOwner = new Owner();
		savedOwner.setId(1);
		savedOwner.setFirstName("George");
		savedOwner.setLastName("Franklin");
		Pet savedPet = new Pet();
		savedPet.setId(1);
		savedPet.setName("Leo");
		savedOwner.addPet(savedPet);
		savedPet.addVisit(visit);

		when(ownerRepository.save(any(Owner.class))).thenReturn(savedOwner);

		// When
		Owner result = visitService.createVisit(owner, 1, visit);

		// Then
		assertThat(result).isNotNull();
	}

}

