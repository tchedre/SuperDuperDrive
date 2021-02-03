package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES")
    List<Note> findAll();

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId}")
    public Note findOne(int noteId);

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId} and userid = #{userId}")
    public Note findByNoteIdAndUserId(int noteId, int userId);

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    public List<Note> findNoteByUserId(int userId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES (#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    public Integer insertNote(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    public void deleteNote(int noteId);

    @Update("UPDATE NOTES SET notetitle = #{noteTitle}, notedescription = #{noteDescription} WHERE noteid = #{noteId}")
    public Integer updateNote(Note note);
}
