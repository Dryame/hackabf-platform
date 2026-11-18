package bf.hackabf.backend.security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

@Service
public class CryptoService {

    private static final Logger logger = LoggerFactory.getLogger(CryptoService.class);
    private static final String PROVIDER = BouncyCastleProvider.PROVIDER_NAME;
    private static final String CURVE_NAME = "secp256r1";

    private KeyPair eccKeyPair;

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

            logger.info("🔐 Paire de clés ECC (courbe {}) initialisée avec succès.", CURVE_NAME);
        } catch (Exception e) {
            logger.error("❌ Échec critique de l'initialisation de la cryptographie ECC", e);
            throw new IllegalStateException("Impossible d'initialiser la sécurité ECC", e);
        }
    }

    public KeyPair getEccKeyPair() {
        return eccKeyPair;
    }

}