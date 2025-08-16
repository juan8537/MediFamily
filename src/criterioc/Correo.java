/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package criterioc;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 *
 * @author camey
 */
public class Correo {
    
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;
    private static final String USERNAME = "medicamentorecordatorio@gmail.com"; 
    private static final String PASSWORD = "rfyx zczo vvqk qwae"; 
    
    
    public static boolean sendMedicationReminder(String toEmail, String patientName, 
            String medName, String dose, String schedule) {
        
        String subject = "💊 Recordatorio: " + medName;
        String body = String.format(
            "Hola %s,\n\nRecuerda tomar tu medicamento:\n\n" +
            "🔹 Medicamento: %s\n🔹 Dosis: %s\n🔹 Horario: %s\n\n" +
            "¡No olvides seguir las indicaciones de tu doctor!\n\n" +
            "Saludos,\nSistema de Recordatorios Médicos",
            patientName, medName, dose, schedule
        );

        // Configuración de propiedades
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.ssl.trust", SMTP_HOST); 

        // Crear sesión
        Session session = Session.getInstance(props,
            new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

        try {
            // Crear mensaje
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            // Enviar mensaje
            Transport.send(message);
            System.out.println("Correo enviado a: " + toEmail);
            return true;
        } catch (MessagingException e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
            // Detalle adicional del error
            if (e.getCause() != null) {
                System.err.println("Causa: " + e.getCause().getMessage());
            }
            return false;
        }
    }
    
   
   
    
}


    

