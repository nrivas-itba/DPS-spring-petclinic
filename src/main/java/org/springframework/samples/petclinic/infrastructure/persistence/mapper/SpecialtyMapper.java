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

import org.springframework.samples.petclinic.domain.model.Specialty;
import org.springframework.stereotype.Component;

/**
 * Mapper between Specialty domain entities and JPA entities.
 *
 * @author Wick Dynex
 */
@Component
public class SpecialtyMapper {

	public Specialty toDomain(org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Specialty jpa) {
		if (jpa == null) {
			return null;
		}
		Specialty domain = new Specialty();
		domain.setId(jpa.getId());
		domain.setName(jpa.getName());
		return domain;
	}

	public org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Specialty toJpa(Specialty domain) {
		if (domain == null) {
			return null;
		}
		org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Specialty jpa = 
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Specialty();
		jpa.setId(domain.getId());
		jpa.setName(domain.getName());
		return jpa;
	}

}

