package com.example.practice1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.practice1.model.GenderTable;

@Repository
public interface GenderTableRepository extends JpaRepository<GenderTable, Integer> {
	Optional<GenderTable> findByGenderType(String genderType);
}
