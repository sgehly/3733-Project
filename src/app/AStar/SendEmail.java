package app.AStar;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.*;

/**
 * This class sends an email
 */
public class SendEmail {


    /**
     * The constructor takes in the email of the individual we want to send the email to
     */
    public SendEmail()
    { }

    /**
     * Method that, when given string input, will send an email with the map to the given address
     * @param email
     */
    public void sendMail(String email)
    {
        if(email.equals(""))
        {
            return;
        }
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
            JOptionPane.showMessageDialog(null,"Your Email will be Sent to You in Approximately 10 Seconds");
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
            combineImages();
            //Send the attachement
            MimeBodyPart imageAttachment = new MimeBodyPart();
            imageAttachment.attachFile("src/resources/maps/emailOutput.png"); //Specify the path over here
            System.out.println("attached file");
            //Attach all the body parts together
            emailContent.addBodyPart(textBodyPart);
            emailContent.addBodyPart(imageAttachment);

            //Attach the multipart to the message
            msg.setContent(emailContent);
            System.out.println("Set Content");
            Transport.send(msg);
            System.out.println("Sent Mail");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * For the first iteration, which is only for one floor, this will combine the images for the two floors and save as another image meant for email
     */
    private void combineImages()
    {
    // load source images
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("src/resources/maps/02_thesecondfloor.png")); //Actual Image
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage overlay = null;
        try {
            overlay = ImageIO.read(new File("src/resources/maps/PathOutput.png")); //Overlay (The Path)
        } catch (IOException e) {
            e.printStackTrace();
        }

        // create the new image, canvas size is the max. of both image sizes
        int w = Math.max(image.getWidth(), overlay.getWidth());
        int h = Math.max(image.getHeight(), overlay.getHeight());
        BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        // paint both images, preserving the alpha channels
        Graphics g = combined.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.drawImage(overlay, 0, 0, null);

    // Save as new image
        try {
            ImageIO.write(combined, "PNG", new File("src/resources/maps/emailOutput.png")); //Where the file is being written to
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
