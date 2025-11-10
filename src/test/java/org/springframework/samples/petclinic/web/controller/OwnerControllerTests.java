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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.application.service.interfaces.OwnerService;
import org.springframework.samples.petclinic.domain.model.Owner;
import org.springframework.samples.petclinic.domain.model.Pet;
import org.springframework.samples.petclinic.domain.model.PetType;
import org.springframework.samples.petclinic.domain.model.Visit;
import org.springframework.samples.petclinic.infrastructure.persistence.mapper.OwnerMapper;
import org.springframework.samples.petclinic.web.controller.OwnerController;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for {@link OwnerController}
 *
 * @author Colin But
 * @author Wick Dynex
 */
@WebMvcTest(OwnerController.class)
@DisabledInNativeImage
@DisabledInAotMode
class OwnerControllerTests {

	private static final int TEST_OWNER_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private OwnerService ownerService;

	@MockitoBean
	private OwnerMapper ownerMapper;

	private Owner george() {
		Owner george = new Owner();
		george.setId(TEST_OWNER_ID);
		george.setFirstName("George");
		george.setLastName("Franklin");
		george.setAddress("110 W. Liberty St.");
		george.setCity("Madison");
		george.setTelephone("6085551023");

		// Create a pet type
		PetType dog = new PetType();
		dog.setName("dog");

		// Create the pet
		Pet max = new Pet();
		max.setId(1);
		max.setName("Max");
		max.setType(dog);
		max.setBirthDate(LocalDate.now());

		// Add a visit
		Visit visit = new Visit();
		visit.setDate(LocalDate.now());
		visit.setDescription("Regular checkup");
		max.addVisit(visit);

		// Add the pet to the owner
		george.addPet(max); // ✅ ensures owner.pets contains the pet

		return george;
	}

	@BeforeEach
	void setup() {
		Owner george = george();
		given(this.ownerService.findByLastNameStartingWith(eq("Franklin"), any(Pageable.class)))
			.willReturn(new PageImpl<>(List.of(george)));

		given(this.ownerService.findById(TEST_OWNER_ID)).willReturn(george);
		given(this.ownerService.save(any(Owner.class))).willAnswer(invocation -> invocation.getArgument(0));

		// Mock mapper conversions
		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner georgeJpa =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner();
		georgeJpa.setId(george.getId());
		georgeJpa.setFirstName(george.getFirstName());
		georgeJpa.setLastName(george.getLastName());
		georgeJpa.setAddress(george.getAddress());
		georgeJpa.setCity(george.getCity());
		georgeJpa.setTelephone(george.getTelephone());
		george.getPets().forEach(pet -> {
			org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet petJpa =
				new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet();
			petJpa.setId(pet.getId());
			petJpa.setName(pet.getName());
			petJpa.setBirthDate(pet.getBirthDate());
			pet.getVisits().forEach(visit -> {
				org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Visit visitJpa =
					new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Visit();
				visitJpa.setDate(visit.getDate());
				petJpa.addVisit(visitJpa);
			});
			georgeJpa.addPet(petJpa);
		});

		given(this.ownerMapper.toJpa(any(Owner.class))).willReturn(georgeJpa);
		given(this.ownerMapper.toDomain(any(org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner.class)))
			.willReturn(george);
	}

	@Test
	void testInitCreationForm() throws Exception {
		given(this.ownerMapper.toJpa(any())).willReturn(
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner());
		mockMvc.perform(get("/owners/new"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("owner"))
			.andExpect(view().name("owners/createOrUpdateOwnerForm"));
	}

	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc
			.perform(post("/owners/new").param("firstName", "Joe")
				.param("lastName", "Bloggs")
				.param("address", "123 Caramel Street")
				.param("city", "London")
				.param("telephone", "1316761638"))
			.andExpect(status().is3xxRedirection());
	}

	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc
			.perform(post("/owners/new").param("firstName", "Joe").param("lastName", "Bloggs").param("city", "London"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("owner"))
			.andExpect(model().attributeHasFieldErrors("owner", "address"))
			.andExpect(model().attributeHasFieldErrors("owner", "telephone"))
			.andExpect(view().name("owners/createOrUpdateOwnerForm"));
	}

