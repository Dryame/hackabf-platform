package bf.hackabf.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path rootLocation = Paths.get("uploads");
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".pdf");
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/jpeg", "image/png", "application/pdf");

    public FileStorageService() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer le dossier d'upload", e);
        }
    }

    public String saveFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Le fichier est vide.");
            }
            // On génère un nom unique pour éviter d'écraser des fichiers
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.contains(".")) {
                throw new RuntimeException("Nom de fichier invalide.");
            }
            int dotIndex = originalFilename.lastIndexOf('.');
            String extension = originalFilename.substring(dotIndex).toLowerCase(Locale.ROOT);
            String contentType = file.getContentType();
            if (!ALLOWED_EXTENSIONS.contains(extension) || contentType == null
                    || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
                throw new RuntimeException("Type de fichier non autorisé.");
            }
            if (!hasAllowedSignature(file, extension)) {
                throw new RuntimeException("Le contenu du fichier ne correspond pas à son extension.");
            }
            String newFilename = UUID.randomUUID().toString() + extension;

            Path destinationFile = this.rootLocation.resolve(Paths.get(newFilename)).normalize().toAbsolutePath();
            Files.copy(file.getInputStream(), destinationFile);
            
            // On renvoie l'URL relative qui sera servie par le backend
            return "/uploads/" + newFilename;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du fichier.", e);
        }
    }

    private boolean hasAllowedSignature(MultipartFile file, String extension) throws IOException {
        byte[] header = file.getBytes();
        int length = Math.min(header.length, 8);
        byte[] signature = new byte[length];
        System.arraycopy(header, 0, signature, 0, length);

        if (extension.equals(".jpg") || extension.equals(".jpeg")) {
            return length >= 3 && (signature[0] & 0xff) == 0xff
                    && (signature[1] & 0xff) == 0xd8
                    && (signature[2] & 0xff) == 0xff;
        }
        if (extension.equals(".png")) {
            return length >= 8
                    && (signature[0] & 0xff) == 0x89
                    && signature[1] == 0x50
                    && signature[2] == 0x4e
                    && signature[3] == 0x47
                    && signature[4] == 0x0d
                    && signature[5] == 0x0a
                    && signature[6] == 0x1a
                    && signature[7] == 0x0a;
        }
        if (extension.equals(".pdf")) {
            return length >= 5
                    && signature[0] == 0x25
                    && signature[1] == 0x50
                    && signature[2] == 0x44
                    && signature[3] == 0x46
                    && signature[4] == 0x2d;
        }
        return false;
    }
}
