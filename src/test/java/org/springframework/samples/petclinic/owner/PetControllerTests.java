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

package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.application.service.interfaces.OwnerService;
import org.springframework.samples.petclinic.application.service.interfaces.PetService;
import org.springframework.samples.petclinic.application.service.interfaces.PetTypeService;
import org.springframework.samples.petclinic.domain.model.Owner;
import org.springframework.samples.petclinic.domain.model.Pet;
import org.springframework.samples.petclinic.domain.model.PetType;
import org.springframework.samples.petclinic.infrastructure.persistence.mapper.OwnerMapper;
import org.springframework.samples.petclinic.infrastructure.persistence.mapper.PetMapper;
import org.springframework.samples.petclinic.infrastructure.persistence.mapper.PetTypeMapper;
import org.springframework.samples.petclinic.web.controller.PetController;
import org.springframework.samples.petclinic.web.formatting.PetTypeFormatter;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Test class for the {@link PetController}
 *
 * @author Colin But
 * @author Wick Dynex
 */
@WebMvcTest(value = PetController.class,
		includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE))
@DisabledInNativeImage
@DisabledInAotMode
class PetControllerTests {

	private static final int TEST_OWNER_ID = 1;

	private static final int TEST_PET_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private OwnerService ownerService;

	@MockitoBean
	private PetService petService;

	@MockitoBean
	private PetTypeService petTypeService;

	@MockitoBean
	private OwnerMapper ownerMapper;

	@MockitoBean
	private PetMapper petMapper;

	@MockitoBean
	private PetTypeMapper petTypeMapper;

