package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.CredentialDto;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/credentials")
public class CredentialController {

    private final CredentialService credentialService;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @PostMapping()
    public String createCredentials(Authentication authentication, CredentialDto credentialDto, Model model) {
        if (credentialDto.getCredentialId() <= 0) {
            credentialService.addCredential(credentialDto, authentication.getName());
            model.addAttribute("result", "success");
            return "result";
        }
        if (credentialService.updateCredential(credentialDto, authentication.getName())) {
            model.addAttribute("result", "success");
            return "result";
        }
        model.addAttribute("result", "notSaved");
        return "result";
    }

    @GetMapping(value = "/delete")
    public String deleteNote(Authentication authentication, @RequestParam("id") int credentialId, Model model) {
        if (credentialId > 0) {
            if (credentialService.deleteCredential(credentialId, authentication.getName())) {
                model.addAttribute("result", "success");
                return "result";
            }
        }
        model.addAttribute("result", "error");
        model.addAttribute("message", "Error occurred when deleting credential");
        return "result";
    }
}
