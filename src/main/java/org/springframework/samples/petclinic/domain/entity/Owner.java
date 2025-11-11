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
import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;


public class Owner extends PersonDomainEntity {

	private String address;

	private String city;

	private String telephone;

	private final List<Pet> pets = new ArrayList<>();

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
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


	public @Nullable Pet getPet(Integer id) {
		return getPets().stream()
			.filter(pet -> !pet.isNew() && Objects.equals(pet.getId(), id))
			.findFirst()
			.orElse(null);
	}


	public @Nullable Pet getPet(String name, boolean ignoreNew) {
		return getPets().stream()
			.filter(pet -> {
				String compName = pet.getName();
				return compName != null && compName.equalsIgnoreCase(name) && (!ignoreNew || !pet.isNew());
			})
			.findFirst()
			.orElse(null);
	}


	public void addVisit(Integer petId, Visit visit) {
		Pet pet = getPet(petId);
		if (pet != null) {
			pet.addVisit(visit);
		}
	}

}

