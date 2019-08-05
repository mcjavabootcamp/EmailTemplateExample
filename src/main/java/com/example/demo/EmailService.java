package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService {

    private TemplateEngine templateEngine;

    //connecting your environment to this class (application properties)
    @Autowired
    Environment env;

    //uses your template engine and connects it to this class
    @Autowired
    public EmailService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }


    private Properties GetProperties() {
        // adds the properties to get a new session later
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", env.getProperty("mail.smtp.starttls.enable"));
        props.put("mail.smtp.auth", env.getProperty("mail.smtp.auth"));
        props.put("mail.smtp.host", env.getProperty("mail.smtp.host"));
        props.put("mail.smtp.port", env.getProperty("mail.smtp.port"));
        // doesn't work without this line adds the ability to trust the server
        props.put("mail.smtp.ssl.trust", env.getProperty("mail.smtp.ssl.trust"));
        //the env.get pulls the information from your application properties with the specific name in the string

        return props;

    }

    private Session GetSession() {

        //Email Account Credentials ( it will be the credentials for the person you are sending the email from)
        final String username = "youremail@gmail.com";
        final String password = "password";

        //create session with username and passsword
        Session session = Session.getInstance(GetProperties(), new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        return session;
    }

    //processing it with a template engine and making it look pretty
    public String BuildTemplateWithContent(String message) {
        Context context = new Context();
        context.setVariable("message", message);
        return templateEngine.process("mailtemplate", context);
    }

    public void SendSimpleEmail () {
        try {
            Message message = new MimeMessage(GetSession());

            //email address you're sending from
            message.setFrom(new InternetAddress("youremail@gmail.com"));

            //email address you're sending email to ( to user email )
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("recievingemail@gmail.com"));

            //email  subject
            message.setSubject("Email confirmation");

            //email content
            //if you want to set your text here and you don't care for user to write it in
            message.setText("sent email");

            //sends the message
            Transport.send(message);

            //below catches if the email doesnt get sent, so it must be a valid email
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}