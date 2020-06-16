package com.project.files.controllers;

import com.project.files.services.StorageService;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("")
public class FileUploadController {
    private final StorageService service;

    public FileUploadController(Environment environment, ApplicationContext applicationContext) {
        String name = environment.getProperty("fileUploadServiceName");
        this.service = (StorageService) applicationContext.getBean(name);
    }

    @PostMapping("/file/")
    public ResponseEntity<Object> uploadFile(@RequestParam(value = "file") MultipartFile multipartFile) {
        return new ResponseEntity<>(service.uploadFile(multipartFile), HttpStatus.OK);
    }

    @PostMapping("/files/")
    public ResponseEntity<Object> uploadFiles(@RequestPart("files") List<MultipartFile> files) {
        return new ResponseEntity<>(service.uploadFiles(files), HttpStatus.OK);
    }

    @PutMapping("/file/")
    public ResponseEntity<Object> updateFile(@RequestParam(value = "path") String path,
                                             @RequestParam(value = "file") MultipartFile multipartFile) {
        return new ResponseEntity<>(service.updateFile(multipartFile, path), HttpStatus.OK);
    }

    @PutMapping("/files/")
    public ResponseEntity<Object> updateFiles(@RequestParam(value = "paths") List<String> paths,
                                              @RequestParam(value = "files") List<MultipartFile> files) {
        return new ResponseEntity<>(service.updateFiles(files, paths), HttpStatus.OK);
    }

    @DeleteMapping("/file/")
    public ResponseEntity<Object> deleteFile(@RequestParam(value = "path") String path) {
        service.deleteFile(path);
        return new ResponseEntity<>("File was deleted.", HttpStatus.OK);
    }

    @DeleteMapping("/files/")
    public ResponseEntity<Object> deleteFiles(@RequestBody Set<String> paths) {
        service.deleteFiles(paths);
        return new ResponseEntity<>("Files were deleted.", HttpStatus.OK);
    }

    @GetMapping(value = "/file/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity getImage(@RequestParam(value = "path") String path) {
        return new ResponseEntity<>(service.getFile(path), HttpStatus.OK);
    }

    @GetMapping(value = "/file/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity getPdf(@RequestParam(value = "path") String path) {
        return new ResponseEntity<>(service.getFile(path), HttpStatus.OK);
    }

    @GetMapping(value = "/files/")
    public ResponseEntity getFiles(@RequestBody Set<String> paths) {
        return new ResponseEntity<>(service.getFiles(paths), HttpStatus.OK);
    }
}
