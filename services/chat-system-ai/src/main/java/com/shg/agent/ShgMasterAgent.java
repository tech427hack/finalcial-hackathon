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
			    By default consider English.
			    If the input is in Hindhi respond in Hindhi language.
			    If the input is in Kannada respond in Kannada language.
			    If the input is in Telugu respond in Telugu language.
			    If the input is in Tamil respond in Tamil language.
			    If the input is in Tamil Oria in Oria language.
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
