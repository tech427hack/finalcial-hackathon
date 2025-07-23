package com.shg.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shg.agent.ShgMasterAgent;


@RestController
public class ShgGroupApplicationController {

    @Autowired
	private final ShgMasterAgent masterAgent;
    
    public ShgGroupApplicationController(ShgMasterAgent masterAgent) {
		this.masterAgent = masterAgent;
	}

    @GetMapping(value = "/user",produces = "application/json")
	@CrossOrigin(origins = "http://localhost:5000")
    public String getAnswer(@RequestParam String query) {
    	System.out.println("Question:"+query);
    		return callWithRetry(query,5) ;
    }
    
    public String callWithRetry(String query,int retries) {
        int delay = 5000; // start with 5s
        for (int i = 0; i < retries; i++) {
            try {
                return masterAgent.handle(query);
            } catch (Exception  e) {
            	
            	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+e.getMessage());
                    System.out.println("Retrying due to RESOURCE_EXHAUSTED... attempt " + (i + 1));
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                    delay *= 2; // exponential backoff
                }
            }
        return "Please try again some issue at server side.";
    }
}