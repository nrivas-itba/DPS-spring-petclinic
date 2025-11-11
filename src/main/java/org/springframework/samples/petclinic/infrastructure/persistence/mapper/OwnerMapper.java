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

import org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper between Owner domain entities and JPA entities.
 *
 * @author Wick Dynex
 */
@Component
public class OwnerMapper {

	private final PetMapper petMapper;

	public OwnerMapper(PetMapper petMapper) {
		this.petMapper = petMapper;
	}

	public org.springframework.samples.petclinic.domain.entity.Owner toDomain(
			Owner jpa) {
		org.springframework.samples.petclinic.domain.entity.Owner domain = new org.springframework.samples.petclinic.domain.entity.Owner();
		domain.setId(jpa.getId());
		domain.setFirstName(jpa.getFirstName());
		domain.setLastName(jpa.getLastName());
		domain.setAddress(jpa.getAddress());
		domain.setCity(jpa.getCity());
		domain.setTelephone(jpa.getTelephone());
		domain.getPets().addAll(jpa.getPets().stream()
			.map(petMapper::toDomain)
			.collect(Collectors.toList()));
		return domain;
	}

	public Owner toJpa(
			org.springframework.samples.petclinic.domain.entity.Owner domain) {
		Owner jpa =
			new Owner();
		jpa.setId(domain.getId());
		jpa.setFirstName(domain.getFirstName());
		jpa.setLastName(domain.getLastName());
		jpa.setAddress(domain.getAddress());
		jpa.setCity(domain.getCity());
		jpa.setTelephone(domain.getTelephone());
		jpa.getPets().addAll(domain.getPets().stream()
			.map(petMapper::toJpa)
			.collect(Collectors.toList()));
		return jpa;
	}

}
