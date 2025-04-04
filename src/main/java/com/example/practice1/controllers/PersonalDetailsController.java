package com.example.practice1.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.practice1.dto.PersonalDetailsDto;
import com.example.practice1.models.PersonalDetails;
import com.example.practice1.services.PersonalDetailsService;

@RestController
@RequestMapping("/api/personal-details")
public class PersonalDetailsController {

	@Autowired
	private PersonalDetailsService personalDetailsService;

	@PostMapping
	public ResponseEntity<PersonalDetails> createPersonalDetails(@RequestBody PersonalDetailsDto personalDetailsDto) {
		try {
			PersonalDetails createdDetails = personalDetailsService.savePersonalDetails(personalDetailsDto);
			return new ResponseEntity<>(createdDetails, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping
	public List<PersonalDetails> getAllPersonalDetails() {
		return personalDetailsService.getAllPersonalDetails();
	}

	@GetMapping("/{id}")
	public ResponseEntity<PersonalDetails> getPersonalDetailsById(@PathVariable Long id) {
		Optional<PersonalDetails> details = personalDetailsService.getPersonalDetailsById(id);
		if (details.isPresent()) {
			return new ResponseEntity<>(details.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<PersonalDetails> updatePersonalDetails(@PathVariable Long id,
			@RequestBody PersonalDetailsDto personalDetailsDto) {
		PersonalDetails updatedDetails = personalDetailsService.updatePersonalDetails(id, personalDetailsDto);
		if (updatedDetails != null) {
			return new ResponseEntity<>(updatedDetails, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePersonalDetails(@PathVariable Long id) {
		personalDetailsService.deletePersonalDetails(id);
		return new ResponseEntity<>("PersonalDetails with ID " + id + " has been marked as deleted.",
				HttpStatus.NO_CONTENT);
	}
}
