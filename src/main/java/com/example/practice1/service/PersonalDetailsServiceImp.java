package com.example.practice1.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.practice1.dto.PersonalDetailsDto;
import com.example.practice1.listing.ProposerListing;
import com.example.practice1.listing.SearchFilter;
import com.example.practice1.model.Gender;
import com.example.practice1.model.GenderTable;
import com.example.practice1.model.MaritalStatus;
import com.example.practice1.model.Nationality;
import com.example.practice1.model.PersonalDetails;
import com.example.practice1.model.Title;
import com.example.practice1.repository.GenderTableRepository;
import com.example.practice1.repository.PersonalDetailsRepository;
import com.example.practice1.repository.ResponseExcelRepository;
import com.example.practice1.response.ResponseExcel;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class PersonalDetailsServiceImp implements PersonalDetailsService {

	@Autowired
	private GenderTableRepository genderTableRepository;

	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;

//	Integer totalRecord = 0;
	Integer totalRecord;
	
	Integer failedRecord;

	@Autowired
	private ResponseExcelRepository responseExcelRepository;

	@Override
	public Integer totalRecords() {

		return totalRecord;
	}
	
	@Override
	public Integer failedRecords() {

		return failedRecord;
	}

	@Override
	public PersonalDetails savePersonalDetails(PersonalDetailsDto personalDetailsDto) {
		String fullName = personalDetailsDto.getFullName();
		LocalDate date = personalDetailsDto.getDateOfBirth();
		String emailId = personalDetailsDto.getEmailId();
		String mobileNumber = personalDetailsDto.getMobileNumber();
		String alternateMobileNumber = personalDetailsDto.getAlternateMobileNumber();
		String address = personalDetailsDto.getAddress();
		String pincode = personalDetailsDto.getPincode();
		String city = personalDetailsDto.getCity();
		String state = personalDetailsDto.getState();
		String panNumber = personalDetailsDto.getPanNumber();

		PersonalDetails pd = new PersonalDetails();
		// validation for title enum
		String title = personalDetailsDto.getTitle();
//		Title title = personalDetailsDto.getTitle();
		if (title == null) {
			throw new IllegalArgumentException("Title is required.");
		}
		try {
			pd.setTitle(Title.valueOf(title.toString().toUpperCase()));

		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid title value: " + title.toString());
		}

		// -------------

		if (fullName == null || fullName.isEmpty() || !fullName.matches("[a-zA-Z\\s]+")) {
			throw new IllegalArgumentException("Full Name is required and should only contain letters and spaces.");
		}

		// validation for gender enum
		String gender = personalDetailsDto.getGender();
//		Gender gender = personalDetailsDto.getGender();
		if (gender == null) {
			throw new IllegalArgumentException("Gender is required.");
		}

		try {
			pd.setGender(Gender.valueOf(gender.toString().toUpperCase()));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid gender value: " + gender.toString());
		}
		// -----------

		if (date == null) {
			throw new IllegalArgumentException("Date of Birth is required.");
		}

		// validation for nationality
		String nationalityStr = personalDetailsDto.getNationality();

		if (nationalityStr == null || nationalityStr.isEmpty()) {
			throw new IllegalArgumentException("Nationality is required.");
		}

		try {
			pd.setNationality(Nationality.valueOf(nationalityStr.toUpperCase()));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid Nationality value: " + nationalityStr);
		}
		// ----------

		// validation for enum
		String maritalStatusStr = personalDetailsDto.getMaritalStatus();

		if (maritalStatusStr == null || maritalStatusStr.isEmpty()) {
			throw new IllegalArgumentException("Marital Status is required.");
		}

		try {
			pd.setMaritalStatus(MaritalStatus.valueOf(maritalStatusStr.toUpperCase()));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid Marital Status value: " + maritalStatusStr);
		}
		// -----------

		if (panNumber == null || panNumber.isEmpty() || !panNumber.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
			throw new IllegalArgumentException("PAN Number is required and should be in the correct format.");
		}

		personalDetailsRepository.findByPanNumber(panNumber).ifPresent(pan -> {
			throw new IllegalArgumentException("PAN Number already exists.");
		});

		if (emailId == null || emailId.isEmpty() || !emailId
				.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
			throw new IllegalArgumentException("Email is required and should be in the correct format.");
		}

		personalDetailsRepository.findByEmailId(emailId).ifPresent(email -> {
			throw new IllegalArgumentException("Email already exists.");
		});

		if (mobileNumber == null || mobileNumber.isEmpty() || !mobileNumber.matches("[789]\\d{9}")) {
			throw new IllegalArgumentException("Mobile Number is required and should be exactly 10 digits.");
		}

		personalDetailsRepository.findByMobileNumber(mobileNumber).ifPresent(mobile -> {
			throw new IllegalArgumentException("Mobile Number already exists.");
		});

		if (alternateMobileNumber != null && !alternateMobileNumber.isEmpty()) {
			if (!alternateMobileNumber.matches("\\d{10}")) {
				throw new IllegalArgumentException("Alternate Mobile Number should be exactly 10 digits if provided.");
			}
			if (alternateMobileNumber.equals(mobileNumber)) {
				throw new IllegalArgumentException("Alternate Mobile Number cannot be the same as Mobile Number.");
			}
		} else {
			throw new IllegalArgumentException("Alternate Mobile Number is required.");
		}

		if (address == null || address.isEmpty()) {
			throw new IllegalArgumentException("Address is required.");
		}

		if (pincode == null || pincode.isEmpty() || !pincode.matches("\\d{6}")) {
			throw new IllegalArgumentException("Pincode is required and should be exactly 6 digits.");
		}

		if (city == null || city.isEmpty() || !city.matches("[a-zA-Z ]+")) {
			throw new IllegalArgumentException("City is required and should contain letters.");
		}

		if (state == null || state.isEmpty() || !state.matches("[a-zA-Z ]+")) {
			throw new IllegalArgumentException("State is required and should contain letters.");
		}

		String genderType = personalDetailsDto.getGender();
		if (genderType != null && !genderType.isEmpty()) {
			Optional<GenderTable> genderTableOptional = genderTableRepository.findByGenderType(genderType);
			if (genderTableOptional.isPresent()) {
				pd.setGenderId(genderTableOptional.get().getGenderId());

			} else {
				throw new IllegalArgumentException("Invalid gender value provided");
			}
		} else {
			throw new IllegalArgumentException("gender cannot be null or empty");
		}

//		pd.setTitle(personalDetailsDto.getTitle());
		pd.setFullName(fullName);
//		pd.setGender(null);
//		pd.setGender(personalDetailsDto.getGender());
		pd.setDateOfBirth(date);
//		pd.setNationality(personalDetailsDto.getNationality());
//		pd.setMaritalStatus(personalDetailsDto.getMaritalStatus());
		pd.setPanNumber(panNumber);
		pd.setEmailId(emailId);
		pd.setMobileNumber(mobileNumber);
		pd.setAlternateMobileNumber(alternateMobileNumber);
		pd.setAddress(address);
		pd.setPincode(pincode);
		pd.setCity(city);
		pd.setState(state);
		pd.setStatus('Y');

		return personalDetailsRepository.save(pd);

	}

	@Override
	public List<PersonalDetails> getAllPersonalDetails() {
//		List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();
//		if (personalDetailsList.isEmpty()) {
//			throw new IllegalArgumentException("No personal details found.");
//		}
		List<PersonalDetails> personalDetailsList = personalDetailsRepository.findByStatus('Y');

		if (personalDetailsList == null || personalDetailsList.isEmpty()) {
			throw new IllegalArgumentException("No personal details found.");
		}

		return personalDetailsList;
	}

	@Override
	public Optional<PersonalDetails> getPersonalDetailsById(Integer personalDetailsId) {

		if (personalDetailsId == null || personalDetailsId <= 0) {
			throw new IllegalArgumentException("Invalid ID provided.");
		}

		Optional<PersonalDetails> personalDetails = personalDetailsRepository
				.findByPersonalDetailsIdAndStatus(personalDetailsId, 'Y');

		if (!personalDetails.isPresent()) {
			throw new IllegalArgumentException("Personal details not found.");
		}

		return personalDetails;

//		Optional<PersonalDetails> personalDetails = personalDetailsRepository
//				.findByPersonalDetailsId(personalDetailsId);
//		if (!personalDetails.isPresent()) {
//			throw new IllegalArgumentException("Personal details not found for the given ID.");
//		}

	}

	@Override
	public PersonalDetails updatePersonalDetails(Integer personalDetailId, PersonalDetailsDto personalDetailsDto) {

//		if (existingDetails == null && existingDetails.isEmpty()) {
//			throw new IllegalArgumentException("The record is deleted and cannot be updated.");
//		}

////		if (existingDetails.isPresent()) {
//		PersonalDetails pd = existingDetails.get();
////			if ('N'.equals(pd.getStatus())) {
////				throw new IllegalArgumentException("The record is deleted and cannot be updated.");
////			}
//

		if (personalDetailId == null || personalDetailId <= 0) {
			throw new IllegalArgumentException("Invalid ID provided.");
		}

		Optional<PersonalDetails> existingDetails = personalDetailsRepository
				.findByPersonalDetailsIdAndStatus(personalDetailId, 'Y');

		if (!existingDetails.isPresent()) {
			throw new IllegalArgumentException("Personal details not found for the given ID.");
		}

//		Optional<PersonalDetails> existingDetails = personalDetailsRepository
//				.findByPersonalDetailsIdAndStatus(personalDetailId, 'Y');
//
//		if (existingDetails == null || existingDetails.isEmpty()) {
//			throw new IllegalArgumentException("The record is not present ");
//		}

		String fullName = personalDetailsDto.getFullName();
		LocalDate date = personalDetailsDto.getDateOfBirth();
		String emailId = personalDetailsDto.getEmailId();
		String mobileNumber = personalDetailsDto.getMobileNumber();
		String alternateMobileNumber = personalDetailsDto.getAlternateMobileNumber();
		String address = personalDetailsDto.getAddress();
		String pincode = personalDetailsDto.getPincode();
		String city = personalDetailsDto.getCity();
		String state = personalDetailsDto.getState();
		String panNumber = personalDetailsDto.getPanNumber();

		PersonalDetails pd = existingDetails.get();

		// validation for title enum
		String title = personalDetailsDto.getTitle();
		if (title == null) {
			throw new IllegalArgumentException("Title is required.");
		}
		try {
			pd.setTitle(Title.valueOf(title.toString().toUpperCase()));

		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid title value: " + title.toString());
		}

		// -------------

		if (fullName == null || fullName.isEmpty() || !fullName.matches("[a-zA-Z\\s]+")) {
			throw new IllegalArgumentException("Full Name is required and should only contain letters and spaces.");
		}

		// validation for gender enum
		String gender = personalDetailsDto.getGender();
		if (gender == null) {
			throw new IllegalArgumentException("Gender is required.");
		}

		try {
			pd.setGender(Gender.valueOf(gender.toString().toUpperCase()));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid gender value: " + gender.toString());
		}
		// -----------

		if (date == null) {
			throw new IllegalArgumentException("Date of Birth is required.");
		}

		// validation for nationality
		String nationalityStr = personalDetailsDto.getNationality();

		if (nationalityStr == null || nationalityStr.isEmpty()) {
			throw new IllegalArgumentException("Nationality is required.");
		}

		try {
			pd.setNationality(Nationality.valueOf(nationalityStr.toUpperCase()));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid Nationality value: " + nationalityStr);
		}
		// ----------

		// validation for enum
		String maritalStatusStr = personalDetailsDto.getMaritalStatus();

		if (maritalStatusStr == null || maritalStatusStr.isEmpty()) {
			throw new IllegalArgumentException("Marital Status is required.");
		}

		try {
			pd.setMaritalStatus(MaritalStatus.valueOf(maritalStatusStr.toUpperCase()));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid Marital Status value: " + maritalStatusStr);
		}
		// -----------

		if (panNumber == null || panNumber.isEmpty() || !panNumber.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
			throw new IllegalArgumentException("PAN Number is required and should be in the correct format.");
		}

		if (emailId == null || emailId.isEmpty() || !emailId
				.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
			throw new IllegalArgumentException("Email is required and should be in the correct format.");
		}

		if (mobileNumber == null || mobileNumber.isEmpty() || !mobileNumber.matches("[789]\\d{9}")) {
			throw new IllegalArgumentException("Mobile Number is required and should be exactly 10 digits.");
		}

		if (alternateMobileNumber != null && !alternateMobileNumber.isEmpty()) {
			if (!alternateMobileNumber.matches("\\d{10}")) {
				throw new IllegalArgumentException("Alternate Mobile Number should be exactly 10 digits if provided.");
			}
			if (alternateMobileNumber.equals(mobileNumber)) {
				throw new IllegalArgumentException("Alternate Mobile Number cannot be the same as Mobile Number.");
			}
		} else {
			throw new IllegalArgumentException("Alternate Mobile Number is required.");
		}

		if (address == null || address.isEmpty()) {
			throw new IllegalArgumentException("Address is required.");
		}

		if (pincode == null || pincode.isEmpty() || !pincode.matches("\\d{6}")) {
			throw new IllegalArgumentException("Pincode is required and should be exactly 6 digits.");
		}

		if (city == null || city.isEmpty() || !city.matches("[a-zA-Z ]+")) {
			throw new IllegalArgumentException("City is required and should contain letters.");
		}

		if (state == null || state.isEmpty() || !state.matches("[a-zA-Z ]+")) {
			throw new IllegalArgumentException("State is required and should contain letters.");
		}

		String genderType = personalDetailsDto.getGender();
		if (genderType != null && !genderType.isEmpty()) {
			Optional<GenderTable> genderTableOptional = genderTableRepository.findByGenderType(genderType);
			if (genderTableOptional.isPresent()) {
				pd.setGenderId(genderTableOptional.get().getGenderId());

			} else {
				throw new IllegalArgumentException("Invalid gender value provided");
			}
		} else {
			throw new IllegalArgumentException("gender cannot be null or empty");
		}

//		PersonalDetails pd = new PersonalDetails();

//		pd.setTitle(personalDetailsDto.getTitle());
		pd.setFullName(personalDetailsDto.getFullName());
//		pd.setGender(personalDetailsDto.getGender());
		pd.setDateOfBirth(personalDetailsDto.getDateOfBirth());
//		pd.setNationality(personalDetailsDto.getNationality());
//		pd.setMaritalStatus(personalDetailsDto.getMaritalStatus());
		pd.setPanNumber(personalDetailsDto.getPanNumber());
		pd.setEmailId(personalDetailsDto.getEmailId());
		pd.setMobileNumber(personalDetailsDto.getMobileNumber());
		pd.setAlternateMobileNumber(personalDetailsDto.getAlternateMobileNumber());
		pd.setAddress(personalDetailsDto.getAddress());
		pd.setPincode(personalDetailsDto.getPincode());
		pd.setCity(personalDetailsDto.getCity());
		pd.setState(personalDetailsDto.getState());
//			pd.setStatus(pd.getStatus());

		return personalDetailsRepository.save(pd);

	}

	@Override
	public void deletePersonalDetails(Integer personalDetailsId) {

		if (personalDetailsId == null || personalDetailsId <= 0) {
			throw new IllegalArgumentException("Invalid ID provided.");
		}

		Optional<PersonalDetails> personalDetails = personalDetailsRepository
				.findByPersonalDetailsId(personalDetailsId);
		if (personalDetails.isPresent()) {
			PersonalDetails pd = personalDetails.get();
			pd.setStatus('N');
			personalDetailsRepository.save(pd);

		} else {
			throw new IllegalArgumentException("Personal details not found for the given ID.");
		}

	}

	@Autowired
	private EntityManager entityManager;

	@Override
	public List<PersonalDetails> getPersonalDetails(ProposerListing proposerListing) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<PersonalDetails> criteriaQuery = criteriaBuilder.createQuery(PersonalDetails.class);
		Root<PersonalDetails> root = criteriaQuery.from(PersonalDetails.class);
		criteriaQuery.select(root);
//		SearchFilter[] searchFilters = proposerListing.getSearchFilters();
//		if (searchFilters != null) {
//			for (SearchFilter filter : searchFilters) {
//				if (filter.getFullName() != null && !filter.getFullName().isEmpty()) {
//					criteriaQuery.where(criteriaBuilder.like(root.get("fullName"), "%" + filter.getFullName() + "%"));
//				}
//				if (filter.getEmailId() != null && !filter.getEmailId().isEmpty()) {
//					criteriaQuery.where(criteriaBuilder.like(root.get("emailId"), "%" + filter.getEmailId() + "%"));
//				}
//				if (filter.getCity() != null && !filter.getCity().isEmpty()) {
//					criteriaQuery.where(criteriaBuilder.like(root.get("city"), "%" + filter.getCity() + "%"));
//				}
//				if (filter.getStatus() != null) {
//					criteriaQuery.where(criteriaBuilder.equal(root.get("status"), filter.getStatus()));
//				}
//
//			}
//		}
		List<Predicate> predicates = new ArrayList<>();
//		predicates.add(criteriaBuilder.equal(root.get("status"), 'Y'));

//		List<SearchFilter> searchFilters = proposerListing.getSearchFiltersList();

//		if (searchFilters != null && !searchFilters.isEmpty()) {
//			for (SearchFilter filter : searchFilters) {
//				String fullName = filter.getFullName();
//				String emailId = filter.getEmailId();
//				String city = filter.getCity();
//				Character status = filter.getStatus();
//
//				if (fullName != null && !fullName.isEmpty()) {
//					predicates.add(criteriaBuilder.equal(root.get("fullName"), fullName));
//				}
//
//				if (emailId != null && !emailId.isEmpty()) {
//					predicates.add(criteriaBuilder.equal(root.get("emailId"), emailId));
//				}
//				if (city != null && !city.isEmpty()) {
//					predicates.add(criteriaBuilder.equal(root.get("city"), city));
//				}
//				if (status != null) {
//					predicates.add(criteriaBuilder.equal(root.get("status"), status));
//				}else {
//					predicates.add(criteriaBuilder.equal(root.get("status"), 'Y'));
//
//				}
//			}
//		}

		List<SearchFilter> searchFilters = proposerListing.getSearchFiltersList();

		if (searchFilters != null && !searchFilters.isEmpty()) {
			SearchFilter filter = searchFilters.get(0);

			String fullName = filter.getFullName();
			String emailId = filter.getEmailId();
			String city = filter.getCity();
			Character status = filter.getStatus();

			if (fullName != null && !fullName.isEmpty()) {
				predicates.add(criteriaBuilder.equal(root.get("fullName"), fullName));
			}

			if (emailId != null && !emailId.isEmpty()) {
				predicates.add(criteriaBuilder.equal(root.get("emailId"), emailId));
			}
			if (city != null && !city.isEmpty()) {
				predicates.add(criteriaBuilder.equal(root.get("city"), city));
			}
			if (status != null) {
				predicates.add(criteriaBuilder.equal(root.get("status"), status));
			} else {

				predicates.add(criteriaBuilder.equal(root.get("status"), 'Y'));
			}
		}

		if (!predicates.isEmpty()) {
			criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
		}

//		if (proposerListing.getPage() >= 0 && proposerListing.getSize() >= 0) {
//			if (proposerListing.getSortBy() == null || proposerListing.getSortBy().isEmpty()) {
//				proposerListing.setSortBy("id");
//			}
//			if (proposerListing.getSortOrder() == null || proposerListing.getSortOrder().isEmpty()) {
//				proposerListing.setSortOrder("DESC");
//			}
//		} else {
//			throw new IllegalArgumentException("please enter correct page and size");
//
//		}
//
//		if (proposerListing.getSortBy() != null && !proposerListing.getSortBy().isEmpty()) {
//			String sortBy = proposerListing.getSortBy();
//
//			if ("ASC".equalsIgnoreCase(proposerListing.getSortOrder())) {
//				criteriaQuery.orderBy(criteriaBuilder.asc(root.get(sortBy)));
//
//			} else {
//				criteriaQuery.orderBy(criteriaBuilder.desc(root.get(sortBy)));
//
//			}
//		}
//
//		if (proposerListing.getPage() <= 0 || proposerListing.getSize() <= 0) {
//			return entityManager.createQuery(criteriaQuery).getResultList();
//
//		} else {
//			Integer page = proposerListing.getPage();
//			Integer size = proposerListing.getSize();
//
//			TypedQuery<PersonalDetails> typedQuery = entityManager.createQuery(criteriaQuery);
//			typedQuery.setFirstResult((page - 1) * size);
//			typedQuery.setMaxResults(size);
//
//			return typedQuery.getResultList();
//		}

		if (proposerListing.getPage() < 0 || proposerListing.getSize() < 0) {
			throw new IllegalArgumentException("Please enter correct page and size");
		}

		String sortBy = (proposerListing.getSortBy() == null || proposerListing.getSortBy().isEmpty()) ? "id"
				: proposerListing.getSortBy();
		String sortOrder = (proposerListing.getSortOrder() == null || proposerListing.getSortOrder().isEmpty()) ? "DESC"
				: proposerListing.getSortOrder();

		if ("ASC".equalsIgnoreCase(sortOrder)) {
			criteriaQuery.orderBy(criteriaBuilder.asc(root.get(sortBy)));
		} else {
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get(sortBy)));
		}

		TypedQuery<PersonalDetails> typedQuery = entityManager.createQuery(criteriaQuery);

		if (proposerListing.getPage() > 0 && proposerListing.getSize() > 0) {
			typedQuery.setFirstResult((proposerListing.getPage() - 1) * proposerListing.getSize());
			typedQuery.setMaxResults(proposerListing.getSize());
		}
		List<PersonalDetails> resultList = typedQuery.getResultList();

		int totalSize = resultList.size();
		totalRecord = totalSize;

		return typedQuery.getResultList();
	}

	@Override
	public void exportPersonalDetailsToExcel(HttpServletResponse response) throws IOException {

		List<PersonalDetails> personalDetailsList = personalDetailsRepository.findAll();

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Personal Details");

		String[] headers = { "Title", "Full Name", "Gender", "Date of Birth", "Nationality", "Marital Status",
				"PAN Number", "Email ID", "Mobile Number", "Alternate Mobile Number", "Address", "Pincode", "City",
				"State", "Status", "Created At", "Updated At", "Gender ID" };
		XSSFRow headerRow = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			headerRow.createCell(i).setCellValue(headers[i]);
		}

		int rowNum = 1;
		for (PersonalDetails details : personalDetailsList) {
			XSSFRow row = sheet.createRow(rowNum++);

//			row.createCell(0).setCellValue(details.getPersonalDetailsId());
			row.createCell(1).setCellValue(details.getTitle() != null ? details.getTitle().toString() : "");
			row.createCell(2).setCellValue(details.getFullName());
			row.createCell(3).setCellValue(details.getGender() != null ? details.getGender().toString() : "");
			row.createCell(4).setCellValue(details.getDateOfBirth() != null ? details.getDateOfBirth().toString() : "");
			row.createCell(5).setCellValue(details.getNationality() != null ? details.getNationality().toString() : "");
			row.createCell(6)
					.setCellValue(details.getMaritalStatus() != null ? details.getMaritalStatus().toString() : "");
			row.createCell(7).setCellValue(details.getPanNumber());
			row.createCell(8).setCellValue(details.getEmailId());
			row.createCell(9).setCellValue(details.getMobileNumber());
			row.createCell(10).setCellValue(details.getAlternateMobileNumber());
			row.createCell(11).setCellValue(details.getAddress());
			row.createCell(12).setCellValue(details.getPincode());
			row.createCell(13).setCellValue(details.getCity());
			row.createCell(14).setCellValue(details.getState());
			row.createCell(15).setCellValue(details.getStatus() != null ? details.getStatus().toString() : "");
			row.createCell(16).setCellValue(details.getCreateAt() != null ? details.getCreateAt().toString() : "");
			row.createCell(17).setCellValue(details.getUpdatedAt() != null ? details.getUpdatedAt().toString() : "");
			row.createCell(18).setCellValue(details.getGenderId() != null ? details.getGenderId() : 0);
		}

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=personal_details.xlsx");

		workbook.write(response.getOutputStream());
		workbook.close();

	}

	@Override
	public String sampleExcel() throws IOException {

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Personal Details");

//		List<String> headers = Arrays.asList("Title*", "Full Name*", "Gender*", "Date of Birth*", "Nationality",
//				"Marital Status", "PAN Number*", "Email ID*", "Mobile Number*", "Alternate Mobile Number", "Address*",
//				"Pincode*", "City*", "State*", "Status", "Created At", "Updated At", "Gender ID");

		List<String> headers = Arrays.asList("Title* ", "Full Name*", "Gender*", "Date of Birth*", "Nationality",
				"Marital Status", "PAN Number*", "Email ID*", "Mobile Number*", "Alternate Mobile Number", "Address*",
				"Pincode*", "City*", "State*");

		XSSFRow headerRow = sheet.createRow(0);
		for (int i = 0; i < headers.size(); i++) {
			headerRow.createCell(i).setCellValue(headers.get(i));
		}

		String uid = UUID.randomUUID().toString().replace("-", "");
		String filePath = "C:\\DOWNLOAD_LINKS\\personal_details" + uid + ".xlsx";
		try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
			workbook.write(fileOut);
		}
		;

		workbook.close();

		return filePath;

	}

