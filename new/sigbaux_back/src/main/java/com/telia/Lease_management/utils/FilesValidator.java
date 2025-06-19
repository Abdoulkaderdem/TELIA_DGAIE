package com.telia.Lease_management.utils;

import java.io.IOException;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class FilesValidator {
    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png", "pdf"};
    private static final int MAX_FILE_NAME_LENGTH =  50;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 Mo

    @SuppressWarnings("deprecation")
    public static String cleanAndValidateFileName(MultipartFile file) throws IOException {

        String originalFilename = file.getOriginalFilename();
        String filename = StringUtils.cleanPath(originalFilename);

        // Check if filename is empty
        if (StringUtils.isEmpty(originalFilename) || StringUtils.isEmpty(filename)) {
            throw new FileUploadException("File name is empty: " + filename);
        }

        // Check maximum file size 
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileUploadException("File size exceeds the limit: " + filename);
        }

        // Check file name for invalid characters
        if (filename.contains("..")) {
            throw new FileUploadException("File size contain invalid characters: " + filename);
        }

        // Check if the file name is too long
        if (filename.length() > MAX_FILE_NAME_LENGTH) {
            throw new FileUploadException("File is too long: " + filename);
        }

        // File extension validation
        if (!isAllowedExtension(filename)) {
            throw new IllegalArgumentException("Unsupported file extension.");
        }

        return filename;
    }

    
    private static boolean isAllowedExtension(String filename) {
        String extension = StringUtils.getFilenameExtension(filename);
        for (String allowedExt : ALLOWED_EXTENSIONS) {
            if (allowedExt.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }
    
}
