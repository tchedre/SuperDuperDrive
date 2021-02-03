package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.NoteDto;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping()
    public String createNote(Authentication authentication, NoteDto noteDto, Model model) {
        if (noteDto.getNoteId() <= 0) {
            noteService.addNote(noteDto, authentication.getName());
            model.addAttribute("result", "success");
            return "result";
        }
        if (noteService.updateNote(noteDto, authentication.getName())) {
            model.addAttribute("result", "success");
            return "result";
        }
        model.addAttribute("result", "notSaved");
        return "result";
    }

    @GetMapping("/delete")
    public String deleteNote(Authentication authentication, @RequestParam("id") int noteId, Model model) {
        if (noteId > 0) {
            if (noteService.deleteNote(noteId, authentication.getName())) {
                model.addAttribute("result", "success");
                return "result";
            }
        }
        model.addAttribute("result", "error");
        model.addAttribute("message", "Error occurred when deleting note");
        return "result";
    }
}
