package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private FileMapper fileMapper;
    private UserService userService;

    public FileService(FileMapper fileMapper, UserService userService) {
        this.fileMapper = fileMapper;
        this.userService = userService;
    }

    public List<File> getUserFiles(String userName) {
        return fileMapper.findFilesByUserId(userService.getUser(userName).getUserId());
    }

    public File getFile(int fileId, String userName) {
        return fileMapper.findByFileIdAndUserId(fileId, userService.getUser(userName).getUserId());
    }

    public boolean addFile(MultipartFile fileUpload, String userName) {
        try {
            File file = new File(fileUpload.getOriginalFilename(), fileUpload.getContentType(),
                    Long.toString(fileUpload.getSize()), userService.getUser(userName).getUserId(),
                    fileUpload.getBytes());
            fileMapper.insertFile(file);
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    public boolean deleteFile(int fileId, String userName) {
        File file = fileMapper.findByFileIdAndUserId(fileId, userService.getUser(userName).getUserId());
        if (file != null) {
            fileMapper.deleteFile(fileId);
            return true;
        }
        return false;
    }
}
