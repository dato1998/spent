package com.project.files.services;

import com.cloudinary.Cloudinary;
import com.project.exceptions.EntityNotFoundException;
import com.project.files.configs.CloudinaryClientConfiguration;
import com.project.files.dtos.FileDTO;
import com.project.files.models.File;
import com.project.files.repositories.FileRepository;
import com.project.spent.dtos.UserDTO;
import com.project.spent.models.User;
import com.project.spent.services.UserService;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Service
public class CloudinaryStorageService implements StorageService {
    private final FileRepository repository;
    private final UserService userService;
    private final ModelMapper mapper;
    private static final Logger log = Logger.getLogger(CloudinaryStorageService.class);
    private final Cloudinary cloudinary;

    public CloudinaryStorageService(FileRepository repository, UserService userService,
                                    ModelMapper mapper, CloudinaryClientConfiguration configuration) {
        this.repository = repository;
        this.userService = userService;
        this.mapper = mapper;
        this.cloudinary = configuration.cloudinaryConfiguration();
    }

    public FileDTO uploadFile(long userId, MultipartFile fileToUpload) {
        String fileName = System.currentTimeMillis() + "_" + fileToUpload.getOriginalFilename();
        String filePath = "files/" + fileName.substring(0, fileName.indexOf("."));
        try {
            if (!filePath.isEmpty()) {
                Map<String, String> params = new HashMap<>();
                params.put("public_id", filePath);
                cloudinary.uploader().upload(fileToUpload.getBytes(), params);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File(fileName, filePath, fileToUpload.getContentType(), Objects.requireNonNull(fileToUpload.getOriginalFilename()).substring(fileToUpload.getOriginalFilename().lastIndexOf(".") + 1), String.valueOf(fileToUpload.getSize()));
        final FileDTO savedFile = entityToDto(repository.save(file));
        final User user = mapper.map(userService.get(userId), User.class);
        user.setPhoto(file);
        userService.update(userId, mapper.map(user, UserDTO.class));

        return savedFile;
    }

    public List<FileDTO> uploadFiles(long userId, List<MultipartFile> filesToUpload) {
        List<FileDTO> files = new ArrayList<>();
        for (int i = 0; i < filesToUpload.size(); i++) {
            FileDTO file = uploadFile(userId, filesToUpload.get(i));
            files.add(file);
        }
        return files;
    }

    public byte[] getFile(String path) {
        byte[] imageBytes = null;
        URL url = getImageURL(path);
        try (InputStream inputStream = url.openStream()) {
            imageBytes = IOUtils.toByteArray(inputStream);
        } catch (Exception ex) {
            log.error(String.format("error converting image to bytes from path %s", path));
        }
        return imageBytes;
    }

    private URL getImageURL(String path) {
        String imageURL;
        URL url = null;
        try {
            imageURL = (String) cloudinary.api().resource(path, new HashMap()).get("secure_url");
            url = new URL(imageURL);
        } catch (Exception e) {
            log.error(String.format("error getting image url from cloudinary from path %s", path));
        }

        return url;
    }

    public List<byte[]> getFiles(Set<String> paths) {
        List<byte[]> urls = new ArrayList<>();
        try {
            List<HashMap> maps = (List<HashMap>) cloudinary.api().resourcesByIds(paths, new HashMap()).get("resources");
            for (HashMap map : maps) {
                urls.add(getFileFromUrl((String) map.get("secure_url")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urls;
    }

    private byte[] getFileFromUrl(String imageUrl) {
        byte[] imageBytes = null;
        URL url = extractURLFromString(imageUrl);
        try (InputStream inputStream = Objects.requireNonNull(url).openStream()) {
            imageBytes = IOUtils.toByteArray(inputStream);
        } catch (Exception ex) {
            log.error(String.format("error converting image to bytes from path %s", imageUrl));
        }
        return imageBytes;
    }

    private URL extractURLFromString(String imageUrl) {
        try {
            return new URL(imageUrl);
        } catch (MalformedURLException e) {
            log.error(String.format("error converting string to URL %s", imageUrl));
        }
        return null;
    }

    public FileDTO updateFile(MultipartFile fileToUpload, String path) {
        String updatedFileName = System.currentTimeMillis() + "_" + fileToUpload.getOriginalFilename();
        try {
            Map<String, String> params = new HashMap<>();
            params.put("public_id", path);
            cloudinary.uploader().upload(fileToUpload.getBytes(), params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //update it in base
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
            cloudinary.uploader().destroy(path, new HashMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFiles(Set<String> paths) {
        try {
            cloudinary.api().deleteResources(paths, new HashMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FileDTO entityToDto(File file) {
        return mapper.map(file, FileDTO.class);
    }
}
