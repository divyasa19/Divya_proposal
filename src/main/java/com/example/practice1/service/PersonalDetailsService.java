package com.example.practice1.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.example.practice1.dto.PersonalDetailsDto;
import com.example.practice1.listing.ProposerListing;
import com.example.practice1.model.PersonalDetails;

import jakarta.servlet.http.HttpServletResponse;

public interface PersonalDetailsService {
	PersonalDetails savePersonalDetails(PersonalDetailsDto personalDetailsDto);

	List<PersonalDetails> getAllPersonalDetails();

	Optional<PersonalDetails> getPersonalDetailsById(Integer personalDetailsId);

	PersonalDetails updatePersonalDetails(Integer personalDetailsId, PersonalDetailsDto personalDetailsDto);

	void deletePersonalDetails(Integer personalDetailsId);

	List<PersonalDetails> getPersonalDetails(ProposerListing proposerListig);
	
	Integer totalRecords();
	
	 void exportPersonalDetailsToExcel(HttpServletResponse response) throws IOException;
	

}