//	@Override
//	public List<PersonalDetails> importPersonalDetailsFromExcel(MultipartFile file) throws IOException {
//		List<PersonalDetails> savedExcelList = new ArrayList<>();
//		try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
//			XSSFSheet sheet = workbook.getSheetAt(0);
//			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//				XSSFRow row = sheet.getRow(i);
//				if (row == null)
//					continue;
//				PersonalDetailsDto dto = new PersonalDetailsDto();
//				dto.setTitle(getCellString(row.getCell(0)));
//				dto.setFullName(getCellString(row.getCell(1)));
//				dto.setGender(getCellString(row.getCell(2)));
//					
//				Cell dobCell = row.getCell(3);
//				if (dobCell != null && dobCell.getCellType() == CellType.NUMERIC
//						&& DateUtil.isCellDateFormatted(dobCell)) {
//					dto.setDateOfBirth(dobCell.getLocalDateTimeCellValue().toLocalDate());
//				}
//				dto.setNationality(getCellString(row.getCell(4)));
//				dto.setMaritalStatus(getCellString(row.getCell(5)));
//				dto.setPanNumber(getCellString(row.getCell(6)));
//				dto.setEmailId(getCellString(row.getCell(7)));
//				dto.setMobileNumber(getCellString(row.getCell(8)));
//				dto.setAlternateMobileNumber(getCellString(row.getCell(9)));
//				dto.setAddress(getCellString(row.getCell(10)));
//				dto.setPincode(getCellString(row.getCell(11)));
//				dto.setCity(getCellString(row.getCell(12)));
//				dto.setState(getCellString(row.getCell(13)));
//				
//				try {
//					PersonalDetails saved = savePersonalDetails(dto);
//					savedExcelList.add(saved);
//				}catch(IllegalArgumentException e) {
//					System.out.println("Error saving row "+i+": "+e.getMessage());
//				}
//			}
//			return savedExcelList;
//		}	
//	}

	@Override
	public List<PersonalDetails> importPersonalDetailsFromExcel(MultipartFile file) throws IOException {
		List<PersonalDetails> savedList = new ArrayList<>();

		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		XSSFSheet sheet = workbook.getSheetAt(0);
	
		totalRecord=0;
		failedRecord=0;
		
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			XSSFRow row = sheet.getRow(i);
//			System.err.println("row" + row);

			if (row == null || isRowCompletelyEmpty(row)) {
				continue;
			}

			totalRecord++;			

			PersonalDetails details = new PersonalDetails();

			String titleStr = getCellString(row.getCell(0)).trim().toUpperCase();
			Title title = null;
			for (Title t : Title.values()) {
				if (t.name().equals(titleStr)) {
					title = t;
					break;
				}
			}
			if (title == null) {
				saveError("Invalid or empty Title", "title", i);
				failedRecord++;
				continue;
			}
			details.setTitle(title);

			String fullName = getCellString(row.getCell(1)).trim();
			if (isNullOrEmpty(fullName) ||!fullName.matches("[a-zA-Z\\s]+")) {
				saveError("Invalid or empty Full Name", "fullName", i);
				failedRecord++;
				continue;
			}

			details.setFullName(fullName);

			String genderStr = getCellString(row.getCell(2)).trim().toUpperCase();
			Gender gender = null;
			for (Gender g : Gender.values()) {
				if (g.name().equals(genderStr)) {
					gender = g;
					break;
				}
			}
			if (gender == null) {
				saveError("Invalid or empty Gender", "gender", i);
				failedRecord++;
				continue;
			}
			details.setGender(gender);
			details.setGenderId(getGenderId(gender));

			Cell dobCell = row.getCell(3);

//				if (dobCell != null && DateUtil.isCellDateFormatted(dobCell)) {
//					details.setDateOfBirth(dobCell.getLocalDateTimeCellValue().toLocalDate());
//					
//				} else {
//					saveError("Invalid or empty Date of Birth", "dateOfBirth", i);
//					continue;
//				}

			if (dobCell != null && dobCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dobCell)) {
				details.setDateOfBirth(dobCell.getLocalDateTimeCellValue().toLocalDate());
			} else {
				saveError("Invalid or empty Date of Birth", "dateOfBirth", i);
				failedRecord++;
				continue;
			}

			String nationalityStr = getCellString(row.getCell(4)).trim().toUpperCase();
			Nationality nationality = null;
			for (Nationality n : Nationality.values()) {
				if (n.name().equals(nationalityStr)) {
					nationality = n;
					break;
				}
			}
