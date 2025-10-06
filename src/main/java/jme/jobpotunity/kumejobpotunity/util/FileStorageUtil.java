package jme.jobpotunity.kumejobpotunity.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileStorageUtil {

    // application.properties တွင် သတ်မှတ်ထားသော file.upload-dir ကို တောင်းခံသည်။
    // ဥပမာ: file.upload-dir=/path/to/upload/directory/
    @Value("${file.upload-dir:./uploads}") 
    private String uploadDir;

    /**
     * File ကို သိမ်းဆည်းပြီး လမ်းကြောင်းကို ပြန်ပေးသည်။
     * @param file သိမ်းဆည်းလိုသော file
     * @param subdirectory သိမ်းဆည်းမည့် လမ်းကြောင်းခွဲ (ဥပမာ: "cvs", "images")
     * @return သိမ်းဆည်းထားသော file ၏ လမ်းကြောင်း
     */
    public String storeFile(MultipartFile file, String subdirectory) throws IOException {
        // ဖိုင်မရှိပါက Exception ပြန်ပေးသည်။
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file " + file.getOriginalFilename());
        }

        // ဖိုင်အသစ်အတွက် ထူးခြားသော UUID နှင့် မူရင်း extension ကို သုံး၍ နာမည်ပေးသည်။
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + extension;
        
        // upload path ကို သတ်မှတ်သည်။
        Path targetLocation = Paths.get(this.uploadDir, subdirectory).toAbsolutePath().normalize();
        
        // Directory မရှိပါက ဖန်တီးသည်။
        if (!Files.exists(targetLocation)) {
            Files.createDirectories(targetLocation);
        }

        // File ကို target location တွင် ရေးသွင်းသည်။
        Path targetFile = targetLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetFile);

        // Database တွင် သိမ်းရန်အတွက် relative path ကို ပြန်ပေးသည်။
        // ဥပမာ: cvs/unique-filename.pdf
        return subdirectory + "/" + fileName;
    }

    /**
     * အခြား လိုအပ်သော File Handling Methods များ (ဥပမာ: load, delete) ကို ဤနေရာတွင် ထည့်နိုင်သည်။
     */
}


