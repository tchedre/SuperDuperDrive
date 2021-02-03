package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.CredentialDto;
import com.udacity.jwdnd.course1.cloudstorage.dto.FileDto;
import com.udacity.jwdnd.course1.cloudstorage.dto.NoteDto;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.stream.Collectors;

@Controller
public class HomeController {

    private NoteService noteService;
    private CredentialService credentialService;
    private FileService fileService;
    private EncryptionService encryptionService;

    public HomeController(NoteService noteService, CredentialService credentialService, FileService fileService, EncryptionService encryptionService) {
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.fileService = fileService;
        this.encryptionService = encryptionService;
    }

    @GetMapping(value = {"/", "/home"})
    public String getHomePage(Authentication authentication, Model model) {
        String userName = authentication.getName();
        model.addAttribute("notes",
                noteService.getUserNotes(userName).stream().map(this::convertToNoteDto).collect(Collectors.toList()));
        model.addAttribute("credentials",
                credentialService.getUserCredentials(userName).stream().map(this::convertToCredentialDto).collect(Collectors.toList()));
        model.addAttribute("files",
                fileService.getUserFiles(userName).stream().map(this::convertToFileDto).collect(Collectors.toList()));
        return "home";
    }

    public NoteDto convertToNoteDto(Note note) {
        return new NoteDto(note.getNoteId(), note.getNoteTitle(), note.getNoteDescription());
    }
    public CredentialDto convertToCredentialDto(Credential credential) {
        return new CredentialDto(credential.getCredentialId(), credential.getUrl(), credential.getUsername(), encryptionService.decryptValue(credential.getPassword(), credential.getKey()));
    }
    public FileDto convertToFileDto(File file) {
        return new FileDto(file.getFileId(), file.getFileName(), file.getContentType(), file.getFileSize());
    }

}
