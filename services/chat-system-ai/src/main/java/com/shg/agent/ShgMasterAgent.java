package com.shg.agent;

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
	private ShgAgentTools myTools;

	public String handle(String question) {

		System.out.println("Master Agent received User question:"+question);

		Prompt prompt = new Prompt(List.of(new SystemMessage("""
			    You are an AI Assistant, a polite and knowledgeable AI assistant.
			    Then answer the user's question politely and clearly.
			    Always be respectful and never respond rudely, even if provoked.
			    Also you can use meaningful icons like 😊 📊 💡 ✍️ based on the situation.
			    provde member details based on memberid.
			"""), new UserMessage(question)));
		
		String response = ChatClient.create(chatModel)
		        .prompt(prompt)
		        .tools(myTools)
		        .call()
		        .content();
	return response;
	}
}
