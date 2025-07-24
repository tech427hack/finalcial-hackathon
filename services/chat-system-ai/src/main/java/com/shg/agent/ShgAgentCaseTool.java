package com.shg.agent;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.shg.dto.CaseDetails;

@Component
public class ShgAgentCaseTool {

    private final List<CaseDetails> caseStore = new ArrayList<>();

    @Tool(name = "createSupportCase", description = "Creates a support case with user name, email, phone and issue description.")
    public String createCase(String name, String email, String phoneNumber, String issueDescription) {
    	CaseDetails supportCase = new CaseDetails(email, phoneNumber, name, issueDescription);
        caseStore.add(supportCase);
        return "📩 Support case created successfully! Case ID: " + supportCase.getCaseId() + " 😊";
    }
    
    @Tool(name = "getCaseDetails", description = "Get all case details or a specific one by Case ID.")
    public String getAllCases(String caseId) {
    	System.out.println("Called getAllCases");
    	if (caseId == null || caseId.isBlank()) {
            if (caseStore.isEmpty()) return "📭 No cases found.";
            
            StringBuilder allCases = new StringBuilder("📋 All Cases:\n\n");
            for (CaseDetails c : caseStore) {
                allCases.append(c.toString()).append("\n\n");
            }
            return allCases.toString();
        }

        try {
            long id = Long.parseLong(caseId.trim());
            return caseStore.stream()
                    .filter(c -> c.getCaseId() == id)
                    .findFirst()
                    .map(CaseDetails::toString)
                    .orElse("❌ Case ID " + id + " not found.");
        } catch (NumberFormatException e) {
            return "⚠️ Invalid Case ID format.";
        }
    	
    }
}
