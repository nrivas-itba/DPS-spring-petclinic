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
package org.springframework.samples.petclinic.domain.usecase;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.domain.entity.Vet;

import java.util.Collection;

/**
 * Service interface for managing veterinarians.
 *
 * @author Wick Dynex
 */
public interface VetUseCase {

	/**
	 * Find all veterinarians.
	 * @return collection of veterinarians
	 */
	Collection<Vet> findAll();

	/**
	 * Find all veterinarians with pagination.
	 * @param pageable pagination information
	 * @return page of veterinarians
	 */
	Page<Vet> findAll(Pageable pageable);

}
