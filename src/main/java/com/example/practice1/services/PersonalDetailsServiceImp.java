package com.example.practice1.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.practice1.dto.PersonalDetailsDto;
import com.example.practice1.models.PersonalDetails;
import com.example.practice1.repositories.PersonalDetailsRepository;

@Service
public class PersonalDetailsServiceImp implements PersonalDetailsService {

	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;

	@Override
	public PersonalDetails savePersonalDetails(PersonalDetailsDto personalDetailsDto) {

		if (personalDetailsDto.getFullName() == null || personalDetailsDto.getFullName().isEmpty()) {
			throw new IllegalArgumentException("Full Name is required.");
		}

		if (!personalDetailsDto.getFullName().matches("[a-zA-Z\\s]+")) {
			throw new IllegalArgumentException("Full Name should only contain letters and spaces.");
		}

		if (personalDetailsDto.getDateOfBirth() == null) {
			throw new IllegalArgumentException("Date of Birth is required.");
		}

//		if (personalDetailsDto.getPanNumber() != null
//				&& !personalDetailsDto.getPanNumber().matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
//			throw new IllegalArgumentException("Invalid PAN Number format.");
//		}
//		 
//		 if (personalDetailsDto.getPanNumber() != null) {
//		        Optional<PersonalDetails> existingPan = personalDetailsRepository.findByPanNumber(personalDetailsDto.getPanNumber());
//		        if (existingPan.isPresent()) {
//		            throw new IllegalArgumentException("PAN Number already exists.");
//		        }
//		    }

		

		
		if (personalDetailsDto.getPanNumber() != null) {
	        
	        if (!personalDetailsDto.getPanNumber().matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
	            throw new IllegalArgumentException("Invalid PAN Number format.");
	        }

	        
//	        Optional<PersonalDetails> existingPan = personalDetailsRepository.findByPanNumber(personalDetailsDto.getPanNumber());
//	        if (existingPan.isPresent()) {
//	            throw new IllegalArgumentException("PAN Number already exists.");
//	        }
	        
	        if (personalDetailsDto.getPanNumber() != null) {
			    List<PersonalDetails> existingPan = personalDetailsRepository.findByPanNumber(personalDetailsDto.getPanNumber());
			    if (!existingPan.isEmpty()) {
			        throw new IllegalArgumentException("PAN Number already exists.");
			    }
			}
	    }
		if (personalDetailsDto.getEmail() == null || personalDetailsDto.getEmail().isEmpty()) {
			throw new IllegalArgumentException("Email is required.");
		}

		if (!personalDetailsDto.getEmail()
				.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
			throw new IllegalArgumentException("Invalid email format.");
		}

		if (personalDetailsDto.getMobileNumber() == null || personalDetailsDto.getMobileNumber().isEmpty()) {
			throw new IllegalArgumentException("Mobile Number is required.");
		}

		if (!personalDetailsDto.getMobileNumber().matches("\\d{10}")) {
			throw new IllegalArgumentException("Mobile Number should be exactly 10 digits.");
		}

		if (personalDetailsDto.getAlternateMobileNumber() != null
				&& !personalDetailsDto.getAlternateMobileNumber().matches("\\d{10}")) {
			throw new IllegalArgumentException("Alternate Mobile Number should be exactly 10 digits if provided.");
		}

		if (personalDetailsDto.getAddress() == null || personalDetailsDto.getAddress().isEmpty()) {
			throw new IllegalArgumentException("Address is required.");
		}

		if (personalDetailsDto.getPincode() == null || personalDetailsDto.getPincode().isEmpty()) {
			throw new IllegalArgumentException("Pincode is required.");
		}

		if (!personalDetailsDto.getPincode().matches("\\d{6}")) {
			throw new IllegalArgumentException("Pincode should be exactly 6 digits.");
		}

		if (personalDetailsDto.getCity() == null || personalDetailsDto.getCity().isEmpty()) {
			throw new IllegalArgumentException("City is required.");
		}

		if (personalDetailsDto.getState() == null || personalDetailsDto.getState().isEmpty()) {
			throw new IllegalArgumentException("State is required.");
		}

		PersonalDetails pd = new PersonalDetails();
		pd.setTitle(personalDetailsDto.getTitle());
		pd.setFullName(personalDetailsDto.getFullName());
		pd.setGender(personalDetailsDto.getGender());
		pd.setDateOfBirth(personalDetailsDto.getDateOfBirth());
		pd.setNationality(personalDetailsDto.getNationality());
		pd.setMaritalStatus(personalDetailsDto.getMaritalStatus());
		pd.setPanNumber(personalDetailsDto.getPanNumber());
		pd.setEmail(personalDetailsDto.getEmail());
		pd.setMobileNumber(personalDetailsDto.getMobileNumber());
		pd.setAlternateMobileNumber(personalDetailsDto.getAlternateMobileNumber());
		pd.setAddress(personalDetailsDto.getAddress());
		pd.setPincode(personalDetailsDto.getPincode());
		pd.setCity(personalDetailsDto.getCity());
		pd.setState(personalDetailsDto.getState());
		pd.setStatus("Y");

		return personalDetailsRepository.save(pd);
	}

