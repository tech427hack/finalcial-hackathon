package com.shg.agent;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ShgAgentTools {

	@Autowired
	ChatModel chatModel;
	
	@Autowired
	 private JdbcTemplate jdbcTemplate;
	
	  @Tool(description = "Who am i? or who are you? SHG Group Details and Group forming and Loan "
	  		+ "apply process and admin details.Documents required to form a group or to apply loan")
	    String getSHGDetails() {
	    	System.out.println("getSHGDetails .. called ");
	        return "			    You are giving the answers for SHD community savings by group of people can form a group\r\n"
	        		+ "			    and can do saving out of it Users can take loan with proper details.\r\n"
	        		+ "			    From this savings group community can generate profits them self.\r\n"
	        		+ "			    Address of SDH community group Sarjapura,Bangalore,India,Ph:9986781724\r\n"
	        		+ "			    Admin email address:manohar.s@sdh.com,Name : Manohar Sambayyapalem.\r\n"
	        		+ "			    We need to get proper approval to form a community Group.\r\n"
	        		+ "			    Once community is formed .. they can conduct meetings and pools in this app.\r\n"
	        		+ "			    To join in to community mobile number & Adhar card is mandatory.";
	    }

		@Tool(description = "provide Member details based on given member details name,id,email id,aadhaar number,phone number,gender")
		String getMemberDetailsBasedOnGivenMemberDetails(String userInput) {
			System.out.println("getMemberDetailsBasedOnGivenMemberDetails .. called ");
			return processMemberQuestion(userInput);
		}
		
		
		 public String processMemberQuestion(String userInput) {
			 String prompt = """
					 You have access to a database with the following table:
					 member(id INTEGER, 
					 		aadhaar VARCHAR,
					 		email character varying(255),
					 		phone character varying(255),
					 		name  character varying(255),
					 		gender )

					 User will provide input in the form of either:
					 - "ID: <value>" to search by 'id'
					 - "Adhar: <value>" to search by 'adhar'
					 - "Phone: <value>" to search by 'phone'

					 User input: "%s"

					 Write a valid SQL SELECT query to get all columns from the 'member' table based on the user input.
			 		 - member by name (use case-insensitive LIKE for name matching)
			 		 If name is mentioned, use: name ILIKE '%%<name>%%'
					 Return ONLY raw SQL. No explanation. No markdown. No comments.
					 """.formatted(userInput);

		        
		        System.out.println("prompt:"+prompt);
		        ChatResponse response = chatModel.call(new Prompt(prompt));
		        String sql = cleanSql(response.getResult().getOutput().getText());

		        System.out.println("Generated SQL:"+sql);
		        if (!sql.trim().toLowerCase().startsWith("select")) {
		            throw new IllegalArgumentException("Only SELECT queries are allowed.");
		        }
		        return executeQuery(sql);
		    }

		    private String cleanSql(String raw) {
		        return raw.replaceAll("```sql", "")
		                  .replaceAll("```", "")
		                  .trim();
		    }

			private String executeQuery(String sql) {
				System.out.println("Query ::: " + sql);
				try {
					List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
					System.out.println("Query execution is completed ..... ");
					if (rows.isEmpty()) {
						return "No results found.";
					}
					return rows.toString();
				} catch (Exception e) {
					return "Something went wrong .. please try agin";
				}
			}

}