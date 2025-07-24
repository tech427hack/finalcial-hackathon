package com.shg.dto;


public class CaseDetails {

	private static long counter = 10000; // Start from 10000
	private final long caseId;
	private String email;
	private String phoneNumber;
	private String name;
	private String issueDescription;

	public CaseDetails(String email, String phoneNumber, String name, String issueDescription) {
		this.caseId = ++counter;;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.name = name;
		this.issueDescription = issueDescription;
	}

	public long getCaseId() {
		return caseId;
	}
	
	public String toString() {
		return "Case ID: " + caseId +
	               "\nName: " + name +
	               "\nEmail: " + email +
	               "\nPhone: " + phoneNumber +
	               "\nIssue Details: " + issueDescription;
	}
}
