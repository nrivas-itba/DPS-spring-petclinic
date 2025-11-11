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
import org.springframework.samples.petclinic.domain.usecase.VetUseCase;
import org.springframework.samples.petclinic.domain.Vets;
import org.springframework.samples.petclinic.domain.entity.Vet;
import org.springframework.samples.petclinic.infrastructure.persistence.mapper.VetMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class VetController {

	private static final int PAGE_SIZE = 5;

	private final VetUseCase vetService;

	private final VetMapper vetMapper;

	public VetController(VetUseCase vetService, VetMapper vetMapper) {
		this.vetService = vetService;
		this.vetMapper = vetMapper;
	}

	@GetMapping("/vets.html")
	public String showVetList(@RequestParam(defaultValue = "1") int page, Model model) {
		Vets vets = new Vets();
		Page<Vet> paginated = findPaginated(page);
		vets.getVetList().addAll(paginated.getContent().stream()
			.map(vetMapper::toJpa)
			.collect(Collectors.toList()));
		return addPaginationModel(page, paginated, model);
	}

	@GetMapping({ "/vets" })
	public @ResponseBody Vets showResourcesVetList() {
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findAll().stream()
			.map(vetMapper::toJpa)
			.collect(Collectors.toList()));
		return vets;
	}

	private String addPaginationModel(int page, Page<Vet> paginated, Model model) {
		List<org.springframework.samples.petclinic.infrastructure.persistence.entity.vet.Vet> listVets = paginated
			.getContent()
			.stream()
			.map(vetMapper::toJpa)
			.collect(Collectors.toList());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listVets", listVets);
		return "vets/vetList";
	}

	private Page<Vet> findPaginated(int page) {
		Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
		return vetService.findAll(pageable);
	}

}
