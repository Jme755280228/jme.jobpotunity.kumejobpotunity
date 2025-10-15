package jme.jobpotunity.kumejobpotunity.util;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageUtil {

    // The root location for uploads
    private final Path fileStorageLocation;

    public FileStorageUtil() {
        // Define your root upload directory, e.g., 'uploads' in the project root.
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file, String subDir) throws IOException {
        // Normalize file name
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Check for invalid characters
        if (originalFileName.contains("..")) {
            throw new IOException("Sorry! Filename contains invalid path sequence " + originalFileName);
        }

        // Create a unique filename to avoid conflicts
        String fileExtension = "";
        try {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        } catch (Exception e) {
            // Handle cases where there is no extension
        }
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        // Create subdirectory if it doesn't exist
        Path targetLocationDir = this.fileStorageLocation.resolve(subDir);
        Files.createDirectories(targetLocationDir);

        // Copy file to the target location
        Path targetLocation = targetLocationDir.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Return the relative path to be stored in the database, e.g., "cvs/unique-name.pdf"
        return Paths.get(subDir, uniqueFileName).toString();
    }
}


