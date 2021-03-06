package edu.wpi.cs3733.d19.teamM.utilities;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.imageio.ImageIO;

import edu.wpi.cs3733.d19.teamM.utilities.AStar.Path;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.PathToString;
import externalLibraries.ImgurUploader.*;

import org.json.*;

import edu.wpi.cs3733.d19.teamM.Main;
import com.sendgrid.*;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.apache.commons.codec.binary.Base64;


/**
 * This class sends an email
 */
public class SendEmail extends Thread {

    public static final String ACCOUNT_SID =
            "ACbfbd0226f179ee74597c887298cbda10";
    public static final String AUTH_TOKEN =
            "eeb459634d5a8407d077635504386d44";

    String type;
    String input;
    Path p;

    /**
     * The constructor takes in the email of the individual we want to send the email to
     */
    public SendEmail(String type, String input, Path p)
    {
        this.type = type;
        this.input = input;
        this.p = p;
    }

    /**
     * Method that, when given string input, will send an email with the map to the given address
     * @param toEmail
     */
    public void sendMail(String toEmail)
    {
        if(toEmail.equals(""))
        {
            return;
        }

        String sendgrid_username  = "sgehlywpi";
        String sendgrid_password  = "MangoManticores1!";
        String to = toEmail;
        SendGrid sendgrid = new SendGrid(sendgrid_username, sendgrid_password);
        SendGrid.Email email = new SendGrid.Email();

        email.addTo(to);
        email.setFrom(to);
        email.setFromName("Brigham & Women's");
        email.setReplyTo("mangomanticores@gehly.net");
        email.setSubject("Hospital Directions");
        email.setHtml("Hello, \n\n" + PathToString.getDirections(p).replaceAll("\n","<br><br>") + "\n\n Thank You,\n Staff at Brigham and Women's");

        try {
            SendGrid.Response response = sendgrid.send(email);
        } catch (SendGridException e) {
            System.out.println(e);
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
            image = ImageIO.read(Main.getResource("/resources/maps/01_thefirstfloor.png")); //Actual Image
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage overlay = null;
        try {
            overlay = ImageIO.read(new File("PathOutput.png")); //Overlay (The Path)
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
            /*AffineTransform at = new AffineTransform();
            at.scale(0.5, 0.5);
            BufferedImage after = new BufferedImage(5000/2, 3400/2, BufferedImage.TYPE_INT_ARGB);

            AffineTransformOp scaleOp =
                    new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            combined = scaleOp.filter(combined, after);*/

            ImageIO.write(combined, "PNG", new File("EmailOutput.png")); //Where the file is being written to
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendSMS(String phoneNumber){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        try{

            //combineImages();

            //String response = Uploader.upload(new File("EmailOutput.png"));

            //String encodedfile = new String(Base64.encodeBase64(Files.readAllBytes(new File("EmailOutput.png").toPath())), "UTF-8");

            //JSONObject obj = new JSONObject(response);
            //System.out.println(response);
            //String link = obj.getJSONObject("data").getString("link").replace(".png","h.png");
            Message message = Message
                    .creator(new PhoneNumber(phoneNumber), // to
                            new PhoneNumber("+15085383787"), // from
                            PathToString.getDirections(p))
                    .create();

            //System.out.println(message.getSid());
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        if(this.type == null || this.input == null){return;};

        if(this.type == "email"){
            this.sendMail(this.input);
        }
        if(this.type == "phone"){
            this.sendSMS("+1"+this.input);
        }
    }

}