//				if (nationality == null) {
//					saveError("Invalid or empty Nationality", "nationality", i);
//					continue;
//				}

			details.setNationality(nationality);

			String maritalStr = getCellString(row.getCell(5)).trim().toUpperCase();
			MaritalStatus maritalStatus = null;
			for (MaritalStatus m : MaritalStatus.values()) {
				if (m.name().equals(maritalStr)) {
					maritalStatus = m;
					break;
				}
			}
//				if (maritalStatus == null) {
//					saveError("Invalid or empty Marital Status", "maritalStatus", i);
//					continue;
//				}
			details.setMaritalStatus(maritalStatus);

			String panNumber = getCellString(row.getCell(6)).trim().toUpperCase();
			if (isNullOrEmpty(panNumber) || !panNumber.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {

				saveError("Invalid or empty PAN Number", "panNumber", i);
				failedRecord++;
				continue;
			}
			details.setPanNumber(panNumber);

			String emailId = getCellString(row.getCell(7)).trim();
			if (isNullOrEmpty(emailId) || !emailId.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
				saveError("Invalid or empty Email ID", "emailId", i);
				failedRecord++;
				continue;
			}
			details.setEmailId(emailId);

			String mobileNumber = getCellString(row.getCell(8)).trim();
			if (isNullOrEmpty(mobileNumber) || !mobileNumber.matches("[6789]\\d{9}")) {
				saveError("Invalid or empty Mobile Number", "mobileNumber", i);
				failedRecord++;
				continue;
			}
			details.setMobileNumber(mobileNumber);

			String alternateMobileNumber = getCellString(row.getCell(9)).trim();
			if (alternateMobileNumber != null && alternateMobileNumber.matches("[6789]\\d{9}")) {
				details.setAlternateMobileNumber(alternateMobileNumber);
			}
//				else {
//					saveError("Invalid or empty alternate Mobile Number", "alternateMobileNumber", i);
//					System.out.println("Invalid or empty Alternate Mobile Number at row " + i);
//					continue;
//				}

			String address = getCellString(row.getCell(10)).trim();
			if (isNullOrEmpty(address)) {
				saveError("Invalid or empty Address", "address", i);
				failedRecord++;
				continue;
			}
			details.setAddress(address);

			String pincode = getCellString(row.getCell(11)).trim();
			if (isNullOrEmpty(pincode) || !pincode.matches("[0-9]{6}")) {
				saveError("Invalid or empty Pincode", "pincode", i);
				failedRecord++;
				continue;
			}
			details.setPincode(pincode);

			String city = getCellString(row.getCell(12)).trim();
			if (isNullOrEmpty(city) || !city.matches("[A-Za-z\\s]+")) {
				saveError("Invalid or empty City", "city", i);
				failedRecord++;
				continue;
			}
			details.setCity(city);

			String state = getCellString(row.getCell(13)).trim();

			if (isNullOrEmpty(state) || !state.matches("[A-Za-z\\s]+")) {
				saveError("Invalid or empty State", "state", i);
				failedRecord++;
				continue;
			}
			details.setState(state);

			details.setStatus('Y');

			

				PersonalDetails saveDetails = personalDetailsRepository.save(details);
				savedList.add(saveDetails);

//				savedList.add(personalDetailsRepository.save(details));				
				ResponseExcel responseExcel = new ResponseExcel();
				responseExcel.setStatus(true);
				responseExcel.setError("success");
				responseExcel.setUpdateMsg("Inserted Successfully");

				responseExcel.setErrorField(details.getPersonalDetailsId().toString());
				responseExcelRepository.save(responseExcel);			
		}
		
	
		workbook.close();
		return savedList;
		
	}

	public boolean isRowCompletelyEmpty(Row row) {
		for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
			Cell cell = row.getCell(cellNum);
			if (cell != null && cell.getCellType() != CellType.BLANK) {
				return false;
			}
		}
		return true;
	}

	private boolean isNullOrEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}

	private void saveError(String errorMsg, String field, int rowIndex) {
		ResponseExcel response = new ResponseExcel();
		
		response.setStatus(false);
		response.setError(errorMsg);
		response.setUpdateMsg("Failed");
		response.setErrorField("Row " + rowIndex + " → " + field);
		responseExcelRepository.save(response);
	}

	private Integer getGenderId(Gender gender) {
		switch (gender) {
		case MALE:
			return 1;
		case FEMALE:
			return 2;
		case OTHER:
			return 3;
		default:
			return null;
		}
	}

	private String getCellString(Cell cell) {
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			return DateUtil.isCellDateFormatted(cell) ? "" : String.valueOf((long) cell.getNumericCellValue());
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		default:
			return "";
		}
	}
}
