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

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.domain.usecase.OwnerUseCase;
import org.springframework.samples.petclinic.domain.entity.Owner;
import org.springframework.samples.petclinic.infrastructure.persistence.mapper.OwnerMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Wick Dynex
 */
@Controller
public class OwnerController {

	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";

	private static final int PAGE_SIZE = 5;

	private final OwnerUseCase ownerService;

	private final OwnerMapper ownerMapper;

	public OwnerController(OwnerUseCase ownerService, OwnerMapper ownerMapper) {
		this.ownerService = ownerService;
		this.ownerMapper = ownerMapper;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@ModelAttribute("owner")
	public org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner findOwner(
			@PathVariable(name = "ownerId", required = false) @Nullable Integer ownerId) {
		if (ownerId == null) {
			return new org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner();
		}
		Owner domainOwner = ownerService.findById(ownerId);
		return ownerMapper.toJpa(domainOwner);
	}

	@GetMapping("/owners/new")
	public String initCreationForm() {
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/owners/new")
	public String processCreationForm(
			@Valid org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner owner,
			BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("error", "There was an error in creating the owner.");
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}

		Owner domainOwner = ownerMapper.toDomain(owner);
		Owner savedOwner = ownerService.save(domainOwner);
		redirectAttributes.addFlashAttribute("message", "New Owner Created");
		return "redirect:/owners/" + savedOwner.getId();
	}

	@GetMapping("/owners/find")
	public String initFindForm() {
		return "owners/findOwners";
	}

	@GetMapping("/owners")
	public String processFindForm(
			@RequestParam(defaultValue = "1") int page,
			org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner owner,
			BindingResult result, Model model) {
		String lastName = getSearchLastName(owner);
		Page<Owner> ownersResults = findPaginatedForOwnersLastName(page, lastName);

		if (ownersResults.isEmpty()) {
			result.rejectValue("lastName", "notFound", "not found");
			return "owners/findOwners";
		}

		if (ownersResults.getTotalElements() == 1) {
			Owner foundOwner = ownersResults.iterator().next();
			return "redirect:/owners/" + foundOwner.getId();
		}

		return addPaginationModel(page, model, ownersResults);
	}

	@GetMapping("/owners/{ownerId}/edit")
	public String initUpdateOwnerForm() {
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/owners/{ownerId}/edit")
	public String processUpdateOwnerForm(
			@Valid org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner owner,
			BindingResult result, @PathVariable("ownerId") int ownerId, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("error", "There was an error in updating the owner.");
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}

		validateOwnerId(owner, ownerId, result, redirectAttributes);
		if (result.hasErrors()) {
			return "redirect:/owners/{ownerId}/edit";
		}

		owner.setId(ownerId);
		Owner domainOwner = ownerMapper.toDomain(owner);
		ownerService.save(domainOwner);
		redirectAttributes.addFlashAttribute("message", "Owner Values Updated");
		return "redirect:/owners/{ownerId}";
	}

	@GetMapping("/owners/{ownerId}")
	public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
		ModelAndView mav = new ModelAndView("owners/ownerDetails");
		Owner domainOwner = ownerService.findById(ownerId);
		org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner owner = ownerMapper
			.toJpa(domainOwner);
		mav.addObject(owner);
		return mav;
	}

	private String getSearchLastName(org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner owner) {
		String lastName = owner.getLastName();
		return lastName != null ? lastName : "";
	}

	private String addPaginationModel(int page, Model model, Page<Owner> paginated) {
		List<org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner> listOwners = paginated
			.getContent()
			.stream()
			.map(ownerMapper::toJpa)
			.collect(Collectors.toList());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listOwners", listOwners);
		return "owners/ownersList";
	}

	private Page<Owner> findPaginatedForOwnersLastName(int page, String lastName) {
		Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
		return ownerService.findByLastNameStartingWith(lastName, pageable);
	}

	private void validateOwnerId(org.springframework.samples.petclinic.infrastructure.persistence.entity.owner.Owner owner,
                                 int ownerId, BindingResult result, RedirectAttributes redirectAttributes) {
		Integer ownerIdFromForm = owner.getId();
		if (ownerIdFromForm != null && !ownerIdFromForm.equals(ownerId)) {
			result.rejectValue("id", "mismatch", "The owner ID in the form does not match the URL.");
			redirectAttributes.addFlashAttribute("error", "Owner ID mismatch. Please try again.");
		}
	}

}
