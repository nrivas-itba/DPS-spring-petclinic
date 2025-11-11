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

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.domain.entity.Owner;
import org.springframework.samples.petclinic.domain.gateway.OwnerRepository;
import org.springframework.samples.petclinic.infrastructure.persistence.mapper.OwnerMapper;
import org.springframework.samples.petclinic.infrastructure.persistence.jpa.OwnerJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA implementation of OwnerRepository.
 *
 * @author Wick Dynex
 */
@Repository
public class OwnerRepositoryImpl implements OwnerRepository {

	private final OwnerJpaRepository jpaRepository;

	private final OwnerMapper mapper;

	public OwnerRepositoryImpl(OwnerJpaRepository jpaRepository, OwnerMapper mapper) {
		this.jpaRepository = jpaRepository;
		this.mapper = mapper;
	}

	@Override
	public Page<Owner> findByLastNameStartingWith(String lastName, Pageable pageable) {
		return jpaRepository.findByLastNameStartingWith(lastName, pageable)
			.map(mapper::toDomain);
	}

	@Override
	public Optional<Owner> findById(Integer id) {
		return jpaRepository.findById(id).map(mapper::toDomain);
	}

	@Override
	public Owner save(Owner owner) {
		var jpaEntity = mapper.toJpa(owner);
		var saved = jpaRepository.save(jpaEntity);
		return mapper.toDomain(saved);
	}

}
