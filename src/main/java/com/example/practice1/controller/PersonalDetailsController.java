package com.example.practice1.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.practice1.dto.PersonalDetailsDto;
import com.example.practice1.listing.ProposerListing;
import com.example.practice1.model.PersonalDetails;
import com.example.practice1.repository.PersonalDetailsRepository;
import com.example.practice1.response.ResponseHandler;
import com.example.practice1.service.PersonalDetailsService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/personal_details/")
public class PersonalDetailsController {

	@Autowired
	private PersonalDetailsService personalDetailsService;

	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;

	@PostMapping("add")
	public ResponseHandler createPersonalDetails(@RequestBody PersonalDetailsDto personalDetailsDto) {
		ResponseHandler responseHandler = new ResponseHandler();
		try {
			PersonalDetails createdDetails = personalDetailsService.savePersonalDetails(personalDetailsDto);
			responseHandler.setData(createdDetails);
			responseHandler.setMessage("Success");
			responseHandler.setStatus(true);
		} catch (IllegalArgumentException e) {
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage(e.getMessage());
			responseHandler.setStatus(false);

		} catch (Exception e) {
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage("Error");
			responseHandler.setStatus(false);
		}
		return responseHandler;
	}

	@GetMapping("listing")
	public ResponseHandler getAllPersonalDetails() {
		ResponseHandler responseHandler = new ResponseHandler();
		try {
			List<PersonalDetails> personalDetailsList = personalDetailsService.getAllPersonalDetails();
			responseHandler.setData(personalDetailsList);
			responseHandler.setMessage("Success");
			responseHandler.setStatus(true);
		} catch (IllegalArgumentException e) {
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage(e.getMessage());
			responseHandler.setStatus(false);

		} catch (Exception e) {
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage("Error");
			responseHandler.setStatus(false);
		}
		return responseHandler;
	}

//	@GetMapping("/{id}")
//	public ResponseHandler updateById(@RequestBody UpdateDto dto ,@PathVariable Integer id) {
//		ResponseHandler responseHandler = new ResponseHandler();
//		try {
//			personalDetailsService.getPersonalDetailsById(id);
//			if (details.isPresent()) {
//
//				responseHandler.setData(details.get());
//				responseHandler.setMessage("success");
//				responseHandler.setStatus(true);
//
//			} else {
//
//				responseHandler.setData(Collections.emptyList());
//				responseHandler.setMessage("Personal Details not found");
//				responseHandler.setStatus(false);
//			}
//		}catch (IllegalArgumentException e) {
//				e.printStackTrace();
//			responseHandler.setData(Collections.emptyList());
//			responseHandler.setMessage(e.getMessage());
//			responseHandler.setStatus(false);
//		} catch (Exception e) {
//
//			responseHandler.setData(Collections.emptyList());
//			responseHandler.setMessage("Error");
//			responseHandler.setStatus(false);
//		}
//		return responseHandler;
//	}

	@GetMapping("list_by_id/{id}")
	public ResponseHandler getPersonalDetailsById(@PathVariable Integer id) {
		ResponseHandler responseHandler = new ResponseHandler();
		try {
			Optional<PersonalDetails> details = personalDetailsService.getPersonalDetailsById(id);

			responseHandler.setData(details.get());
			responseHandler.setMessage("success");
			responseHandler.setStatus(true);

		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage(e.getMessage());
			responseHandler.setStatus(false);
		} catch (Exception e) {
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage("Error");
			responseHandler.setStatus(false);
		}
		return responseHandler;
	}

	@PutMapping("update/{id}")
	public ResponseHandler updatePersonalDetails(@PathVariable Integer id,
			@RequestBody PersonalDetailsDto personalDetailsDto) {
		ResponseHandler responseHandler = new ResponseHandler();
		try {
			PersonalDetails updatedDetails = personalDetailsService.updatePersonalDetails(id, personalDetailsDto);

			responseHandler.setData(updatedDetails);
			responseHandler.setMessage("success");
			responseHandler.setStatus(true);

		} catch (IllegalArgumentException e) {
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage(e.getMessage());
			responseHandler.setStatus(false);
		} catch (Exception e) {
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage("Error");
			responseHandler.setStatus(false);
		}
		return responseHandler;
	}

	@DeleteMapping("delete/{id}")
	public ResponseHandler deletePersonalDetails(@PathVariable Integer id) {
		ResponseHandler responseHandler = new ResponseHandler();
		try {
			personalDetailsService.deletePersonalDetails(id);

			responseHandler.setData(null);
			responseHandler.setMessage("success");
			responseHandler.setStatus(true);
		} catch (IllegalArgumentException e) {
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage(e.getMessage());
			responseHandler.setStatus(false);

		} catch (Exception e) {

			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage("Error");
			responseHandler.setStatus(false);
		}
		return responseHandler;
	}

	@PostMapping("get_all")
	public ResponseHandler getPersonalDetails(@RequestBody ProposerListing proposerListig) {
		ResponseHandler responseHandler = new ResponseHandler();

		try {
			List<PersonalDetails> personalDetailsList = personalDetailsService.getPersonalDetails(proposerListig);

			responseHandler.setData(personalDetailsList);
			responseHandler.setTotalRecords(personalDetailsService.totalRecords());
			responseHandler.setMessage("success");
			responseHandler.setStatus(true);

		} catch (IllegalArgumentException e) {

			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage(e.getMessage());
			responseHandler.setStatus(false);

		} catch (Exception e) {
			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage("Error");
			responseHandler.setStatus(false);
		}
		return responseHandler;
	}

	@GetMapping("export")
	public ResponseHandler exportPersonalDetailsToExcel(HttpServletResponse response) {
		ResponseHandler responseHandler = new ResponseHandler();
		try {

			personalDetailsService.exportPersonalDetailsToExcel(response);
//			responseHandler.setMessage("Excel file exported successfully");
//			responseHandler.setStatus(true);
			return responseHandler;
		} catch (IOException e) {
//			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage(e.getMessage());
			responseHandler.setStatus(false);

		} catch (Exception e) {
//			responseHandler.setData(new ArrayList<>());
			responseHandler.setMessage("Error");
			responseHandler.setStatus(false);
		}
		return responseHandler;

	}

	@GetMapping("sample_excel")
	public ResponseHandler sampleExcel() {
		ResponseHandler responseHandler = new ResponseHandler();
		try {

			String downloadLink = personalDetailsService.sampleExcel();
			responseHandler.setData(downloadLink);
			responseHandler.setMessage("Excel file generated successfully");
			responseHandler.setStatus(true);

		} catch (Exception e) {

			responseHandler.setMessage("Error");
			responseHandler.setStatus(false);
		}
		return responseHandler;

	}

	@PostMapping(value = "import_personal_details", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseHandler importPersonalDetails(
			@Parameter(description = "Excel file to upload", required = true) @RequestParam("file") MultipartFile file) {
		ResponseHandler responseHandler = new ResponseHandler();
		try {
			List<PersonalDetails> savedExceList = personalDetailsService.importPersonalDetailsFromExcel(file);

			responseHandler.setData(savedExceList);
			responseHandler.setMessage("Excel data imported successfully");
			responseHandler.setStatus(true);
		} catch (Exception e) {
			e.printStackTrace();
			responseHandler.setMessage("Failed to import Excel data: " + e.getMessage());
			responseHandler.setStatus(false);
			responseHandler.setData(new ArrayList<>());
		}
		return responseHandler;
	}

}
