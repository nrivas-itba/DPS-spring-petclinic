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

import org.springframework.samples.petclinic.domain.entity.PetType;
import org.springframework.samples.petclinic.domain.gateway.PetTypeRepository;
import org.springframework.samples.petclinic.infrastructure.persistence.mapper.PetTypeMapper;
import org.springframework.samples.petclinic.infrastructure.persistence.jpa.PetTypeJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * JPA implementation of PetTypeRepository.
 *
 * @author Wick Dynex
 */
@Repository
public class PetTypeRepositoryImpl implements PetTypeRepository {

	private final PetTypeJpaRepository jpaRepository;

	private final PetTypeMapper mapper;

	public PetTypeRepositoryImpl(PetTypeJpaRepository jpaRepository, PetTypeMapper mapper) {
		this.jpaRepository = jpaRepository;
		this.mapper = mapper;
	}

	@Override
	public List<PetType> findAll() {
		return jpaRepository.findPetTypes().stream()
			.map(mapper::toDomain)
			.collect(Collectors.toList());
	}

}

