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
package org.springframework.samples.petclinic.application.service.interfaces;

import org.springframework.samples.petclinic.domain.model.Owner;
import org.springframework.samples.petclinic.domain.model.Pet;

import java.time.LocalDate;

/**
 * Service interface for managing pets.
 *
 * @author Wick Dynex
 */
public interface PetService {

	/**
	 * Create a new pet for an owner.
	 * @param owner the owner
	 * @param pet the pet to create
	 * @return the saved owner
	 */
	Owner createPet(Owner owner, Pet pet);

	/**
	 * Update an existing pet.
	 * @param owner the owner
	 * @param pet the pet to update
	 * @return the saved owner
	 */
	Owner updatePet(Owner owner, Pet pet);

	/**
	 * Validate that a pet name is unique for an owner.
	 * @param owner the owner
	 * @param petName the pet name
	 * @param petId the pet ID (null for new pets)
	 * @return true if the name is unique, false otherwise
	 */
	boolean isPetNameUniqueForOwner(Owner owner, String petName, Integer petId);

	/**
	 * Validate that a birth date is not in the future.
	 * @param birthDate the birth date
	 * @return true if valid, false otherwise
	 */
	boolean isBirthDateValid(LocalDate birthDate);

}