	@Override
	public List<PersonalDetails> getAllPersonalDetails() {
		List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();

		if (personalDetailsList.isEmpty()) {
			throw new IllegalArgumentException("No personal details found.");
		}

		return personalDetailsList;
	}

	@Override
	public Optional<PersonalDetails> getPersonalDetailsById(Long id) {
		Optional<PersonalDetails> personalDetails = personalDetailsRepository.findById(id);

		if (!personalDetails.isPresent()) {
			throw new IllegalArgumentException("Personal details not found for the given ID.");
		}

		return personalDetails;
	}

	@Override
	public PersonalDetails updatePersonalDetails(Long id, PersonalDetailsDto personalDetailsDto) {
		Optional<PersonalDetails> existingDetails = personalDetailsRepository.findById(id);

		if (existingDetails.isPresent()) {
			PersonalDetails pd = existingDetails.get();

			// Check if the status is 'N' (deleted)
			if ("N".equals(pd.getStatus())) {
				throw new IllegalArgumentException("The record is deleted and cannot be updated.");
			}

			// Validation checks
			if (personalDetailsDto.getFullName() != null && !personalDetailsDto.getFullName().matches("[a-zA-Z\\s]+")) {
				throw new IllegalArgumentException("Full Name should only contain letters and spaces.");
			}

			if (personalDetailsDto.getMobileNumber() != null
					&& !personalDetailsDto.getMobileNumber().matches("\\d{10}")) {
				throw new IllegalArgumentException("Mobile Number should be exactly 10 digits.");
			}

			if (personalDetailsDto.getEmail() != null && !personalDetailsDto.getEmail()
					.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
				throw new IllegalArgumentException("Invalid email format.");
			}

			if (personalDetailsDto.getPanNumber() != null
					&& !personalDetailsDto.getPanNumber().matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
				throw new IllegalArgumentException("Invalid PAN Number format.");
			}

			// Update fields, but ignore the status field from the DTO
			pd.setTitle(personalDetailsDto.getTitle() != null ? personalDetailsDto.getTitle() : pd.getTitle());
			pd.setFullName(
					personalDetailsDto.getFullName() != null ? personalDetailsDto.getFullName() : pd.getFullName());
			pd.setGender(personalDetailsDto.getGender() != null ? personalDetailsDto.getGender() : pd.getGender());
			pd.setDateOfBirth(personalDetailsDto.getDateOfBirth() != null ? personalDetailsDto.getDateOfBirth()
					: pd.getDateOfBirth());
			pd.setNationality(personalDetailsDto.getNationality() != null ? personalDetailsDto.getNationality()
					: pd.getNationality());
			pd.setMaritalStatus(personalDetailsDto.getMaritalStatus() != null ? personalDetailsDto.getMaritalStatus()
					: pd.getMaritalStatus());
			pd.setPanNumber(
					personalDetailsDto.getPanNumber() != null ? personalDetailsDto.getPanNumber() : pd.getPanNumber());
			pd.setEmail(personalDetailsDto.getEmail() != null ? personalDetailsDto.getEmail() : pd.getEmail());
			pd.setMobileNumber(personalDetailsDto.getMobileNumber() != null ? personalDetailsDto.getMobileNumber()
					: pd.getMobileNumber());
			pd.setAlternateMobileNumber(personalDetailsDto.getAlternateMobileNumber() != null
					? personalDetailsDto.getAlternateMobileNumber()
					: pd.getAlternateMobileNumber());
			pd.setAddress(personalDetailsDto.getAddress() != null ? personalDetailsDto.getAddress() : pd.getAddress());
			pd.setPincode(personalDetailsDto.getPincode() != null ? personalDetailsDto.getPincode() : pd.getPincode());
			pd.setCity(personalDetailsDto.getCity() != null ? personalDetailsDto.getCity() : pd.getCity());
			pd.setState(personalDetailsDto.getState() != null ? personalDetailsDto.getState() : pd.getState());

			
			pd.setStatus(pd.getStatus());

			return personalDetailsRepository.save(pd);
		} else {
			throw new IllegalArgumentException("Personal details not found for the given ID.");
		}
	}

	@Override
	public void deletePersonalDetails(Long id) {
		Optional<PersonalDetails> existingDetails = personalDetailsRepository.findById(id);

		if (existingDetails.isPresent()) {
			PersonalDetails pd = existingDetails.get();
			pd.setStatus("N");
			personalDetailsRepository.save(pd);
		} else {
			throw new IllegalArgumentException("Personal details not found for the given ID.");
		}
	}

}
