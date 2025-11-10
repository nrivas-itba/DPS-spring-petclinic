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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.domain.model.Owner;
import org.springframework.samples.petclinic.infrastructure.persistence.mapper.OwnerMapper;
import org.springframework.samples.petclinic.infrastructure.persistence.repository.OwnerJpaRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Unit tests for OwnerRepositoryImpl.
 *
 * @author Wick Dynex
 */
@ExtendWith(MockitoExtension.class)
class OwnerRepositoryImplTests {

	@Mock
	private OwnerJpaRepository jpaRepository;

	@Mock
	private OwnerMapper mapper;

	private OwnerRepositoryImpl repository;

	@BeforeEach
	void setUp() {
		repository = new OwnerRepositoryImpl(jpaRepository, mapper);
	}

	@Test
	void testSave() {
		// Given
		Owner domainOwner = new Owner();
		domainOwner.setFirstName("George");
		domainOwner.setLastName("Franklin");
		domainOwner.setAddress("110 W. Liberty St.");
		domainOwner.setCity("Madison");
		domainOwner.setTelephone("6085551023");

		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner jpaOwner =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner();
		jpaOwner.setFirstName("George");
		jpaOwner.setLastName("Franklin");
		jpaOwner.setAddress("110 W. Liberty St.");
		jpaOwner.setCity("Madison");
		jpaOwner.setTelephone("6085551023");

		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner savedJpaOwner =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner();
		savedJpaOwner.setId(1);
		savedJpaOwner.setFirstName("George");
		savedJpaOwner.setLastName("Franklin");
		savedJpaOwner.setAddress("110 W. Liberty St.");
		savedJpaOwner.setCity("Madison");
		savedJpaOwner.setTelephone("6085551023");

		Owner savedDomainOwner = new Owner();
		savedDomainOwner.setId(1);
		savedDomainOwner.setFirstName("George");
		savedDomainOwner.setLastName("Franklin");
		savedDomainOwner.setAddress("110 W. Liberty St.");
		savedDomainOwner.setCity("Madison");
		savedDomainOwner.setTelephone("6085551023");

		when(mapper.toJpa(domainOwner)).thenReturn(jpaOwner);
		when(jpaRepository.save(jpaOwner)).thenReturn(savedJpaOwner);
		when(mapper.toDomain(savedJpaOwner)).thenReturn(savedDomainOwner);

		// When
		Owner result = repository.save(domainOwner);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(1);
		assertThat(result.getFirstName()).isEqualTo("George");
		assertThat(result.getLastName()).isEqualTo("Franklin");
		assertThat(result.getAddress()).isEqualTo("110 W. Liberty St.");
		assertThat(result.getCity()).isEqualTo("Madison");
		assertThat(result.getTelephone()).isEqualTo("6085551023");
	}

	@Test
	void testSaveExistingOwner() {
		// Given
		Owner existingDomainOwner = new Owner();
		existingDomainOwner.setId(5);
		existingDomainOwner.setFirstName("Eduardo");
		existingDomainOwner.setLastName("Rodriquez");
		existingDomainOwner.setAddress("2693 Commerce St.");
		existingDomainOwner.setCity("McFarland");
		existingDomainOwner.setTelephone("6085558763");

		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner jpaOwner =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner();
		jpaOwner.setId(5);
		jpaOwner.setFirstName("Eduardo");
		jpaOwner.setLastName("Rodriquez");
		jpaOwner.setAddress("2693 Commerce St.");
		jpaOwner.setCity("McFarland");
		jpaOwner.setTelephone("6085558763");

		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner savedJpaOwner =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner();
		savedJpaOwner.setId(5);
		savedJpaOwner.setFirstName("Eduardo");
		savedJpaOwner.setLastName("Rodriquez");
		savedJpaOwner.setAddress("2693 Commerce St.");
		savedJpaOwner.setCity("McFarland");
		savedJpaOwner.setTelephone("6085558763");

		Owner savedDomainOwner = new Owner();
		savedDomainOwner.setId(5);
		savedDomainOwner.setFirstName("Eduardo");
		savedDomainOwner.setLastName("Rodriquez");
		savedDomainOwner.setAddress("2693 Commerce St.");
		savedDomainOwner.setCity("McFarland");
		savedDomainOwner.setTelephone("6085558763");

		when(mapper.toJpa(existingDomainOwner)).thenReturn(jpaOwner);
		when(jpaRepository.save(any(org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner.class)))
			.thenReturn(savedJpaOwner);
		when(mapper.toDomain(savedJpaOwner)).thenReturn(savedDomainOwner);

		// When
		Owner result = repository.save(existingDomainOwner);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(5);
	}

