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
package org.springframework.samples.petclinic.application.exception;

import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.domain.exception.OwnerNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for OwnerNotFoundException.
 *
 * @author Wick Dynex
 */
class OwnerNotFoundExceptionTests {

	@Test
	void testConstructorWithMessage() {
		// Given
		String message = "Owner not found with id: 1";

		// When
		OwnerNotFoundException exception = new OwnerNotFoundException(message);

		// Then
		assertThat(exception).isNotNull();
		assertThat(exception.getMessage()).isEqualTo(message);
		assertThat(exception).isInstanceOf(RuntimeException.class);
	}

	@Test
	void testConstructorWithNullMessage() {
		// When
		OwnerNotFoundException exception = new OwnerNotFoundException(null);

		// Then
		assertThat(exception).isNotNull();
		assertThat(exception.getMessage()).isNull();
		assertThat(exception).isInstanceOf(RuntimeException.class);
	}

	@Test
	void testConstructorWithEmptyMessage() {
		// Given
		String message = "";

		// When
		OwnerNotFoundException exception = new OwnerNotFoundException(message);

		// Then
		assertThat(exception).isNotNull();
		assertThat(exception.getMessage()).isEqualTo("");
	}

}

