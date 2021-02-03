package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.dto.NoteDto;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;
    private final UserService userService;

    public NoteService(NoteMapper noteMapper, UserService userService) {
        this.noteMapper = noteMapper;
        this.userService = userService;
    }

    public List<Note> getUserNotes(String userName) {
        return noteMapper.findNoteByUserId(userService.getUser(userName).getUserId());
    }

    public void addNote(NoteDto noteDto, String userName) {
        Note note = new Note(noteDto.getNoteTitle(), noteDto.getNoteDescription(), userService.getUser(userName).getUserId());
        noteMapper.insertNote(note);
    }

    public boolean deleteNote(Integer noteId, String userName) {
        Note note = noteMapper.findByNoteIdAndUserId(noteId, userService.getUser(userName).getUserId());
        if (note != null) {
            noteMapper.deleteNote(noteId);
            return true;
        }
        return false;
    }

    public boolean updateNote(NoteDto noteDto, String userName) {
        Note note = noteMapper.findByNoteIdAndUserId(noteDto.getNoteId(), userService.getUser(userName).getUserId());
        if (note != null) {
            note.setNoteTitle(noteDto.getNoteTitle());
            note.setNoteDescription(noteDto.getNoteDescription());
            noteMapper.updateNote(note);
            return true;
        }
        return false;
    }
}
