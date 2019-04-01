package app.AStar;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * This class sends an email
 */
public class SendEmail {

    public static void main(String[] args) {
    sendMail("yvsp26@gmail.com");
    }

    /**
     * The constructor takes in the email of the individual we want to send the email to
     */
    public SendEmail(String email)
    {


    }

    /**
     * Method that, when given string input, will send an email with the map to the given address
     * @param email
     */
    public static void sendMail(String email)
    {
        //auth info
        final String username = "BrighamAndWomensKiosk";
        final String password = "mangoManticores";
        //From and to emails
        String fromEmail = "BrighamAndWomensKiosk@gmail.com";
        String toEmail = email;


        //Set the properties for the auth
        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        //Create a session
       Session session = Session.getInstance(properties, new javax.mail.Authenticator()
       {    //Deal with the authentication for the mail (or else gmail etc. will be unhappy)
           protected PasswordAuthentication getPasswordAuthentication()
           {
               return new PasswordAuthentication(username,password);
           }
       });
       //Let's create the mail message
        MimeMessage msg = new MimeMessage(session);
        //Let's try to actually send the email now
        try
        {
            msg.setFrom(new InternetAddress(fromEmail)); //use the provided email to send the message
            //Try to keep the above as the same client that was used for the server (gmail)
            msg.setRecipient(Message.RecipientType.TO,new InternetAddress(toEmail));//Set the message recipient
            msg.setSubject("Directions to Requested Location"); //Set the subject line

            Multipart emailContent = new MimeMultipart();

            //Text body content
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText("Hello, \n\n Attached to this email is an image that outlines the path to the destination you requested. On behalf of everyone at Brigham and Women's Hospital, have a good day! \n \n Thank You,\n Staff at Brigham and Women's"); //The body of the email

            //Create the image body part
            //Get the overlayed images and store them in resources

            //Send the attachement
            MimeBodyPart imageAttachment = new MimeBodyPart();
            imageAttachment.attachFile("src/resources/maps/00_thegroundfloor.png"); //Specify the path over here

            //Attach all the body parts together
            emailContent.addBodyPart(textBodyPart);
            emailContent.addBodyPart(imageAttachment);

            //Attach the multipart to the message
            msg.setContent(emailContent);

            Transport.send(msg);
            System.out.println("Sent Mail");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }




}
