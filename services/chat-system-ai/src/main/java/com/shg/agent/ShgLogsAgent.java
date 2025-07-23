package com.shg.agent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShgLogsAgent {

	@Autowired
	ChatModel chatModel;
	
	@Tool(description = "Process the system logs and respond to the user based on exception details requested")
	public String getLogsExceptionDetails(String userQuery) {
	    System.out.println("getLogsExceptionDetails .. called");

	    String logsPath = "D:\\ZestPrime\\Hackathon\\hackathon2025\\finalcial-hackathon\\services\\chat-system-ai\\logs\\logs.txt";
	    String fileUri = "file:///" + logsPath.replace("\\", "/"); // Make Windows path URI compatible

	    try {
	        // Read the whole log file as a single string
	        String logContent = Files.readString(Paths.get(logsPath));

	        // Prompt to model
	        String prompt = """
	            You are a helpful assistant. The user is asking for exception-related log details.

	            Below is the user query:
	            "%s"

	            And here is the full log content:
	            """
	            .formatted(userQuery) + "```\n" + logContent + "\n```" + """

	            🔍 From the logs, extract only the most relevant part that matches the exception(s) the user is asking about.
	            Include the timestamp and 2-3 lines(max) around the exception to give context.
	            Also include date and time stamp when it occured.
	            Format the response in **markdown** always and ensure the result is easy to read.
	            """;

	        ChatResponse response = chatModel.call(new Prompt(prompt));
	        String responseText = response.getResult().getOutput().getText().trim();

	        // Append Full Path link in markdown
	        String fullPathMarkdown = "\n\n📁 **Full Path**: [Open Log File](" + fileUri + ")";
	        return responseText + fullPathMarkdown;

	    } catch (IOException e) {
	        return "❌ Failed to read log file: " + e.getMessage();
	    } catch (Exception e) {
	        return "❌ Unexpected error: " + e.getMessage();
	    }
	}



}
