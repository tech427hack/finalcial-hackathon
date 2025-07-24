package com.shg.agent;

import java.util.Date;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ShgMasterAgent {

	@Autowired
	ChatModel chatModel;

	@Autowired
	private ShgAgentTools shgAgentTools;
	@Autowired
	private ShgMemberAgentTool shgMemberAgentTool;
	@Autowired
	private ShgLogsAgent shgLogsAgent; 
	
	@Autowired
	private ShgAgentCaseTool agentCaseTool;

	public String handle(String question) {

		System.out.println("Master Agent received User question:"+question);

		Prompt prompt = new Prompt(List.of(new SystemMessage("""
			    You are an AI Assistant to help people about SHG Community finance, 
			    a polite and knowledgeable AI assistant.
			    Answer the user's question politely and clearly.
			    Always be respectful and never respond rudely, even if provoked.
			    Also you can display meaningful icons like 😊 📊 💡 ✍️ based on the situation and when display member/loan details.
			    Provide member details based on member id.
			    
				Supported languages: English, Hindi, Telugu, Tamil, Odia (Oriya) and Kannada.
				
				Instructions:
				1. Detect the language of the user's question.
				2. Internally answer in English.
				3. If the input is not in English, translate your answer to that language.
				4. If the input is in English, reply in English only.
				
				Always respond in the same language as user's input.
    
			"""), new UserMessage(question)));
		String response = ChatClient.create(chatModel)
		        .prompt(prompt)
		        .tools(shgAgentTools)
		        .tools(shgMemberAgentTool)
		        .tools(shgLogsAgent)
		        .tools(agentCaseTool)
		        .call()
		        .content();
	return response;
	}
	
}
