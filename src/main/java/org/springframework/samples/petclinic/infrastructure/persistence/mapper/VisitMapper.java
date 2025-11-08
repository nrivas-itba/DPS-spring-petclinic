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

import org.springframework.samples.petclinic.domain.model.Visit;
import org.springframework.stereotype.Component;

/**
 * Mapper between Visit domain entities and JPA entities.
 *
 * @author Wick Dynex
 */
@Component
public class VisitMapper {

	public Visit toDomain(org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Visit jpa) {
		if (jpa == null) {
			return null;
		}
		Visit domain = new Visit();
		domain.setId(jpa.getId());
		domain.setDate(jpa.getDate());
		domain.setDescription(jpa.getDescription());
		return domain;
	}

	public org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Visit toJpa(Visit domain) {
		if (domain == null) {
			return null;
		}
		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Visit jpa = 
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Visit();
		jpa.setId(domain.getId());
		jpa.setDate(domain.getDate());
		jpa.setDescription(domain.getDescription());
		return jpa;
	}

}

