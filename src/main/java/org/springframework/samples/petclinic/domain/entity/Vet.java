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
package org.springframework.samples.petclinic.domain.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Domain entity representing a veterinarian.
 *
 * @author Wick Dynex
 */
public class Vet extends PersonDomainEntity {

	private List<Specialty> specialties = new ArrayList<>();

	public List<Specialty> getSpecialties() {
		return specialties.stream()
			.sorted(Comparator.comparing(Specialty::getName))
			.collect(Collectors.toList());
	}


	public void addSpecialty(Specialty specialty) {
		specialties.add(specialty);
	}

}

