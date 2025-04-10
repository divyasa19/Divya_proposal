package com.example.practice1.listing;

import java.util.ArrayList;
import java.util.List;

public class ProposerListing {

	private Integer page;
	private Integer size;
	private String sortBy;
	private String sortOrder;

//	private SearchFilter[] searchFilters;

	List<SearchFilter> searchFiltersList = new ArrayList<>();

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

//	public SearchFilter[] getSearchFilters() {
//		return searchFilters;
//	}
//	public void setSearchFilters(SearchFilter[] searchFilters) {
//		this.searchFilters = searchFilters;
//	}
	public List<SearchFilter> getSearchFiltersList() {
		return searchFiltersList;
	}

	public void setSearchFiltersList(List<SearchFilter> searchFiltersList) {
		this.searchFiltersList = searchFiltersList;
	}

}
