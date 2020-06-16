package com.project.files.services;

import com.project.exceptions.EntityNotFoundException;
import com.project.exceptions.FileUploadException;
import com.project.files.configs.FTPClientConfiguration;
import com.project.files.dtos.FileDTO;
import com.project.files.models.File;
import com.project.files.repositories.FileRepository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
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
public class FTPStorageService implements StorageService {
    private final FileRepository repository;
    private final ModelMapper mapper;
    @Value("${ftp.path}")
    private String ftpSystemPath;

    private FTPClient ftpClient;

    public FTPStorageService(FileRepository repository, ModelMapper mapper, FTPClientConfiguration fileUploadConfiguration) {
        this.repository = repository;
        this.mapper = mapper;
        this.ftpClient = fileUploadConfiguration.getFtpClient();
    }

    public FileDTO uploadFile(MultipartFile fileToUpload) {
        String fileName = System.currentTimeMillis() + "_" + fileToUpload.getOriginalFilename();
        try {
            boolean status = ftpClient.storeFile(ftpSystemPath + fileName, fileToUpload.getInputStream());
            if (!status) {
                throw new FileUploadException("File upload failed. Server might be down. Status code: " + ftpClient.getReplyCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(fileName, ftpSystemPath + fileName, fileToUpload.getContentType(), Objects.requireNonNull(fileToUpload.getOriginalFilename()).substring(fileToUpload.getOriginalFilename().lastIndexOf(".") + 1), String.valueOf(fileToUpload.getSize()));
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

    public byte[] getFile(String path) {
        byte[] result = null;
        try {
            result = IOUtils.toByteArray(ftpClient.retrieveFileStream(path));
            ftpClient.completePendingCommand();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public List<byte[]> getFiles(Set<String> paths) {
        List<byte[]> files = new ArrayList<>();
        for (String path : paths) {
            files.add(getFile(path));
        }
        return files;
    }

    public FileDTO updateFile(MultipartFile fileToUpload, String path) {
        String updatedFileName = System.currentTimeMillis() + "_" + fileToUpload.getOriginalFilename();
        try {
            boolean status = ftpClient.storeFile(path, fileToUpload.getInputStream());
            if (!status) {
                throw new FileUploadException("File update failed. Server might be down.\nStatus code: " + ftpClient.getReplyCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        try {
            ftpClient.deleteFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFiles(Set<String> paths) {
        for (String path : paths) {
            deleteFile(path);
        }
    }

    private FileDTO entityToDto(File file) {
        return mapper.map(file, FileDTO.class);
    }
}
