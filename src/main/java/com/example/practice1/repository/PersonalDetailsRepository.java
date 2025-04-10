package com.example.practice1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.practice1.model.PersonalDetails;

@Repository
public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails, Long> {

	Optional<PersonalDetails> findByPanNumber(String panNumber);

	Optional<PersonalDetails> findByMobileNumber(String mobileNumber);

//	Optional<PersonalDetails> findByIdAndStatus(Integer id, char c);

	Optional<PersonalDetails> findByEmailId(String emailId);

	Optional<PersonalDetails> findByPersonalDetailsId(Integer id);

	Optional<PersonalDetails> findByPersonalDetailsIdAndStatus(Integer personalDetailId, Character c);

	List<PersonalDetails> findByStatus(char c);

}
