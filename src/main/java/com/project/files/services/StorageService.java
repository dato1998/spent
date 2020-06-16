package com.project.files.services;

import com.project.files.dtos.FileDTO;
import com.project.files.models.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface StorageService {
    FileDTO uploadFile(MultipartFile fileToUpload);
    List<FileDTO> uploadFiles(List<MultipartFile> filesToUpload);
    FileDTO updateFile(MultipartFile fileToUpload, String path);
    List<FileDTO> updateFiles(List<MultipartFile> filesToUpload, List<String> paths);
    void deleteFile(String path);
    void deleteFiles(Set<String> paths);
    byte[] getFile(String path);
    List<byte[]> getFiles(Set<String> paths);
}
