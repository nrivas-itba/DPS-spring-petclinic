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

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for Vet domain model.
 *
 * @author Wick Dynex
 */
class VetDomainTests {

	@Test
	void testGetSpecialtiesReturnsSortedList() {
		// Given
		Vet vet = new Vet();

		Specialty surgery = new Specialty();
		surgery.setId(1);
		surgery.setName("Surgery");

		Specialty dentistry = new Specialty();
		dentistry.setId(2);
		dentistry.setName("Dentistry");

		Specialty radiology = new Specialty();
		radiology.setId(3);
		radiology.setName("Radiology");

		// Add specialties in non-alphabetical order
		vet.addSpecialty(surgery);
		vet.addSpecialty(dentistry);
		vet.addSpecialty(radiology);

		// When
		List<Specialty> specialties = vet.getSpecialties();

		// Then
		assertThat(specialties).hasSize(3);
		assertThat(specialties.get(0).getName()).isEqualTo("Dentistry");
		assertThat(specialties.get(1).getName()).isEqualTo("Radiology");
		assertThat(specialties.get(2).getName()).isEqualTo("Surgery");
	}

	@Test
	void testGetSpecialtiesWhenEmpty() {
		// Given
		Vet vet = new Vet();

		// When
		List<Specialty> specialties = vet.getSpecialties();

		// Then
		assertThat(specialties).isEmpty();
	}

	@Test
	void testGetSpecialtiesWithSingleSpecialty() {
		// Given
		Vet vet = new Vet();

		Specialty radiology = new Specialty();
		radiology.setId(1);
		radiology.setName("Radiology");

		vet.addSpecialty(radiology);

		// When
		List<Specialty> specialties = vet.getSpecialties();

		// Then
		assertThat(specialties).hasSize(1);
		assertThat(specialties.get(0).getName()).isEqualTo("Radiology");
	}

	@Test
	void testGetSpecialtiesReturnsSortedWithCaseInsensitivity() {
		// Given
		Vet vet = new Vet();

		Specialty zebra = new Specialty();
		zebra.setId(1);
		zebra.setName("Zebra Studies");

		Specialty apple = new Specialty();
		apple.setId(2);
		apple.setName("Apple Care");

		Specialty monkey = new Specialty();
		monkey.setId(3);
		monkey.setName("Monkey Medicine");

		vet.addSpecialty(zebra);
		vet.addSpecialty(apple);
		vet.addSpecialty(monkey);

		// When
		List<Specialty> specialties = vet.getSpecialties();

		// Then
		assertThat(specialties).hasSize(3);
		assertThat(specialties.get(0).getName()).isEqualTo("Apple Care");
		assertThat(specialties.get(1).getName()).isEqualTo("Monkey Medicine");
		assertThat(specialties.get(2).getName()).isEqualTo("Zebra Studies");
	}

}

