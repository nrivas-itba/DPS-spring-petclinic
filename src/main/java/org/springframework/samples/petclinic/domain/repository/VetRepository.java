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
package org.springframework.samples.petclinic.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.domain.model.Vet;

import java.util.Collection;

/**
 * Repository interface for Vet domain objects.
 * This interface is independent of JPA and can be implemented by any persistence technology.
 *
 * @author Wick Dynex
 */
public interface VetRepository {

	/**
	 * Retrieve all <code>Vet</code>s from the data store.
	 * @return a <code>Collection</code> of <code>Vet</code>s
	 */
	Collection<Vet> findAll();

	/**
	 * Retrieve all <code>Vet</code>s from data store in Pages
	 * @param pageable pagination information
	 * @return page of veterinarians
	 */
	Page<Vet> findAll(Pageable pageable);

}

