package com.shg.agent;


import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShgAgentTools {

	@Autowired
	ChatModel chatModel;
	
	  @Tool(description = "Who am i? or who are you? SHG Group Details and Group forming and Loan "
	  		+ "apply process and admin details.Documents required to form a group or to apply loan")
	    String getSHGDetails() {
	    	System.out.println("getSHGDetails .. called ");
	        return "			    You are giving the answers for SHG community savings(Phone num and Adhar docs are mandatory) by group of people can form a group\r\n"
	        		+ "			    and can do saving out of it members can apply for loan with proper details.\r\n"
	        		+ "			    From this savings group community can generate profits them self.\r\n"
	        		+ "			    Address of SHG community group Sarjapura,Bangalore,India,Ph:9986781724\r\n"
	        		+ "			    Admin email address:manohar.s@shg.com,Name : Manohar Sambayyapalem.\r\n"
	        		+ "			    We need to get proper approval to form a community Group.\r\n"
	        		+ "			    Once community is formed .. they can conduct meetings and pools in this app.\r\n"
	        		+ "			    To join in to community mobile number & Adhar card is mandatory.";
	    }
}