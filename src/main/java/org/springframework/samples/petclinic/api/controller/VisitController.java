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
package org.springframework.samples.petclinic.api.controller;

import java.util.Map;

import org.springframework.samples.petclinic.application.exception.PetNotFoundException;
import org.springframework.samples.petclinic.domain.usecase.OwnerUseCase;
import org.springframework.samples.petclinic.domain.usecase.VisitUseCase;
import org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Pet;
import org.springframework.samples.petclinic.domain.entity.Owner;
import org.springframework.samples.petclinic.domain.entity.Visit;
import org.springframework.samples.petclinic.infrastructure.persistence.mapper.OwnerMapper;
import org.springframework.samples.petclinic.infrastructure.persistence.mapper.PetMapper;
import org.springframework.samples.petclinic.infrastructure.persistence.mapper.VisitMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Dave Syer
 * @author Wick Dynex
 */
@Controller
public class VisitController {

	private final OwnerUseCase ownerService;

	private final VisitUseCase visitService;

	private final OwnerMapper ownerMapper;

	private final PetMapper petMapper;

	private final VisitMapper visitMapper;

	public VisitController(OwnerUseCase ownerService, VisitUseCase visitService, OwnerMapper ownerMapper,
						   PetMapper petMapper, VisitMapper visitMapper) {
		this.ownerService = ownerService;
		this.visitService = visitService;
		this.ownerMapper = ownerMapper;
		this.petMapper = petMapper;
		this.visitMapper = visitMapper;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	/**
	 * Called before each and every @RequestMapping annotated method. 2 goals: - Make sure
	 * we always have fresh data - Since we do not use the session scope, make sure that
	 * Pet object always has an id (Even though id is not part of the form fields)
	 * @param ownerId the owner ID
	 * @param petId the pet ID
	 * @param model the model
	 * @return Visit
	 */
	@ModelAttribute("visit")
	public org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Visit loadPetWithVisit(
			@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId, Map<String, Object> model) {
		Owner domainOwner = ownerService.findById(ownerId);
		org.springframework.samples.petclinic.domain.entity.Pet domainPet = findPetOrThrow(domainOwner, petId);

		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner owner = ownerMapper
			.toJpa(domainOwner);
		Pet pet = petMapper
			.toJpa(domainPet);

		model.put("pet", pet);
		model.put("owner", owner);

		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Visit visit =
			new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Visit();
		pet.addVisit(visit);
		return visit;
	}

	@GetMapping("/owners/{ownerId}/pets/{petId}/visits/new")
	public String initNewVisitForm() {
		return "pets/createOrUpdateVisitForm";
	}

	@PostMapping("/owners/{ownerId}/pets/{petId}/visits/new")
	public String processNewVisitForm(
			@ModelAttribute org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner owner,
			@PathVariable int petId,
			@Valid org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Visit visit,
			BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "pets/createOrUpdateVisitForm";
		}

		Owner domainOwner = ownerMapper.toDomain(owner);
		Visit domainVisit = visitMapper.toDomain(visit);
		visitService.createVisit(domainOwner, petId, domainVisit);
		redirectAttributes.addFlashAttribute("message", "Your visit has been booked");
		return "redirect:/owners/{ownerId}";
	}

	private org.springframework.samples.petclinic.domain.entity.Pet findPetOrThrow(Owner owner, int petId) {
		org.springframework.samples.petclinic.domain.entity.Pet pet = owner.getPet(petId);
		if (pet == null) {
			throw new PetNotFoundException(
					"Pet with id " + petId + " not found for owner with id " + owner.getId() + ".");
		}
		return pet;
	}

}
