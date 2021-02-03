package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES")
    List<File> findAll();

    @Select("SELECT * FROM FILES WHERE fileid = #{fileId}")
    public File findOne(int fileId);

    @Select("SELECT * FROM FILES WHERE fileid = #{fileId} and userid = #{userId}")
    public File findByFileIdAndUserId(int fileId, int userId);

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    public List<File> findFilesByUserId(int userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, filedata, userid) VALUES (#{fileName}, #{contentType}, #{fileSize}, #{fileData}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    public Integer insertFile(File file);

    @Delete("DELETE FROM FILES WHERE fileid = #{fileId}")
    public void deleteFile(int fileId);
}