	@Test
	void testInitFindForm() throws Exception {
		mockMvc.perform(get("/owners/find"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("owner"))
			.andExpect(view().name("owners/findOwners"));
	}

	@Test
	void testProcessFindFormSuccess() throws Exception {
		Page<Owner> tasks = new PageImpl<>(List.of(george(), new Owner()));
		when(this.ownerService.findByLastNameStartingWith(anyString(), any(Pageable.class))).thenReturn(tasks);
		mockMvc.perform(get("/owners?page=1")).andExpect(status().isOk()).andExpect(view().name("owners/ownersList"));
	}

	@Test
	void testProcessFindFormByLastName() throws Exception {
		Page<Owner> tasks = new PageImpl<>(List.of(george()));
		when(this.ownerService.findByLastNameStartingWith(eq("Franklin"), any(Pageable.class))).thenReturn(tasks);
		mockMvc.perform(get("/owners?page=1").param("lastName", "Franklin"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/" + TEST_OWNER_ID));
	}

	@Test
	void testProcessFindFormNoOwnersFound() throws Exception {
		Page<Owner> tasks = new PageImpl<>(List.of());
		when(this.ownerService.findByLastNameStartingWith(eq("Unknown Surname"), any(Pageable.class)))
			.thenReturn(tasks);
		mockMvc.perform(get("/owners?page=1").param("lastName", "Unknown Surname"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrors("owner", "lastName"))
			.andExpect(model().attributeHasFieldErrorCode("owner", "lastName", "notFound"))
			.andExpect(view().name("owners/findOwners"));

	}

	@Test
	void testInitUpdateOwnerForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/edit", TEST_OWNER_ID))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("owner"))
			.andExpect(model().attribute("owner", hasProperty("lastName", is("Franklin"))))
			.andExpect(model().attribute("owner", hasProperty("firstName", is("George"))))
			.andExpect(model().attribute("owner", hasProperty("address", is("110 W. Liberty St."))))
			.andExpect(model().attribute("owner", hasProperty("city", is("Madison"))))
			.andExpect(model().attribute("owner", hasProperty("telephone", is("6085551023"))))
			.andExpect(view().name("owners/createOrUpdateOwnerForm"));
	}

	@Test
	void testProcessUpdateOwnerFormSuccess() throws Exception {
		mockMvc
			.perform(post("/owners/{ownerId}/edit", TEST_OWNER_ID).param("firstName", "Joe")
				.param("lastName", "Bloggs")
				.param("address", "123 Caramel Street")
				.param("city", "London")
				.param("telephone", "1616291589"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@Test
	void testProcessUpdateOwnerFormUnchangedSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/edit", TEST_OWNER_ID))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@Test
	void testProcessUpdateOwnerFormHasErrors() throws Exception {
		mockMvc
			.perform(post("/owners/{ownerId}/edit", TEST_OWNER_ID).param("firstName", "Joe")
				.param("lastName", "Bloggs")
				.param("address", "")
				.param("telephone", ""))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("owner"))
			.andExpect(model().attributeHasFieldErrors("owner", "address"))
			.andExpect(model().attributeHasFieldErrors("owner", "telephone"))
			.andExpect(view().name("owners/createOrUpdateOwnerForm"));
	}

	@Test
	void testShowOwner() throws Exception {
		Owner domainOwner = george();
		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner jpaOwner =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner();
		jpaOwner.setId(domainOwner.getId());
		jpaOwner.setFirstName(domainOwner.getFirstName());
		jpaOwner.setLastName(domainOwner.getLastName());
		jpaOwner.setAddress(domainOwner.getAddress());
		jpaOwner.setCity(domainOwner.getCity());
		jpaOwner.setTelephone(domainOwner.getTelephone());

		// Add at least one pet so test passes
		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet petJpa =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet();
		petJpa.setName("Max");
		jpaOwner.addPet(petJpa);

		given(ownerService.findById(TEST_OWNER_ID)).willReturn(domainOwner);
		given(ownerMapper.toJpa(domainOwner)).willReturn(jpaOwner);

		mockMvc.perform(get("/owners/{ownerId}", TEST_OWNER_ID))
			.andExpect(status().isOk())
			.andExpect(model().attribute("owner", hasProperty("lastName", is("Franklin"))))
			.andExpect(model().attribute("owner", hasProperty("firstName", is("George"))))
			.andExpect(model().attribute("owner", hasProperty("pets", not(empty()))))
			.andExpect(view().name("owners/ownerDetails"));
	}


	@Test
	public void testProcessUpdateOwnerFormWithIdMismatch() throws Exception {
		int pathOwnerId = 1;

		Owner owner = new Owner();
		owner.setId(2);
		owner.setFirstName("John");
		owner.setLastName("Doe");
		owner.setAddress("Center Street");
		owner.setCity("New York");
		owner.setTelephone("0123456789");

		when(ownerService.findById(pathOwnerId)).thenReturn(owner);

		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner ownerJpa =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner();
		ownerJpa.setId(owner.getId());
		ownerJpa.setFirstName(owner.getFirstName());
		ownerJpa.setLastName(owner.getLastName());
		ownerJpa.setAddress(owner.getAddress());
		ownerJpa.setCity(owner.getCity());
		ownerJpa.setTelephone(owner.getTelephone());
		given(this.ownerMapper.toJpa(owner)).willReturn(ownerJpa);

		mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/edit", pathOwnerId).flashAttr("owner", ownerJpa))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/owners/" + pathOwnerId + "/edit"))
			.andExpect(flash().attributeExists("error"));
	}

}
