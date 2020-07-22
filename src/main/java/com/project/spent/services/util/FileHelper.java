package com.project.spent.services.util;

import com.project.files.dtos.FileDTO;
import com.project.files.services.CloudinaryStorageService;
import com.project.spent.dtos.UserDTO;

import java.util.Base64;
import java.util.Optional;

public class FileHelper {
    public static Optional<String> getPhotoAsString(final CloudinaryStorageService cloudinaryStorageService, final UserDTO user) {
        final FileDTO file = user.getPhoto();
        Optional<String> photoAsString = Optional.empty();
        if (file != null) {
            final byte[] photoAsBytes = cloudinaryStorageService.getFile(file.getPath());
            if (photoAsBytes != null)
                photoAsString = Optional.ofNullable(Base64.getEncoder().encodeToString(photoAsBytes));
        }

        return photoAsString;
    }
}
