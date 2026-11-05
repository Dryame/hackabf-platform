package bf.hackabf.backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // @Async permet d'envoyer l'email en arrière-plan sans bloquer la création du hackathon
    @Async
    public void envoyerEmailNouveauHackathon(String titreHackathon, String description, String destinataire) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(destinataire);
            message.setSubject("🚀 Nouveau Hackathon sur HackaBF : " + titreHackathon);
            message.setText("Bonjour,\n\nUn nouveau hackathon vient d'être publié sur HackaBF !\n\n" +
                    "Titre : " + titreHackathon + "\n" +
                    "Description : " + description + "\n\n" +
                    "Connectez-vous vite sur la plateforme pour vous inscrire !\n\n" +
                    "L'équipe HackaBF.");
            
            mailSender.send(message);
            System.out.println("✅ Email envoyé à " + destinataire);
        } catch (Exception e) {
            System.err.println("❌ Erreur d'envoi d'email : " + e.getMessage());
        }
    }
}
