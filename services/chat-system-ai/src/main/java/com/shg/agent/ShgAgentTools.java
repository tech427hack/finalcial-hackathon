package com.shg.agent;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class ShgAgentTools {

	  @Tool(description = "Who am i? or who are you? SHG Group Details and Group forming and Loan "
	  		+ "apply process and admin details.Documents required to form a group or to apply loan")
	    String getMyDetails() {
	    	System.out.println("getMyDetails .. called ");
	        return "			    You are giving the answers for SHD community savings by group of people can form a group\r\n"
	        		+ "			    and can do saving out of it Users can take loan with proper details.\r\n"
	        		+ "			    From this savings group community can generate profits them self.\r\n"
	        		+ "			    Address of SDH community group Sarjapura,Bangalore,India,Ph:9986781724\r\n"
	        		+ "			    Admin email address:manohar.s@sdh.com,Name : Manohar Sambayyapalem.\r\n"
	        		+ "			    We need to get proper approval to form a community Group.\r\n"
	        		+ "			    Once community is formed .. they can conduct meetings and pools in this app.\r\n"
	        		+ "			    To join in to community mobile number & Adhar card is mandatory.";
	    }

		@Tool(description = "provide loan details based on Loan account number")
		String getLoanDetailsBasedLoanAccnumber(String loanAccNumber) {
			System.out.println("getLoanDetailsBasedLoanAccnumber .. called ");
			return "Loan Acc : "+loanAccNumber+", name: Manohar, amount:1234" ;
		}

}