	@Test
	void testFindByLastNameStartingWith() {
		// Given
		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner jpaOwner1 =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner();
		jpaOwner1.setId(1);
		jpaOwner1.setFirstName("Betty");
		jpaOwner1.setLastName("Davis");
		jpaOwner1.setAddress("638 Cardinal Ave.");
		jpaOwner1.setCity("Sun Prairie");
		jpaOwner1.setTelephone("6085551749");

		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner jpaOwner2 =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner();
		jpaOwner2.setId(2);
		jpaOwner2.setFirstName("Harold");
		jpaOwner2.setLastName("Daniels");
		jpaOwner2.setAddress("563 Friendly St.");
		jpaOwner2.setCity("Windsor");
		jpaOwner2.setTelephone("6085553198");

		List<org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner> jpaOwners =
			Arrays.asList(jpaOwner1, jpaOwner2);
		Pageable pageable = PageRequest.of(0, 10);
		Page<org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner> jpaPage =
			new PageImpl<>(jpaOwners, pageable, 2);

		Owner domainOwner1 = new Owner();
		domainOwner1.setId(1);
		domainOwner1.setFirstName("Betty");
		domainOwner1.setLastName("Davis");
		domainOwner1.setAddress("638 Cardinal Ave.");
		domainOwner1.setCity("Sun Prairie");
		domainOwner1.setTelephone("6085551749");

		Owner domainOwner2 = new Owner();
		domainOwner2.setId(2);
		domainOwner2.setFirstName("Harold");
		domainOwner2.setLastName("Daniels");
		domainOwner2.setAddress("563 Friendly St.");
		domainOwner2.setCity("Windsor");
		domainOwner2.setTelephone("6085553198");

		when(jpaRepository.findByLastNameStartingWith(eq("Da"), any(Pageable.class))).thenReturn(jpaPage);
		when(mapper.toDomain(jpaOwner1)).thenReturn(domainOwner1);
		when(mapper.toDomain(jpaOwner2)).thenReturn(domainOwner2);

		// When
		Page<Owner> result = repository.findByLastNameStartingWith("Da", pageable);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getContent()).hasSize(2);
		assertThat(result.getTotalElements()).isEqualTo(2);
		assertThat(result.getNumber()).isEqualTo(0);
		assertThat(result.getSize()).isEqualTo(10);

		List<Owner> owners = result.getContent();
		assertThat(owners.get(0).getFirstName()).isEqualTo("Betty");
		assertThat(owners.get(0).getLastName()).isEqualTo("Davis");
		assertThat(owners.get(1).getFirstName()).isEqualTo("Harold");
		assertThat(owners.get(1).getLastName()).isEqualTo("Daniels");
	}

	@Test
	void testFindByLastNameStartingWithWhenEmpty() {
		// Given
		Pageable pageable = PageRequest.of(0, 10);
		Page<org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner> emptyPage =
			new PageImpl<>(Arrays.asList(), pageable, 0);

		when(jpaRepository.findByLastNameStartingWith(eq("XYZ"), any(Pageable.class))).thenReturn(emptyPage);

		// When
		Page<Owner> result = repository.findByLastNameStartingWith("XYZ", pageable);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getContent()).isEmpty();
		assertThat(result.getTotalElements()).isEqualTo(0);
	}

	@Test
	void testFindByLastNameStartingWithWithPagination() {
		// Given
		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner jpaOwner3 =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner();
		jpaOwner3.setId(3);
		jpaOwner3.setFirstName("Eduardo");
		jpaOwner3.setLastName("Rodriquez");
		jpaOwner3.setAddress("2693 Commerce St.");
		jpaOwner3.setCity("McFarland");
		jpaOwner3.setTelephone("6085558763");

		List<org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner> jpaOwners =
			Arrays.asList(jpaOwner3);
		Pageable pageable = PageRequest.of(1, 2);
		Page<org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner> jpaPage =
			new PageImpl<>(jpaOwners, pageable, 5);

		Owner domainOwner3 = new Owner();
		domainOwner3.setId(3);
		domainOwner3.setFirstName("Eduardo");
		domainOwner3.setLastName("Rodriquez");
		domainOwner3.setAddress("2693 Commerce St.");
		domainOwner3.setCity("McFarland");
		domainOwner3.setTelephone("6085558763");

		when(jpaRepository.findByLastNameStartingWith(eq("Ro"), any(Pageable.class))).thenReturn(jpaPage);
		when(mapper.toDomain(jpaOwner3)).thenReturn(domainOwner3);

		// When
		Page<Owner> result = repository.findByLastNameStartingWith("Ro", pageable);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getTotalElements()).isEqualTo(5);
		assertThat(result.getNumber()).isEqualTo(1); // Second page (0-indexed)
		assertThat(result.getSize()).isEqualTo(2);
		assertThat(result.getContent().get(0).getLastName()).isEqualTo("Rodriquez");
	}

}

