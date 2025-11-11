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
package org.springframework.samples.petclinic.application.usecaseimpl;

import org.springframework.samples.petclinic.domain.usecase.PetUseCase;
import org.springframework.samples.petclinic.domain.entity.Owner;
import org.springframework.samples.petclinic.domain.entity.Pet;
import org.springframework.samples.petclinic.domain.gateway.OwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Service implementation for managing pets.
 *
 * @author Wick Dynex
 */
@Service
@Transactional(readOnly = true)
public class PetUseCaseImpl implements PetUseCase {

	private final OwnerRepository ownerRepository;

	public PetUseCaseImpl(OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
	}

	@Override
	@Transactional
	public Owner createPet(Owner owner, Pet pet) {
		owner.addPet(pet);
		return ownerRepository.save(owner);
	}

	@Override
	@Transactional
	public Owner updatePet(Owner owner, Pet pet) {
		owner.getPets().stream()
			.filter(existingPet -> Objects.equals(existingPet.getId(), pet.getId()))
			.findFirst()
			.ifPresentOrElse(
				existingPet -> updatePetProperties(existingPet, pet),
				() -> owner.addPet(pet)
			);

		return ownerRepository.save(owner);
	}

	@Override
	public boolean isPetNameUniqueForOwner(Owner owner, String petName, Integer petId) {
		if (!StringUtils.hasText(petName)) {
			return true;
		}

		Pet existingPet = owner.getPet(petName, false);
		return existingPet == null || Objects.equals(existingPet.getId(), petId);
	}

	@Override
	public boolean isBirthDateValid(LocalDate birthDate) {
		LocalDate currentDate = LocalDate.now();
		return !birthDate.isAfter(currentDate);
	}

	private void updatePetProperties(Pet existingPet, Pet pet) {
		existingPet.setName(pet.getName());
		existingPet.setBirthDate(pet.getBirthDate());
		existingPet.setType(pet.getType());
	}

}
