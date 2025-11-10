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

package org.springframework.samples.petclinic.web.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.samples.petclinic.application.exception.PetNotFoundException;
import org.springframework.samples.petclinic.application.service.interfaces.OwnerService;
import org.springframework.samples.petclinic.application.service.interfaces.VisitService;
import org.springframework.samples.petclinic.domain.model.Owner;
import org.springframework.samples.petclinic.domain.model.Pet;
import org.springframework.samples.petclinic.domain.model.Visit;
import org.springframework.samples.petclinic.infrastructure.persistence.mapper.OwnerMapper;
import org.springframework.samples.petclinic.infrastructure.persistence.mapper.PetMapper;
import org.springframework.samples.petclinic.infrastructure.persistence.mapper.VisitMapper;
import org.springframework.samples.petclinic.web.controller.VisitController;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 * @author Wick Dynex
 */
@WebMvcTest(VisitController.class)
@DisabledInNativeImage
@DisabledInAotMode
class VisitControllerTests {

	private static final int TEST_OWNER_ID = 1;

	private static final int TEST_PET_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private OwnerService ownerService;

	@MockitoBean
	private VisitService visitService;

	@MockitoBean
	private OwnerMapper ownerMapper;

	@MockitoBean
	private PetMapper petMapper;

	@MockitoBean
	private VisitMapper visitMapper;

	@BeforeEach
	void init() {
		Owner owner = new Owner();
		owner.setId(TEST_OWNER_ID);
		Pet pet = new Pet();
		owner.addPet(pet);
		pet.setId(TEST_PET_ID);

		// Create JPA entities
		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner ownerJpa =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner();
		ownerJpa.setId(owner.getId());
		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet petJpa =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet();
		petJpa.setId(pet.getId());
		ownerJpa.addPet(petJpa);

		// Create non-existent pet JPA entity
		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet nonExistentPetJpa =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet();
		nonExistentPetJpa.setId(999);

		// Mock owner service
		given(this.ownerService.findById(TEST_OWNER_ID)).willReturn(owner);

		// Mock owner mapper
		given(this.ownerMapper.toJpa(any(Owner.class))).willReturn(ownerJpa);
		given(this.ownerMapper.toDomain(any(org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner.class)))
			.willReturn(owner);

		// Mock pet mapper
		given(this.petMapper.toJpa(any(Pet.class))).willReturn(petJpa);
		given(this.petMapper.toDomain(any(org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet.class)))
			.will(invocation -> {
				org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet p =
					invocation.getArgument(0);
				if (p.getId() == nonExistentPetJpa.getId()) {
					throw new PetNotFoundException("Pet with id " + nonExistentPetJpa.getId() +
						" not found for owner with id " + owner.getId() + ".");
				}
				return pet;
			});

		// Mock visit service
		given(this.visitService.createVisit(any(Owner.class), any(Integer.class), any(Visit.class)))
			.will(invocation -> {
				Owner o = invocation.getArgument(0);
				Integer petId = invocation.getArgument(1);
				Visit visit = invocation.getArgument(2);
				o.addVisit(petId, visit);
				return o;
			});

		// Mock visit mapper
		given(this.visitMapper.toDomain(any(org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Visit.class)))
			.will(invocation -> {
				org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Visit v =
					invocation.getArgument(0);
				Visit domain = new Visit();
				domain.setId(v.getId());
				domain.setDate(v.getDate());
				domain.setDescription(v.getDescription());
				return domain;
			});
	}

	@Test
	void testInitNewVisitForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdateVisitForm"));
	}

	@Test
	void testProcessNewVisitFormSuccess() throws Exception {
		mockMvc
			.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID)
				.param("name", "George")
				.param("description", "Visit Description"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@Test
	void testProcessNewVisitFormHasErrors() throws Exception {
		mockMvc
			.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, TEST_PET_ID).param("name",
					"George"))
			.andExpect(model().attributeHasErrors("visit"))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdateVisitForm"));
	}

	@Test
	void testInitNewVisitFormWithNonExistentPet() throws Exception {
		try {
			mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID, 999));
		} catch (Exception ex) {
			assert(ex.getCause() instanceof PetNotFoundException);
		}
	}

}
