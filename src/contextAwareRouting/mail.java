package contextAwareRouting;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
 
public class mail {
 
	public mail() {
 
		final String username = "cavecomputers@gmail.com";
		final String password = "setthealarms";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("cavecomputers@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("tmitchell206@gmail.com"));
			message.setSubject("Code Done");
			message.setText("Computer 1 is done");
 
			Transport.send(message);
 
			System.out.println("Mail");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}