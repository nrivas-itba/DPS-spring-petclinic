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

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.samples.petclinic.application.service.OwnerService;
import org.springframework.samples.petclinic.application.service.PetService;
import org.springframework.samples.petclinic.application.service.PetTypeService;
import org.springframework.samples.petclinic.domain.model.Owner;
import org.springframework.samples.petclinic.domain.model.Pet;
import org.springframework.samples.petclinic.infrastructure.persistence.mapper.OwnerMapper;
import org.springframework.samples.petclinic.infrastructure.persistence.mapper.PetMapper;
import org.springframework.samples.petclinic.infrastructure.persistence.mapper.PetTypeMapper;
import org.springframework.samples.petclinic.web.validation.PetValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Wick Dynex
 */
@Controller
@RequestMapping("/owners/{ownerId}")
public class PetController {

	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

	private final OwnerService ownerService;

	private final PetService petService;

	private final PetTypeService petTypeService;

	private final OwnerMapper ownerMapper;

	private final PetMapper petMapper;

	private final PetTypeMapper petTypeMapper;

	public PetController(OwnerService ownerService, PetService petService, PetTypeService petTypeService,
			OwnerMapper ownerMapper, PetMapper petMapper, PetTypeMapper petTypeMapper) {
		this.ownerService = ownerService;
		this.petService = petService;
		this.petTypeService = petTypeService;
		this.ownerMapper = ownerMapper;
		this.petMapper = petMapper;
		this.petTypeMapper = petTypeMapper;
	}

	@ModelAttribute("types")
	public Collection<org.springframework.samples.petclinic.infrastructure.persistence.entity.PetType> populatePetTypes() {
		return petTypeService.findAll().stream()
			.map(petTypeMapper::toJpa)
			.collect(Collectors.toList());
	}

	@ModelAttribute("owner")
	public org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner findOwner(
			@PathVariable("ownerId") int ownerId) {
		Owner domainOwner = ownerService.findById(ownerId);
		return ownerMapper.toJpa(domainOwner);
	}

	@ModelAttribute("pet")
	public org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet findPet(
			@PathVariable("ownerId") int ownerId,
			@PathVariable(name = "petId", required = false) @Nullable Integer petId) {

		if (petId == null) {
			return new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet();
		}

		Owner domainOwner = ownerService.findById(ownerId);
		Pet domainPet = domainOwner.getPet(petId);
		return domainPet != null ? petMapper.toJpa(domainPet) : null;
	}

	@InitBinder("owner")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("pet")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new PetValidator());
	}

	@GetMapping("/pets/new")
	public String initCreationForm(
			org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner owner,
			ModelMap model) {
		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet pet =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet();
		owner.addPet(pet);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/pets/new")
	public String processCreationForm(
		@ModelAttribute("owner") org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner owner,
		@ModelAttribute("pet") @Valid org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet pet,
		BindingResult result, RedirectAttributes redirectAttributes) {

		Owner domainOwner = ownerMapper.toDomain(owner);
		Pet domainPet = petMapper.toDomain(pet);

		validatePetNameUniqueness(domainOwner, domainPet, result);
		validateBirthDate(domainPet, result);

		if (result.hasErrors()) {
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}

		petService.createPet(domainOwner, domainPet);
		redirectAttributes.addFlashAttribute("message", "New Pet has been Added");
		return String.format("redirect:/owners/%s", owner.getId());
	}

	@GetMapping("/pets/{petId}/edit")
	public String initUpdateForm() {
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/pets/{petId}/edit")
	public String processUpdateForm(
		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner owner,
		@Valid org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet pet,
		BindingResult result, RedirectAttributes redirectAttributes) {

		Owner domainOwner = ownerMapper.toDomain(owner);
		Pet domainPet = petMapper.toDomain(pet);

		validatePetNameUniqueness(domainOwner, domainPet, result);
		validateBirthDate(domainPet, result);

		if (result.hasErrors()) {
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}

		petService.updatePet(domainOwner, domainPet);
		redirectAttributes.addFlashAttribute("message", "Pet details has been edited");
		return String.format("redirect:/owners/%s", owner.getId());
	}

	private void validatePetNameUniqueness(Owner owner, Pet pet, BindingResult result) {
		String petName = pet.getName();
		Integer petId = pet.getId();
		if (!petService.isPetNameUniqueForOwner(owner, petName, petId)) {
			result.rejectValue("name", "duplicate", "already exists");
		}
	}

	private void validateBirthDate(Pet pet, BindingResult result) {
		if (!petService.isBirthDateValid(pet.getBirthDate())) {
			result.rejectValue("birthDate", "typeMismatch.birthDate");
		}
	}

}
