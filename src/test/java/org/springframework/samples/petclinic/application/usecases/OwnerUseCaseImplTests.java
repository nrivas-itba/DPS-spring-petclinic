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
import org.springframework.samples.petclinic.domain.model.Owner;
import org.springframework.samples.petclinic.domain.repository.OwnerRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Unit tests for OwnerServiceImpl.
 *
 * @author Wick Dynex
 */
@ExtendWith(MockitoExtension.class)
class OwnerUseCaseImplTests {

	@Mock
	private OwnerRepository ownerRepository;

	private OwnerUseCaseImpl ownerService;

	@BeforeEach
	void setUp() {
		ownerService = new OwnerUseCaseImpl(ownerRepository);
	}

	@Test
	void testFindByLastNameStartingWith() {
		// Given
		Owner owner1 = new Owner();
		owner1.setId(1);
		owner1.setFirstName("George");
		owner1.setLastName("Franklin");

		Owner owner2 = new Owner();
		owner2.setId(2);
		owner2.setFirstName("Betty");
		owner2.setLastName("Davis");

		Owner owner3 = new Owner();
		owner3.setId(3);
		owner3.setFirstName("Eduardo");
		owner3.setLastName("Rodriquez");

		List<Owner> owners = Arrays.asList(owner2);
		Pageable pageable = PageRequest.of(0, 10);
		Page<Owner> expectedPage = new PageImpl<>(owners, pageable, 1);

		when(ownerRepository.findByLastNameStartingWith(anyString(), any(Pageable.class)))
			.thenReturn(expectedPage);

		// When
		Page<Owner> result = ownerService.findByLastNameStartingWith("Dav", pageable);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent().get(0).getLastName()).isEqualTo("Davis");
		assertThat(result.getTotalElements()).isEqualTo(1);
	}

	@Test
	void testFindByLastNameStartingWith_WhenNoMatches() {
		// Given
		Pageable pageable = PageRequest.of(0, 10);
		Page<Owner> emptyPage = new PageImpl<>(Arrays.asList(), pageable, 0);

		when(ownerRepository.findByLastNameStartingWith(anyString(), any(Pageable.class)))
			.thenReturn(emptyPage);

		// When
		Page<Owner> result = ownerService.findByLastNameStartingWith("XYZ", pageable);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getContent()).isEmpty();
		assertThat(result.getTotalElements()).isEqualTo(0);
	}

	@Test
	void testFindByLastNameStartingWith_WithEmptyString() {
		// Given
		Owner owner1 = new Owner();
		owner1.setId(1);
		owner1.setFirstName("George");
		owner1.setLastName("Franklin");

		Owner owner2 = new Owner();
		owner2.setId(2);
		owner2.setFirstName("Betty");
		owner2.setLastName("Davis");

		List<Owner> owners = Arrays.asList(owner1, owner2);
		Pageable pageable = PageRequest.of(0, 10);
		Page<Owner> expectedPage = new PageImpl<>(owners, pageable, 2);

		when(ownerRepository.findByLastNameStartingWith(anyString(), any(Pageable.class)))
			.thenReturn(expectedPage);

		// When
		Page<Owner> result = ownerService.findByLastNameStartingWith("", pageable);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getContent()).hasSize(2);
		assertThat(result.getTotalElements()).isEqualTo(2);
	}

	@Test
	void testSave() {
		// Given
		Owner owner = new Owner();
		owner.setFirstName("Harold");
		owner.setLastName("Davis");
		owner.setAddress("638 Cardinal Ave.");
		owner.setCity("Sun Prairie");
		owner.setTelephone("6085551023");

		Owner savedOwner = new Owner();
		savedOwner.setId(100);
		savedOwner.setFirstName("Harold");
		savedOwner.setLastName("Davis");
		savedOwner.setAddress("638 Cardinal Ave.");
		savedOwner.setCity("Sun Prairie");
		savedOwner.setTelephone("6085551023");

		when(ownerRepository.save(any(Owner.class))).thenReturn(savedOwner);

		// When
		Owner result = ownerService.save(owner);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(100);
		assertThat(result.getFirstName()).isEqualTo("Harold");
		assertThat(result.getLastName()).isEqualTo("Davis");
	}

	@Test
	void testSave_UpdateExistingOwner() {
		// Given
		Owner existingOwner = new Owner();
		existingOwner.setId(1);
		existingOwner.setFirstName("George");
		existingOwner.setLastName("Franklin");
		existingOwner.setAddress("110 W. Liberty St.");
		existingOwner.setCity("Madison");
		existingOwner.setTelephone("6085551023");

		existingOwner.setAddress("111 W. Liberty St."); // Updated address

		when(ownerRepository.save(any(Owner.class))).thenReturn(existingOwner);

		// When
		Owner result = ownerService.save(existingOwner);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(1);
		assertThat(result.getAddress()).isEqualTo("111 W. Liberty St.");
	}

}

