package com.project.files.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.project.exceptions.EntityNotFoundException;
import com.project.exceptions.FileNotFoundException;
import com.project.files.configs.AmazonS3Configuration;
import com.project.files.dtos.FileDTO;
import com.project.files.models.File;
import com.project.files.repositories.FileRepository;
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class AWSStorageService implements StorageService {
    private final FileRepository repository;
    private final ModelMapper mapper;
    @Value("${aws.bucketName}")
    private String bucketName;

    private AmazonS3 amazonS3Client;

    public AWSStorageService(FileRepository repository, ModelMapper mapper, AmazonS3Configuration amazonS3Configuration) {
        this.repository = repository;
        this.mapper = mapper;
        this.amazonS3Client = amazonS3Configuration.getAmazonClient();
    }

    public FileDTO uploadFile(MultipartFile fileToUpload) {
        String fileName = System.currentTimeMillis() + "_" + fileToUpload.getOriginalFilename();
        String uploadDirectory = "files/" + fileName;
        ObjectMetadata fileMetaData = getObjectMetadata(fileToUpload);
        try {
            amazonS3Client.putObject(bucketName, uploadDirectory, fileToUpload.getInputStream(), fileMetaData);
        } catch (AmazonServiceException | IOException ex) {
            ex.printStackTrace();
        }
        File file = new File(fileName, uploadDirectory, fileToUpload.getContentType(), Objects.requireNonNull(fileToUpload.getOriginalFilename()).substring(fileToUpload.getOriginalFilename().lastIndexOf(".") + 1), String.valueOf(fileToUpload.getSize()));
        return entityToDto(repository.save(file));
    }

    public List<FileDTO> uploadFiles(List<MultipartFile> filesToUpload) {
        List<FileDTO> files = new ArrayList<>();
        for (int i = 0; i < filesToUpload.size(); i++) {
            FileDTO file = uploadFile(filesToUpload.get(i));
            files.add(file);
        }
        return files;
    }

    public FileDTO updateFile(MultipartFile fileToUpload, String path) {
        ObjectMetadata fileMetaData = getObjectMetadata(fileToUpload);
        String updatedFileName = System.currentTimeMillis() + "_" + fileToUpload.getOriginalFilename();
        try {
            amazonS3Client.putObject(bucketName, path, fileToUpload.getInputStream(), fileMetaData);
        } catch (AmazonServiceException | IOException ex) {
            ex.printStackTrace();
        }
        File updatedFile = new File(updatedFileName, path, fileToUpload.getContentType(), Objects.requireNonNull(fileToUpload.getOriginalFilename()).substring(fileToUpload.getOriginalFilename().lastIndexOf(".") + 1), String.valueOf(fileToUpload.getSize()));
        return entityToDto(updatedFile);
    }

    public List<FileDTO> updateFiles(List<MultipartFile> filesToUpload, List<String> paths) {
        List<FileDTO> files = new ArrayList<>();
        if (filesToUpload.size() == paths.size()) {
            for (int i = 0; i < paths.size(); i++) {
                FileDTO file = updateFile(filesToUpload.get(i), paths.get(i));
                files.add(file);
            }
        } else {
            throw new EntityNotFoundException("new uploaded and old files path amounts does not match please check and try again");
        }
        return files;
    }

    public void deleteFile(String path) {
        amazonS3Client.deleteObject(bucketName, path);
    }

    public void deleteFiles(Set<String> paths) {
        for (String path : paths) {
            deleteFile(path);
        }
    }

    public byte[] getFile(String path) {
        byte[] file;
        try {
            file = IOUtils.toByteArray(amazonS3Client.getObject(bucketName, path).getObjectContent());
        } catch (NullPointerException | IOException ex) {
            throw new FileNotFoundException("File not found. Check filename and try again.");
        }
        return file;
    }

    public List<byte[]> getFiles(Set<String> paths) {
        List<byte[]> files = new ArrayList<>();
        for (String path : paths) {
            files.add(getFile(path));
        }
        return files;
    }

    private ObjectMetadata getObjectMetadata(MultipartFile fileToUpload) {
        ObjectMetadata fileMetaData = new ObjectMetadata();
        fileMetaData.addUserMetadata("fileName", fileToUpload.getOriginalFilename());
        fileMetaData.addUserMetadata("contentType", fileToUpload.getContentType());
        fileMetaData.addUserMetadata("size", String.valueOf(fileToUpload.getSize()));
        return fileMetaData;
    }

    private FileDTO entityToDto(File file) {
        return mapper.map(file, FileDTO.class);
    }
}
