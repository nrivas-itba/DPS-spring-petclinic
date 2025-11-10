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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;

/**
 * Domain entity representing an owner.
 *
 * @author Wick Dynex
 */
public class Owner extends PersonDomainEntity {

	private @Nullable String address;

	private @Nullable String city;

	private @Nullable String telephone;

	private final List<Pet> pets = new ArrayList<>();

	public @Nullable String getAddress() {
		return this.address;
	}

	public void setAddress(@Nullable String address) {
		this.address = address;
	}

	public @Nullable String getCity() {
		return this.city;
	}

	public void setCity(@Nullable String city) {
		this.city = city;
	}

	public @Nullable String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(@Nullable String telephone) {
		this.telephone = telephone;
	}

	public List<Pet> getPets() {
		return this.pets;
	}

	public void addPet(Pet pet) {
		if (pet.isNew()) {
			getPets().add(pet);
		}
	}

	/**
	 * Return the Pet with the given id, or null if none found for this Owner.
	 * @param id to test
	 * @return the Pet with the given id, or null if no such Pet exists for this Owner
	 */
	public @Nullable Pet getPet(Integer id) {
		return getPets().stream()
			.filter(pet -> !pet.isNew() && Objects.equals(pet.getId(), id))
			.findFirst()
			.orElse(null);
	}

	/**
	 * Return the Pet with the given name, or null if none found for this Owner.
	 * @param name to test
	 * @param ignoreNew whether to ignore new pets (pets that are not saved yet)
	 * @return the Pet with the given name, or null if no such Pet exists for this Owner
	 */
	public @Nullable Pet getPet(String name, boolean ignoreNew) {
		return getPets().stream()
			.filter(pet -> {
				String compName = pet.getName();
				return compName != null && compName.equalsIgnoreCase(name) && (!ignoreNew || !pet.isNew());
			})
			.findFirst()
			.orElse(null);
	}

	/**
	 * Adds the given {@link Visit} to the {@link Pet} with the given identifier.
	 * @param petId the identifier of the {@link Pet}, must not be {@literal null}.
	 * @param visit the visit to add, must not be {@literal null}.
	 */
	public void addVisit(Integer petId, Visit visit) {
		Pet pet = getPet(petId);
		if (pet != null) {
			pet.addVisit(visit);
		}
	}

}

