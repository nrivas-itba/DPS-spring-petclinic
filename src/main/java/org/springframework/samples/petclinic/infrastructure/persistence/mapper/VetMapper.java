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

import org.springframework.samples.petclinic.domain.entity.Vet;
import org.springframework.stereotype.Component;

/**
 * Mapper between Vet domain entities and JPA entities.
 *
 * @author Wick Dynex
 */
@Component
public class VetMapper {

	private final SpecialtyMapper specialtyMapper;

	public VetMapper(SpecialtyMapper specialtyMapper) {
		this.specialtyMapper = specialtyMapper;
	}

	public Vet toDomain(org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Vet jpa) {
		Vet domain = new Vet();
		domain.setId(jpa.getId());
		domain.setFirstName(jpa.getFirstName());
		domain.setLastName(jpa.getLastName());
		jpa.getSpecialties().forEach(specialty -> {
			domain.addSpecialty(specialtyMapper.toDomain(specialty));
		});
		return domain;
	}

	public org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Vet toJpa(Vet domain) {
		org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Vet jpa =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Vet();
		jpa.setId(domain.getId());
		jpa.setFirstName(domain.getFirstName());
		jpa.setLastName(domain.getLastName());
		domain.getSpecialties().forEach(specialty -> {
			jpa.addSpecialty(specialtyMapper.toJpa(specialty));
		});
		return jpa;
	}

}

