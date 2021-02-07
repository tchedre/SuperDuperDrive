package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Controller
@RequestMapping("/files")
public class FileController implements HandlerExceptionResolver {

    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(value = "/{fileId}")
    public ResponseEntity<InputStreamResource> downloadFile(Authentication authentication, @PathVariable("fileId") int fileId) {
        if (fileId < 0) {
            return (ResponseEntity<InputStreamResource>) ResponseEntity.badRequest();
        }

        File file = this.fileService.getFile(fileId, authentication.getName());

        if (file != null) {
            InputStream inputStream = new ByteArrayInputStream(file.getFileData());
            InputStreamResource resource = new InputStreamResource(inputStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getFileName())
                    .contentType(MediaType.parseMediaType(file.getContentType()))
                    .body(resource);
        }

        return (ResponseEntity<InputStreamResource>) ResponseEntity.notFound();
    }

    @PostMapping()
    public String uploadFile(MultipartFile fileUpload, Authentication authentication, Model model) {
        if (fileUpload.isEmpty()) {
            model.addAttribute("result", "error");
            model.addAttribute("message", "File to upload is empty");
            return "result";
        }
        if (fileService.isFileExist(fileUpload.getOriginalFilename())) {
            model.addAttribute("result", "error");
            model.addAttribute("message", fileUpload.getOriginalFilename() + " already exists");
            return "result";
        }
        if (fileService.addFile(fileUpload, authentication.getName())) {
            model.addAttribute("result", "success");
            return "result";
        }
        model.addAttribute("result", "error");
        model.addAttribute("message", "Error occurred when uploading file");
        return "result";
    }

    @GetMapping("/delete")
    public String deleteFile(Authentication authentication, @RequestParam("id") int fileId, Model model) {
        if (fileId > 0) {
            if (fileService.deleteFile(fileId, authentication.getName())) {
                model.addAttribute("result", "success");
                return "result";
            }
        }
        model.addAttribute("result", "error");
        model.addAttribute("message", "Error occurred when deleting your file");
        return "result";
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        ModelAndView modelAndView = new ModelAndView("result");
        if (e instanceof MaxUploadSizeExceededException) {
            modelAndView.getModel().put("result", "error");
            modelAndView.getModel().put("message", "File size exceeds limit!");
        }
        return modelAndView;
    }
}
