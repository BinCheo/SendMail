import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

 


public class Application {

	public static void main(String[] args) throws MessagingException {
		String subject = "SendMailSubject";
		String text = "Welcome SendMail";
		String html = "</br><h3>Project SendMail</h3>";
		String[] file = {"data/text_20200309.pdf"};
		InternetAddress[] address = {new InternetAddress("example@fpt.edu.vn")};
		sendMail(subject, text, html, file, address);

	}
	
	public static void sendMail(String subject, String text, String html, String[] file, InternetAddress[] address) throws MessagingException {
		if(address.length < 1) {
			System.out.println("There is no email address");
			return;
		}
		
		// Get properties object
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", MailConfig.HOST_NAME);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", MailConfig.TSL_PORT);
 
        // get Session
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MailConfig.APP_EMAIL, MailConfig.APP_PASSWORD);
            }
        });
              
        try {
        	
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MailConfig.APP_EMAIL));
            message.addRecipients(Message.RecipientType.TO, address);
            if(subject != null) {
            	message.setSubject(subject);
            }
            Multipart multipart = new MimeMultipart();
            
         // 3) create MimeBodyPart object and set your message text
            if(text != null) {
            	BodyPart messageBodyPart0 = new MimeBodyPart();
                messageBodyPart0.setText(text);
                multipart.addBodyPart(messageBodyPart0);
            }
            if(html != null) {
            	BodyPart messageBodyPart1 = new MimeBodyPart();
                messageBodyPart1.setContent(html,"text/html; charset=UTF-8");
                multipart.addBodyPart(messageBodyPart1);
            }
 
            
            	if(file != null) {
                	for(int i = 0; i < file.length; i++) {
                   	 MimeBodyPart messageBodyPart2 = new MimeBodyPart();                 	
                   	 String filename = file[i];
                   	 if(!filename.trim().equals("")) {
                   		DataSource source = new FileDataSource(filename);
                        messageBodyPart2.setDataHandler(new DataHandler(source));
                        messageBodyPart2.setFileName(filename);
                        multipart.addBodyPart(messageBodyPart2);
                   	 }
                   }
                }

            
            
            // 6) set the multiplart object to the message object
            message.setContent(multipart);
 
            // 7) send message
            Transport.send(message);
 
            System.out.println("Message sent successfully");
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        
        
	  }

}
