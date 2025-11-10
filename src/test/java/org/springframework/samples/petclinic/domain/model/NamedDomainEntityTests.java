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
package org.springframework.samples.petclinic.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for NamedDomainEntity.
 *
 * @author Wick Dynex
 */
class NamedDomainEntityTests {

	@Test
	void testToStringWithName() {
		// Given
		NamedDomainEntity entity = new NamedDomainEntity();
		entity.setName("Test Entity");

		// When
		String result = entity.toString();

		// Then
		assertThat(result).isEqualTo("Test Entity");
	}

	@Test
	void testToStringWithNullName() {
		// Given
		NamedDomainEntity entity = new NamedDomainEntity();
		entity.setName(null);

		// When
		String result = entity.toString();

		// Then
		assertThat(result).isEqualTo("<null>");
	}

	@Test
	void testToStringWithEmptyName() {
		// Given
		NamedDomainEntity entity = new NamedDomainEntity();
		entity.setName("");

		// When
		String result = entity.toString();

		// Then
		assertThat(result).isEqualTo("");
	}

	@Test
	void testToStringWithDefaultName() {
		// Given
		NamedDomainEntity entity = new NamedDomainEntity();
		// Don't set name, it should be null by default

		// When
		String result = entity.toString();

		// Then
		assertThat(result).isEqualTo("<null>");
	}

	@Test
	void testToStringWithSpecialCharacters() {
		// Given
		NamedDomainEntity entity = new NamedDomainEntity();
		entity.setName("Test@Entity#123");

		// When
		String result = entity.toString();

		// Then
		assertThat(result).isEqualTo("Test@Entity#123");
	}

}

