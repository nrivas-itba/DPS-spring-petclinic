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

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.domain.model.Vet;
import org.springframework.samples.petclinic.domain.repository.VetRepository;
import org.springframework.samples.petclinic.infrastructure.persistence.mapper.VetMapper;
import org.springframework.samples.petclinic.infrastructure.persistence.repository.VetJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * JPA implementation of VetRepository.
 *
 * @author Wick Dynex
 */
@Repository
public class VetRepositoryImpl implements VetRepository {

	private final VetJpaRepository jpaRepository;

	private final VetMapper mapper;

	public VetRepositoryImpl(VetJpaRepository jpaRepository, VetMapper mapper) {
		this.jpaRepository = jpaRepository;
		this.mapper = mapper;
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable("vets")
	public Collection<Vet> findAll() {
		return jpaRepository.findAll().stream()
			.map(mapper::toDomain)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable("vets")
	public Page<Vet> findAll(Pageable pageable) {
		return jpaRepository.findAll(pageable).map(mapper::toDomain);
	}

}

