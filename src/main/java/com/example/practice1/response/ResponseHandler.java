package com.example.practice1.response;

public class ResponseHandler {
	private Boolean status;
	private Object data;
	private String message;
	private long totalRecords ;

	public ResponseHandler() {
		super();
	}

	
	public ResponseHandler(Boolean status, Object data, String message, long totalRecords) {
		super();
		this.status = status;
		this.data = data;
		this.message = message;
		this.totalRecords = totalRecords;
	}


	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public long getTotalRecords() {
		return totalRecords;
	}


	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}
	
	
}
