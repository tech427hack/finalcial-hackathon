package com.shg.controller;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shg.agent.ShgMasterAgent;


@RestController
public class ShgGroupApplicationController {

   // private final ChatClient chatClient;

    @Autowired
	private final ShgMasterAgent masterAgent;
    
    public ShgGroupApplicationController(ShgMasterAgent masterAgent) {
		this.masterAgent = masterAgent;
	}
   /* public ShgGroupApplicationController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }*/

    @GetMapping(value = "/user",produces = "application/json")
	@CrossOrigin(origins = "http://localhost:5000")
    public String getAnswer(@RequestParam String query) {
    	System.out.println("Question:"+query);
    	
    	/*Prompt prompt = new Prompt(List.of(new SystemMessage("""
			    You are an AI Assistant, a polite and knowledgeable AI assistant.
			    Then answer the user's question politely and clearly.
			    You are giving the answers for SHD community savings by group of people can form a group
			    and can do saving out of it Users can take loan with proper details.
			    From this savings group community can generate profits them self.
			    Address of SDH community group Sarjapura,Bangalore,India,Ph:9986781724
			    Admin email address:manohar.s@sdh.com,Name : Manohar Sambayyapalem.
			    We need to get proper approval to form a community Group.
			    Once community is formed .. they can conduct meetings and pools in this app.
			    To join in to community mobile number & Adhar card is mandatory.
			    Always be respectful and never respond rudely, even if provoked.
			    Also you can use meaningful icons like 😊 📊 💡 ✍️ based on the situation.
			"""), new UserMessage(query)));
    	
    	ChatResponse cliResponse = chatClient.prompt(prompt)
				.call()
				.chatResponse();
    	String response = cliResponse.getResult().getOutput().getText();
    	System.out.println("Response:"+response);
    	return  response;*/
    	
    	return masterAgent.handle(query);
    	
    }
}