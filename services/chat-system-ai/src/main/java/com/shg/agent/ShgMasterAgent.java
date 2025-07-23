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

	public String handle(String question) {

		System.out.println("Master Agent received User question:"+question);

		Prompt prompt = new Prompt(List.of(new SystemMessage("""
			    You are an AI Assistant to help people about SHG Community finance, 
			    a polite and knowledgeable AI assistant.
			    Answer the user's question politely and clearly.
			    Always be respectful and never respond rudely, even if provoked.
			    Also you can display meaningful icons like 😊 📊 💡 ✍️ based on the situation and when display member/loan details.
			    Provide member details based on member id.
			    
				You are a multilingual assistant that understands and replies in the same language as the user.
				
				Supported languages: English, Hindi, Telugu, Tamil, and Kannada.
				
				Instructions:
				1. Detect the language of the user's question.
				2. Answer the question accurately in English internally.
				3. Translate your response back into the same language the user used.
				
				Always respond in the user's original language.
    
			"""), new UserMessage(question)));
		String response = ChatClient.create(chatModel)
		        .prompt(prompt)
		        .tools(shgAgentTools)
		        .tools(shgMemberAgentTool)
		        .tools(shgLogsAgent)
		        .call()
		        .content();
	return response;
	}
	
}
