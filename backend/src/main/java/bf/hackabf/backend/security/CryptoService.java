package bf.hackabf.backend.security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.IESParameterSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

@Service
public class CryptoService {

    private static final Logger logger = LoggerFactory.getLogger(CryptoService.class);
    private static final String PROVIDER = BouncyCastleProvider.PROVIDER_NAME;
    private static final String CURVE_NAME = "secp256r1";

    private KeyPair eccKeyPair;
    private IESParameterSpec iesParamSpec;

    @PostConstruct
    public void init() {
        try {
            // Enregistrement du fournisseur Bouncy Castle
            if (Security.getProvider(PROVIDER) == null) {
                Security.addProvider(new BouncyCastleProvider());
            }

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC", PROVIDER);
            keyGen.initialize(new ECGenParameterSpec(CURVE_NAME), new SecureRandom());
            this.eccKeyPair = keyGen.generateKeyPair();

            // Paramètres ECIES (nonce aléatoire)
            byte[] nonce = new byte[16];
            new SecureRandom().nextBytes(nonce);
            this.iesParamSpec = new IESParameterSpec(null, null, 256, 256, nonce, false);

            logger.info("🔐 Paire de clés ECC (courbe {}) initialisée avec succès.", CURVE_NAME);
        } catch (Exception e) {
            logger.error("❌ Échec critique de l'initialisation de la cryptographie ECC", e);
            throw new IllegalStateException("Impossible d'initialiser la sécurité ECC", e);
        }
    }

    public KeyPair getEccKeyPair() {
        return eccKeyPair;
    }

    public String encryptEcies(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("ECIES", PROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE, eccKeyPair.getPublic(), iesParamSpec);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            logger.error("Erreur lors du chiffrement ECIES", e);
            throw new RuntimeException("Erreur de chiffrement des données", e);
        }
    }

    public String decryptEcies(String encryptedBase64) {
        try {
            Cipher cipher = Cipher.getInstance("ECIES", PROVIDER);
            cipher.init(Cipher.DECRYPT_MODE, eccKeyPair.getPrivate(), iesParamSpec);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedBase64));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error("Erreur lors du déchiffrement ECIES", e);
            throw new RuntimeException("Erreur de déchiffrement des données", e);
        }
    }
}