package bf.hackabf.backend.config;

import jakarta.annotation.PostConstruct;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Configuration;
import java.security.Security;

@Configuration
public class CryptoConfig {
    @PostConstruct
    public void init() {
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.addProvider(new BouncyCastleProvider());
        System.out.println("✅ Bouncy Castle initialisé pour la cryptographie ECC.");
    }
}