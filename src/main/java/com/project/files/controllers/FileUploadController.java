package com.project.files.controllers;

import com.project.files.services.StorageService;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/file/user/{userId}")
    public ResponseEntity<Object> uploadFile(@PathVariable("userId") final long userId,
                                             @RequestParam(value = "file") MultipartFile multipartFile) {
        return new ResponseEntity<>(service.uploadFile(userId, multipartFile), HttpStatus.OK);
    }

    @PostMapping("/files/user/{userId}")
    public ResponseEntity<Object> uploadFiles(@PathVariable("userId") final long userId,
                                              @RequestPart("files") List<MultipartFile> files) {
        return new ResponseEntity<>(service.uploadFiles(userId, files), HttpStatus.OK);
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
