package com.cognizant.md.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
//	@Autowired
//	private JavaMailSender mailSender;
//	
//	public void sendHtmlInMail(String to, String subject, String body) throws MessagingException {
//		MimeMessage msg = mailSender.createMimeMessage();
//		MimeMessageHelper helper = new MimeMessageHelper(msg);
//		helper.setFrom("techsoftmd22@gmail.com");
//		helper.setTo(to);
//		helper.setText(body,true);
//		helper.setSubject(subject);
//	}
//	
//	public void sendMail(String to, String subject, String body) {
//		SimpleMailMessage msg = new SimpleMailMessage();
//		msg.setFrom("techsoftmd22@gmail.com");
//		msg.setTo(to);
//		msg.setSubject(subject);
//		msg.setText(body);
//		mailSender.send(msg);
//		System.out.println("Mail Sent Successfully...");
//	}
//	
//	public void sendMailWithAttachment(String to, String subject, String body, String attachmentPath) throws MessagingException {
//		MimeMessage msg = mailSender.createMimeMessage();
//		MimeMessageHelper helper = new MimeMessageHelper(msg,true);
//		helper.setFrom("techsoftmd22@gmail.com");
//		helper.setTo(to);
//		helper.setText(body);
//		helper.setSubject(subject);
//		
//		FileSystemResource fileSystem = new FileSystemResource(new File(attachmentPath));
//		helper.addAttachment(fileSystem.getFilename(), fileSystem);
//		mailSender.send(msg);
//		System.out.println("Mail Sent Successfully...");
//	}
//	
//	public void sendMailBuilder(String to, String subject, String body) {
//		
//		Mailer mailer = (Mailer) MailerBuilder
//		          .withSMTPServer("smtp.gmail.com", 465, "techsoftmd22@gmail.com", "TechSoftMd22")
//		          .withProxy("proxy.cognizant.com", 6050, "2072071@cognizant.com", "Naseer@1")
//		          .withSessionTimeout(10 * 1000)
//		          .buildMailer();
//
//		Email email = EmailBuilder.startingBlank()
//				 .to(to)
//				 .withSubject(subject)
//				 .withPlainText(body)
//		         .buildEmail();
//		
//		mailer.sendMail(email);
//		System.out.println("Message sent...");
//	}
}
