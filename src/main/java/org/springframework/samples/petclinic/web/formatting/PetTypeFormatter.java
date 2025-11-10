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
package org.springframework.samples.petclinic.web.formatting;

import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.application.usecases.interfaces.PetTypeUseCase;
import org.springframework.samples.petclinic.infrastructure.persistence.entity.PetType;
import org.springframework.samples.petclinic.infrastructure.persistence.mapper.PetTypeMapper;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;
import java.util.Objects;

/**
 * Instructs Spring MVC on how to parse and print elements of type 'PetType'. Starting
 * from Spring 3.0, Formatters have come as an improvement in comparison to legacy
 * PropertyEditors. See the following links for more details: - The Spring ref doc:
 * https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/core.html#format
 *
 * @author Mark Fisher
 * @author Juergen Hoeller
 * @author Michael Isvy
 */
@Component
public class PetTypeFormatter implements Formatter<PetType> {

	private final PetTypeUseCase petTypeService;

	private final PetTypeMapper petTypeMapper;

	public PetTypeFormatter(PetTypeUseCase petTypeService, PetTypeMapper petTypeMapper) {
		this.petTypeService = petTypeService;
		this.petTypeMapper = petTypeMapper;
	}

	@Override
	public String print(PetType petType, Locale locale) {
		String name = petType.getName();
		return (name != null) ? name : "<null>";
	}

	@Override
	public PetType parse(String text, Locale locale) throws ParseException {
		return this.petTypeService.findAll().stream()
			.filter(type -> Objects.equals(type.getName(), text))
			.findFirst()
			.map(petTypeMapper::toJpa)
			.orElseThrow(() -> new ParseException("type not found: " + text, 0));
	}

}
