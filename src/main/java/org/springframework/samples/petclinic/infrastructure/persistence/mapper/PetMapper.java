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
package org.springframework.samples.petclinic.infrastructure.persistence.mapper;

import org.springframework.samples.petclinic.domain.model.Pet;
import org.springframework.samples.petclinic.domain.model.Visit;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper between Pet domain entities and JPA entities.
 *
 * @author Wick Dynex
 */
@Component
public class PetMapper {

	private final PetTypeMapper petTypeMapper;

	private final VisitMapper visitMapper;

	public PetMapper(PetTypeMapper petTypeMapper, VisitMapper visitMapper) {
		this.petTypeMapper = petTypeMapper;
		this.visitMapper = visitMapper;
	}

	public Pet toDomain(org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet jpa) {
		Pet domain = new Pet();
		domain.setId(jpa.getId());
		domain.setName(jpa.getName());
		domain.setBirthDate(jpa.getBirthDate());
		domain.setType(petTypeMapper.toDomain(jpa.getType()));
		domain.getVisits().addAll(jpa.getVisits().stream()
			.map(visitMapper::toDomain)
			.collect(Collectors.toList()));
		return domain;
	}

	public org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet toJpa(Pet domain) {
		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet jpa =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet();
		jpa.setId(domain.getId());
		jpa.setName(domain.getName());
		jpa.setBirthDate(domain.getBirthDate());
		jpa.setType(petTypeMapper.toJpa(domain.getType()));
		domain.getVisits().forEach(visit -> {
			org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Visit visitJpa = visitMapper.toJpa(visit);
			jpa.addVisit(visitJpa);
		});
		return jpa;
	}

}

