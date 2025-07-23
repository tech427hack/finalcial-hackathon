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

	    try {
	        // Read the whole log file as a single string
	        String logContent = Files.readString(Paths.get(logsPath));

	        // Ask the model to extract relevant exception section
	        String prompt = """
	            You are a helpful assistant. The user is asking for exception-related log details.

	            Below is the user query:
	            "%s"

	            And here is the full log content:
	            """
	            .formatted(userQuery) + "```\n" + logContent + "\n```" + """

	            🔍 From the logs, extract only the most relevant part that matches the exception(s) the user is asking about.
	            Include the timestamp and 3-4 lines around the exception to give context.

	            Respond in a clear, readable format.
	            """;

	        ChatResponse response = chatModel.call(new Prompt(prompt));
	        return response.getResult().getOutput().getText().trim();

	    } catch (IOException e) {
	        return "❌ Failed to read log file: " + e.getMessage();
	    } catch (Exception e) {
	        return "❌ Unexpected error: " + e.getMessage();
	    }
	}

}
