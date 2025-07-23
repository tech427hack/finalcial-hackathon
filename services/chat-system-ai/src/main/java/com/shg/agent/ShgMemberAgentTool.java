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
public class ShgMemberAgentTool {

	@Autowired
	ChatModel chatModel;
	
	@Autowired
	 private JdbcTemplate jdbcTemplate;
	

		@Tool(description = "provide Member details based on given member inputs name,id,email,aadhaar number,phone number and gender")
		String getMemberDetailsBasedOnGivenMemberDetails(String userInput) {
			System.out.println("getMemberDetailsBasedOnGivenMemberDetails .. called ");
			return processMemberQuestion(userInput);
		}
		
		
		 public String processMemberQuestion(String userInput) {
			 String prompt = """
					 You have access to a database with the following table:
					 member(id INTEGER, 
					 		aadhaar character varying(255),
					 		email character varying(255),
					 		phone character varying(255),
					 		name  character varying(255),
					 		gender )

					 User will provide input in the form of either:
					 	- "ID: <value>"         → search by id
						- "Name: <value>"       → search by name using ILIKE
						Use the following rules:
						- If input is memberID and number → match ID
					    - If input is Aadhaar and 12-digit number → match Aadhaar
					    - If input is Phone and 10-digit number → match Phone

					 User input: "%s"

					 Write a valid SQL SELECT query to get all columns from the 'member' table based on the user input.
			 		 - member by name (use case-insensitive ILIKE for name matching)
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
					System.out.println("Query execution is completed Result size:"+rows.size());
					
					return rows.toString();
				} catch (Exception e) {
					return "Something went wrong .. please try agin";
				}
			}
}
