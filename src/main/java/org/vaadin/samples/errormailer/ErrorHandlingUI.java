package org.vaadin.samples.errormailer;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * This is a simple application that uses general error handling to catch and
 * notify all errors in a Vaadin application via email.
 *
 * Every time exception occurs, in addition to default handling, we send a email
 * to specified address.
 *
 * Read more about Vaadin exception handling at
 * https://vaadin.com/book/-/page/application.errors.html
 */
@Theme("valo")
@SuppressWarnings("serial")
public class ErrorHandlingUI extends UI {

    private static final String APPLICATION_EMAIL_TO = "sami+testto@vaadin.com";
    private static final String APPLICATION_EMAIL_FROM = "sami+testfrom@vaadin.com";
    private static final String APPLICATION_EMAIL_SUBJECT = "Uncaught application exception";

    private static final String APPLICATION_EMAIL_BODY_START = "There was an uncaugh exception in your application:\n\n";
    private static final String APPLICATION_EMAIL_BODY_END = "\n\n(This is an automated error message.)";

    private int clickCounter = 0;
    private Label clickCounterLabel;

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = ErrorHandlingUI.class)
    public static class Servlet extends VaadinServlet {

    }

    @Override
    protected void init(VaadinRequest request) {

        // Configure a application global error handler
        // Configure the error handler for the UI
        UI.getCurrent().setErrorHandler(new DefaultErrorHandler() {
            @Override
            public void error(com.vaadin.server.ErrorEvent event) {

                // Send an email telling about the error
                sendEmail(findAbstractComponent(event), event.getThrowable());

                // Do the default error handling (optional)
                doDefault(event);
            }

        });

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);

        layout.addComponent(new Label("Hello World!"));
        layout.addComponent(new Label("Greetings from server."));
        layout.addComponent(new Label("I have "
                + Runtime.getRuntime().availableProcessors()
                + " processors and "
                + (Runtime.getRuntime().totalMemory() / 1000000)
                + " MB total memory."));

        Button button = new Button("Generate a NullPointerException");
        button.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                clickCounter++;
                clickCounterLabel.setValue("Clicks: " + clickCounter);
                Notification.show("Thank you for clicking.");
                ((String) null).length(); // This will cause NPE
            }
        });

        layout.addComponent(button);
        layout.addComponent(clickCounterLabel = new Label("Clicks: 0"));
    }

    /**
     * Send email about an error using JavaMail API.
     *
     * @param origin Original component that caused the problem.
     * @param t Exception that was thrown.
     */
    private void sendEmail(AbstractComponent origin, Throwable t) {

        Properties props = new Properties();

        /* Here we use basic localhost for sending out the email, but 
         depending your mail server setup you might need something extra
         for SSL and authentication. */
        props.setProperty("mail.smtp.host", "localhost");
        Session session = Session.getDefaultInstance(props);

        /* For example for a Gmail account setup:
         props.put("mail.smtp.auth", "true");
         props.put("mail.smtp.starttls.enable", "true");
         props.put("mail.smtp.host", "smtp.gmail.com");
         props.put("mail.smtp.port", "587");
         Session session = Session.getInstance(props,
         new javax.mail.Authenticator() {
         protected PasswordAuthentication getPasswordAuthentication() {
         return new PasswordAuthentication(GMAIL_ADDRESS, GMAIL_PASSWORD);
         }
         });
         */
        // Format the error message for email
        StringWriter messageBody = new StringWriter();
        messageBody.write("Component class: ");
        messageBody.write(origin.getClass().getCanonicalName());
        messageBody.write("\nUI class: ");
        messageBody.write(origin.getUI().getClass().getCanonicalName());
        messageBody.write("\n\n");
        t.printStackTrace(new PrintWriter(messageBody));

        // Send the email using JavaMail Transport.
        // TODO: This might be a long operation and we are not interested in the result of it.
        // Consider using separate Thread here.
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(APPLICATION_EMAIL_FROM));
            InternetAddress[] address = {new InternetAddress(APPLICATION_EMAIL_TO)};
            message.setRecipients(Message.RecipientType.TO, address);
            message.setSubject(APPLICATION_EMAIL_SUBJECT);
            message.setSentDate(new Date());
            message.setText(APPLICATION_EMAIL_BODY_START + messageBody + APPLICATION_EMAIL_BODY_END);
            Transport.send(message);
        } catch (MessagingException ex) {
            // Exception during exception handling. Just log it.
            Logger.getLogger(ErrorHandlingUI.class.getName()).log(Level.SEVERE, "Exception when sending email", ex);
        }
    }

}
