package bf.hackabf.backend.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service gérant la création, la signature et la validation des Tokens JWT.
 * Utilise l'algorithme ES256 (ECDSA) avec les clés asymétriques fournies par CryptoService.
 */
@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private final CryptoService cryptoService;
    private static final long EXPIRATION_TIME_MS = 86400000; // 24 heures

    public JwtService(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    /**
     * Génère un Token JWT signé avec la clé privée ECC.
     */
    public String generateToken(UserDetails userDetails) {
        try {
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(userDetails.getUsername())
                    .claim("roles", roles)
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                    .build();

            ECPrivateKey privateKey = (ECPrivateKey) cryptoService.getEccKeyPair().getPrivate();
            ECKey jwk = new ECKey.Builder(Curve.P_256, (ECPublicKey) cryptoService.getEccKeyPair().getPublic())
                    .privateKey(privateKey)
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.ES256),
                    claims
            );

            signedJWT.sign(new ECDSASigner(jwk));
            return signedJWT.serialize();
        } catch (Exception e) {
            logger.error("Erreur lors de la génération du JWT", e);
            throw new RuntimeException("Erreur de génération du token", e);
        }
    }

    /**
     * Valide la signature et l'expiration du Token JWT.
     */
    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            ECPublicKey publicKey = (ECPublicKey) cryptoService.getEccKeyPair().getPublic();
            ECDSAVerifier verifier = new ECDSAVerifier(publicKey);

            if (!signedJWT.verify(verifier)) {
                logger.warn("Signature JWT invalide");
                return false;
            }
            if (signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date())) {
                logger.warn("Token JWT expiré");
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("Erreur de validation du JWT", e);
            return false;
        }
    }

    public String extractUsername(String token) {
        try {
            return SignedJWT.parse(token).getJWTClaimsSet().getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}
