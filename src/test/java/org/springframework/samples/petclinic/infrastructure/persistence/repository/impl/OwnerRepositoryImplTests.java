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
import org.springframework.samples.petclinic.domain.model.Owner;
import org.springframework.samples.petclinic.infrastructure.persistence.mapper.OwnerMapper;
import org.springframework.samples.petclinic.infrastructure.persistence.repository.OwnerJpaRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
		
		verify(mapper).toJpa(domainOwner);
		verify(jpaRepository).save(jpaOwner);
		verify(mapper).toDomain(savedJpaOwner);
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
		verify(mapper).toJpa(existingDomainOwner);
		verify(jpaRepository).save(jpaOwner);
		verify(mapper).toDomain(savedJpaOwner);
	}

}