	@BeforeEach
	void setup() {
		PetType cat = new PetType();
		cat.setId(3);
		cat.setName("hamster");
		given(this.petTypeService.findAll()).willReturn(List.of(cat));

		org.springframework.samples.petclinic.infrastructure.persistence.entity.PetType catJpa =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.PetType();
		catJpa.setId(cat.getId());
		catJpa.setName(cat.getName());
		given(this.petTypeMapper.toJpa(any(PetType.class))).willReturn(catJpa);
		given(this.petTypeMapper.toDomain(any(org.springframework.samples.petclinic.infrastructure.persistence.entity.PetType.class)))
			.willReturn(cat);

		Owner owner = new Owner();
		owner.setId(TEST_OWNER_ID); // Asegurar que el owner de dominio tiene el ID
		Pet pet = new Pet();
		Pet dog = new Pet();
		owner.addPet(pet);
		owner.addPet(dog);
		pet.setId(TEST_PET_ID);
		dog.setId(TEST_PET_ID + 1);
		pet.setName("petty");
		dog.setName("doggy");
		given(this.ownerService.findById(TEST_OWNER_ID)).willReturn(owner);

		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner ownerJpa =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner();
		ownerJpa.setId(TEST_OWNER_ID); // Asegurar que el ownerJpa tiene el ID
		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet petJpa =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet();
		petJpa.setId(pet.getId());
		petJpa.setName(pet.getName());
		ownerJpa.addPet(petJpa);
		given(this.ownerMapper.toJpa(any(Owner.class))).willReturn(ownerJpa);

		// Actualizar este mock para que copie el ID del owner JPA al owner de dominio
		given(this.ownerMapper.toDomain(any(org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner.class)))
			.willAnswer(invocation -> {
				org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner jpaOwner = invocation.getArgument(0);
				Owner domainOwner = new Owner();
				domainOwner.setId(jpaOwner.getId() != null ? jpaOwner.getId() : TEST_OWNER_ID);
				domainOwner.setFirstName(jpaOwner.getFirstName());
				domainOwner.setLastName(jpaOwner.getLastName());
				domainOwner.setAddress(jpaOwner.getAddress());
				domainOwner.setCity(jpaOwner.getCity());
				domainOwner.setTelephone(jpaOwner.getTelephone());
				// Copiar las mascotas si existen
				jpaOwner.getPets().forEach(jpaPet -> {
					Pet domainPet = new Pet();
					domainPet.setId(jpaPet.getId());
					domainPet.setName(jpaPet.getName());
					domainPet.setBirthDate(jpaPet.getBirthDate());
					domainOwner.addPet(domainPet);
				});
				return domainOwner;
			});

		given(this.petMapper.toJpa(any(Pet.class))).willReturn(petJpa);

		// Actualizar este mock para que configure correctamente el type
		given(this.petMapper.toDomain(any(org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet.class)))
			.willAnswer(invocation -> {
				org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet jpaPet = invocation.getArgument(0);
				Pet domainPet = new Pet();
				domainPet.setId(jpaPet.getId());
				domainPet.setName(jpaPet.getName());
				domainPet.setBirthDate(jpaPet.getBirthDate());
				if (jpaPet.getType() != null) {
					domainPet.setType(cat); // Usar el cat que ya tenemos configurado
				}
				return domainPet;
			});

		given(this.petService.isPetNameUniqueForOwner(any(Owner.class), eq("petty"), any())).willReturn(false);
		given(this.petService.isPetNameUniqueForOwner(any(Owner.class), eq("Betty"), any())).willReturn(true);
		given(this.petService.isPetNameUniqueForOwner(any(Owner.class), anyString(), any())).willReturn(true);
		given(this.petService.isBirthDateValid(LocalDate.parse("2015-02-12"))).willReturn(true);
		given(this.petService.isBirthDateValid(any(LocalDate.class))).willReturn(true);
		given(this.petService.createPet(any(Owner.class), any(Pet.class))).willAnswer(invocation -> {
			Owner o = invocation.getArgument(0);
			Pet p = invocation.getArgument(1);
			o.addPet(p);
			return o;
		});
		given(this.petService.updatePet(any(Owner.class), any(Pet.class))).willAnswer(invocation -> {
			Owner o = invocation.getArgument(0);
			Pet p = invocation.getArgument(1);
			Pet existing = o.getPet(p.getId());
			if (existing != null) {
				existing.setName(p.getName());
				existing.setBirthDate(p.getBirthDate());
				existing.setType(p.getType());
			}
			return o;
		});
	}

	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/new", TEST_OWNER_ID))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdatePetForm"))
			.andExpect(model().attributeExists("pet"));
	}

	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc
			.perform(post("/owners/{ownerId}/pets/new", TEST_OWNER_ID).param("name", "Betty")
				.param("type", "hamster")
				.param("birthDate", "2015-02-12"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/1"));
	}

	@Nested
	class ProcessCreationFormHasErrors {

		@Test
		void testProcessCreationFormWithBlankName() throws Exception {
			mockMvc
				.perform(post("/owners/{ownerId}/pets/new", TEST_OWNER_ID).param("name", "\t \n")
					.param("birthDate", "2015-02-12"))
				.andExpect(model().attributeHasNoErrors("owner"))
				.andExpect(model().attributeHasErrors("pet"))
				.andExpect(model().attributeHasFieldErrors("pet", "name"))
				.andExpect(model().attributeHasFieldErrorCode("pet", "name", "required"))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdatePetForm"));
		}

		@Test
		void testProcessCreationFormWithDuplicateName() throws Exception
		{
			given(petService.isPetNameUniqueForOwner(any(Owner.class), eq("petty"), any()))
				.willReturn(false);
			mockMvc
				.perform(post("/owners/{ownerId}/pets/new", TEST_OWNER_ID)
						.param("name", "petty")
					.param("birthDate", "2015-02-12")
					.param("type", "hamster"))

				.andExpect(model().attributeHasNoErrors("owner"))
				.andExpect(model().attributeHasErrors("pet"))
				.andExpect(model().attributeHasFieldErrors("pet", "name"))
				.andExpect(model().attributeHasFieldErrorCode("pet", "name", "duplicate"))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdatePetForm"));
		}

		@Test
		void testProcessCreationFormWithMissingPetType() throws Exception {
			mockMvc
				.perform(post("/owners/{ownerId}/pets/new", TEST_OWNER_ID).param("name", "Betty")
					.param("birthDate", "2015-02-12"))
				.andExpect(model().attributeHasNoErrors("owner"))
				.andExpect(model().attributeHasErrors("pet"))
				.andExpect(model().attributeHasFieldErrors("pet", "type"))
				.andExpect(model().attributeHasFieldErrorCode("pet", "type", "required"))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdatePetForm"));
		}

		@Test
		void testProcessCreationFormWithInvalidBirthDate() throws Exception {
			LocalDate currentDate = LocalDate.now();
			String futureBirthDate = currentDate.plusMonths(1).toString();

			given(petService.isBirthDateValid(currentDate.plusMonths(1))).willReturn(false);

			mockMvc
				.perform(post("/owners/{ownerId}/pets/new", TEST_OWNER_ID).param("name", "Betty")
					.param("birthDate", futureBirthDate))
				.andExpect(model().attributeHasNoErrors("owner"))
				.andExpect(model().attributeHasErrors("pet"))
				.andExpect(model().attributeHasFieldErrors("pet", "birthDate"))
				.andExpect(model().attributeHasFieldErrorCode("pet", "birthDate", "typeMismatch.birthDate"))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdatePetForm"));
		}

		@Test
		void testInitUpdateForm() throws Exception {
			mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("pet"))
				.andExpect(view().name("pets/createOrUpdatePetForm"));
		}

	}

	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		mockMvc
			.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID).param("name", "Betty")
				.param("type", "hamster")
				.param("birthDate", "2015-02-12"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/1"));
	}

	@Nested
	class ProcessUpdateFormHasErrors {

		@Test
		void testProcessUpdateFormWithInvalidBirthDate() throws Exception {
			mockMvc
				.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID).param("name", " ")
					.param("birthDate", "2015/02/12"))
				.andExpect(model().attributeHasNoErrors("owner"))
				.andExpect(model().attributeHasErrors("pet"))
				.andExpect(model().attributeHasFieldErrors("pet", "birthDate"))
				.andExpect(model().attributeHasFieldErrorCode("pet", "birthDate", "typeMismatch"))
				.andExpect(view().name("pets/createOrUpdatePetForm"));
		}

		@Test
		void testProcessUpdateFormWithBlankName() throws Exception {
			mockMvc
				.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID).param("name", "  ")
					.param("birthDate", "2015-02-12"))
				.andExpect(model().attributeHasNoErrors("owner"))
				.andExpect(model().attributeHasErrors("pet"))
				.andExpect(model().attributeHasFieldErrors("pet", "name"))
				.andExpect(model().attributeHasFieldErrorCode("pet", "name", "required"))
				.andExpect(view().name("pets/createOrUpdatePetForm"));
		}

	}

}
