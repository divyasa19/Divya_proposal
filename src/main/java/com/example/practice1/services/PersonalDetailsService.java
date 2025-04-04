package com.example.practice1.services;

import java.util.List;
import java.util.Optional;

import com.example.practice1.dto.PersonalDetailsDto;
import com.example.practice1.models.PersonalDetails;

public interface PersonalDetailsService {
	PersonalDetails savePersonalDetails(PersonalDetailsDto personalDetailsDto);

	List<PersonalDetails> getAllPersonalDetails();

	Optional<PersonalDetails> getPersonalDetailsById(Long id);

	PersonalDetails updatePersonalDetails(Long id, PersonalDetailsDto personalDetailsDto);

	void deletePersonalDetails(Long id);
}
