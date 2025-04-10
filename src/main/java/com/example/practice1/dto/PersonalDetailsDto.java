package com.example.practice1.dto;

import java.time.LocalDate;
import java.util.Date;

import com.example.practice1.model.Gender;
import com.example.practice1.model.MaritalStatus;
import com.example.practice1.model.Nationality;
import com.example.practice1.model.Title;

public class PersonalDetailsDto {

//	private Title title;
	private String title;

	private String fullName;

	private String gender;
//	private Gender gender;
	private LocalDate dateOfBirth;

//	private Nationality nationality;
	private String nationality;

//	private MaritalStatus maritalStatus;
	private String maritalStatus;
	private String panNumber;
	private String emailId;
	private String mobileNumber;
	private String alternateMobileNumber;
	private String address;
	private String pincode;
	private String city;
	private String state;
	private Character status;

//	public Title getTitle() {
//		return title;
//	}
//
//	public void setTitle(Title title) {
//		this.title = title;
//	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

//	public Gender getGender() {
//		return gender;
//	}
//
//	public void setGender(Gender gender) {
//		this.gender = gender;
//	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

//	public Nationality getNationality() {
//		return nationality;
//	}
//
//	public void setNationality(Nationality nationality) {
//		this.nationality = nationality;
//	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

//	public MaritalStatus getMaritalStatus() {
//		return maritalStatus;
//	}
//
//	public void setMaritalStatus(MaritalStatus maritalStatus) {
//		this.maritalStatus = maritalStatus;
//	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getAlternateMobileNumber() {
		return alternateMobileNumber;
	}

	public void setAlternateMobileNumber(String alternateMobileNumber) {
		this.alternateMobileNumber = alternateMobileNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

}
