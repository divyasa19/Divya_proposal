package com.example.practice1.response;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="response_excel")
public class ResponseExcel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="response_excel_id")
	private Integer responseExcelId;
	
	@Column(name="status")
	private Boolean status;
	
	@Column(name="error")
	private String error;
	
	@Column(name="update_msg")
	private String updateMsg;
	
	@Column(name="error_field")
	private String errorField;
	
	

	public ResponseExcel() {
		super();
	}

	public ResponseExcel(Integer responseExcelId, Boolean status, String error, String updateMsg, String errorField) {
		super();
		this.responseExcelId = responseExcelId;
		this.status = status;
		this.error = error;
		this.updateMsg = updateMsg;
		this.errorField = errorField;
	}

	public Integer getResponseExcelId() {
		return responseExcelId;
	}

	public void setResponseExcelId(Integer responseExcelId) {
		this.responseExcelId = responseExcelId;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getUpdateMsg() {
		return updateMsg;
	}

	public void setUpdateMsg(String updateMsg) {
		this.updateMsg = updateMsg;
	}

	public String getErrorField() {
		return errorField;
	}

	public void setErrorField(String errorField) {
		this.errorField = errorField;
	}
	
	
	
	
	
	

	
	
	
	
	
	
	
}
