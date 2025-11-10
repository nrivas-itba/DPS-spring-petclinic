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
package org.springframework.samples.petclinic.infrastructure.persistence.entity.owner;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for Visit entity.
 *
 * @author Wick Dynex
 */
class VisitEntityTests {

	@Test
	void testDefaultConstructorSetsCurrentDate() {
		// When
		Visit visit = new Visit();

		// Then
		assertThat(visit.getDate()).isNotNull();
		assertThat(visit.getDate()).isEqualTo(LocalDate.now());
	}

	@Test
	void testSetDate() {
		// Given
		Visit visit = new Visit();
		LocalDate newDate = LocalDate.of(2025, 11, 10);

		// When
		visit.setDate(newDate);

		// Then
		assertThat(visit.getDate()).isEqualTo(newDate);
	}

	@Test
	void testSetDateToNull() {
		// Given
		Visit visit = new Visit();

		// When
		visit.setDate(null);

		// Then
		assertThat(visit.getDate()).isNull();
	}

	@Test
	void testSetDescription() {
		// Given
		Visit visit = new Visit();

		// When
		visit.setDescription("Rabies shot");

		// Then
		assertThat(visit.getDescription()).isEqualTo("Rabies shot");
	}

	@Test
	void testSetDescriptionToNull() {
		// Given
		Visit visit = new Visit();
		visit.setDescription("Initial description");

		// When
		visit.setDescription(null);

		// Then
		assertThat(visit.getDescription()).isNull();
	}

}

