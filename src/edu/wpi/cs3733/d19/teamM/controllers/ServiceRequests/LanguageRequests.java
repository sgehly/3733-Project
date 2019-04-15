package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import com.darkprograms.speech.translator.GoogleTranslate;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;

import java.awt.*;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class LanguageRequests implements Initializable {

    @FXML
    private Text userText;

    @FXML
    private javafx.scene.control.Label lblClock;

    @FXML
    private javafx.scene.control.Label lblDate;

    @FXML
    private Text errorMessage;

    @FXML
    private JFXTextField translateText;


    ObservableList<String> languages = FXCollections.observableArrayList(
            "Afrikaans", "Akan", "Albanian","Amharic","Arabic", "Azerbaijani", "Basque",
             "Bengali","Bosnian", "Bulgarian","Burmese", "Chamorro", "Cree", "Croatian", "Czech",  "Danish",
             "Dutch","English", "Estonian", "Ewe",  "Farsi", "Finnish", "Flemish", "French", "French Canadian",
            "Fula", "Fulani", "Fuzhou", "Gaelic", "Gaelic-scottish", "Georgian", "German", "Gorani", "Greek", "Gujarati",
            "Haitian Creole", "Hausa", "Hebrew", "Hindi", "Hungarian", "Ibanag", "Ibo", "Icelandic", "Igbo",
            "Indonesian", "Inuktitut", "Italian", "Japanese", "Javanese", "Kashmiri", "Kazakh", "Kikuyu",
            "Kinyarwanda", "Korean", "Kurdish", "Kyrgyz",
            "Latvian", "Lingala", "Lithuanian", "Macedonian", "Malay", "Malayalam", "Maltese",
            "Marathi", "Marshallese", "Mongolian", "Navajo", "Nepali",
            "Norwegian", "Oromo", "Pashto", "Patois", "Polish", "Portuguese",
            "Punjabi", "Romanian", "Russian", "Samoan", "Serbian", "Shona", "Sichuan", "Sinhalese", "Slovak",
            "Somali", "Spanish", "Sundanese", "Swahili", "Swedish", "Tagalog", "Tajik", "Tamil",
            "Telugu", "Thai", "Tibetan", "Tigrinya", "Tongan", "Turkish", "Twi", "Ukrainian", "Urdu", "Uyghur", "Uzbek",
            "Vietnamese", "Welsh", "Wolof", "Yiddish", "Yoruba");

    private String getISOEncoding(String input)
    {
        String returnString = "";

        if(input.equals("Afrikaans"))
        {
            returnString = "af";
        }
        else if(input.equals("Akan"))
        {
            returnString = "ak";
        }
        else if(input.equals("Albanian"))
        {
            returnString = "sq";
        }
        else if(input.equals("Amharic"))
        {
            returnString = "am";
        }
        else if(input.equals("Arabic"))
        {
            returnString = "ar";
        }
        else if(input.equals("Azerbaijani"))
        {
            returnString = "az";
        }
        else if(input.equals("Basque"))
        {
            returnString = "eu";
        }
        else if(input.equals("Bengali"))
        {
            returnString = "bn";
        }
        else if(input.equals("Bosnian"))
        {
            returnString = "bs";
        }
        else if(input.equals("Bulgarian"))
        {
            returnString = "bg";
        }
        else if(input.equals("Burmese"))
        {
            returnString = "my";
        }
        else if(input.equals("Chamorro"))
        {
            returnString = "ch";
        }
        else if(input.equals("Cree"))
        {
            returnString = "cr";
        }
        else if(input.equals("Croatian"))
        {
            returnString = "hr";
        }
        else if(input.equals("Czech"))
        {
            returnString = "cs";
        }
        else if(input.equals("Danish"))
        {
            returnString = "da";
        }
        else if(input.equals("Dutch"))
        {
            returnString = "nl";
        }
        else if(input.equals("English"))
        {
            returnString = "en";
        }
        else if(input.equals("Estonian"))
        {
            returnString = "et";
        }
        else if(input.equals("Ewe"))
        {
            returnString = "ee";
        }
        else if(input.equals("Farsi"))
        {
            returnString = "fa";
        }
        else if(input.equals("Finnish"))
        {
            returnString = "fi";
        }
        else if(input.equals("Flemish"))
        {
            returnString = "nl";
        }
        else if(input.equals("French"))
        {
            returnString = "fr";
        }
        else if(input.equals("French Canadian"))
        {
            returnString = "fr";
        }
        else if(input.equals("Fula"))
        {
            returnString = "ff";
        }
        else if(input.equals("Gaelic"))
        {
            returnString = "gd";
        }
        else if(input.equals("Gaelic-scottish"))
        {
            returnString = "gd";
        }
        else if(input.equals("Georgian"))
        {
            returnString = "ka";
        }
        else if(input.equals("German"))
        {
            returnString = "de";
        }
        else if(input.equals("Greek"))
        {
            returnString = "el";
        }
        else if(input.equals("Gujarati"))
        {
            returnString = "gu";
        }
        else if(input.equals("Haitian Creole"))
        {
            returnString = "ht";
        }
        else if(input.equals("Hausa"))
        {
            returnString = "ha";
        }
        else if(input.equals("Hebrew"))
        {
            returnString = "he";
        }
        else if(input.equals("Hindi"))
        {
            returnString = "hi";
        }
        else if(input.equals("Hungarian"))
        {
            returnString = "hu";
        }
        else if(input.equals("Ibo"))
        {
            returnString = "ig";
        }
        else if(input.equals("Icelandic"))
        {
            returnString = "is";
        }
        else if(input.equals("Igbo"))
        {
            returnString = "ig";
        }
        else if(input.equals("Indonesian"))
        {
            returnString = "id";
        }
        else if(input.equals("Inuktitut"))
        {
            returnString = "iu";
        }
        else if(input.equals("Italian"))
        {
            returnString = "it";
        }
        else if(input.equals("Japanese"))
        {
            returnString = "ja";
        }
        else if(input.equals("Javanese"))
        {
            returnString = "jv";
        }
        else if(input.equals("Kashmiri"))
        {
            returnString = "ks";
        }
        else if(input.equals("Kazakh"))
        {
            returnString = "kk";
        }
        else if(input.equals("Kikuyu"))
        {
            returnString = "ki";
        }
        else if(input.equals("Kinyarwanda"))
        {
            returnString = "rw";
        }
        else if(input.equals("Korean"))
        {
            returnString = "ko";
        }
        else if(input.equals("Kurdish"))
        {
            returnString = "ku";
        }
        else if(input.equals("Kyrgyz"))
        {
            returnString = "ky";
        }
        else if(input.equals("Latvian"))
        {
            returnString = "lv";
        }
        else if(input.equals("Lingala"))
        {
            returnString = "ln";
        }
        else if(input.equals("Lithuanian"))
        {
            returnString = "lt";
        }
        else if(input.equals("Macedonian"))
        {
            returnString = "mk";
        }
        else if(input.equals("Malay"))
        {
            returnString = "ms";
        }
        else if(input.equals("Malayalam"))
        {
            returnString = "ml";
        }
        else if(input.equals("Maltese"))
        {
            returnString = "mt";
        }
        else if(input.equals("Marathi"))
        {
            returnString = "mr";
        }
        else if(input.equals("Marshallese"))
        {
            returnString = "mh";
        }
        else if(input.equals("Mongolian"))
        {
            returnString = "mn";
        }
        else if(input.equals("Navajo"))
        {
            returnString = "nv";
        }
        else if(input.equals("Nepali"))
        {
            returnString = "ne";
        }
        else if(input.equals("Norwegian"))
        {
            returnString = "nn";
        }
        else if(input.equals("Oromo"))
        {
            returnString = "om";
        }
        else if(input.equals("Pashto"))
        {
            returnString = "ps";
        }
        else if(input.equals("Polish"))
        {
            returnString = "pl";
        }
        else if(input.equals("Portuguese"))
        {
            returnString = "pt";
        }
        else if(input.equals("Punjabi"))
        {
            returnString = "pa";
        }
        else if(input.equals("Romanian"))
        {
            returnString = "ro";
        }
        else if(input.equals("Russian"))
        {
            returnString = "ru";
        }
        else if(input.equals("Samoan"))
        {
            returnString = "sm";
        }
        else if(input.equals("Serbian"))
        {
            returnString = "sr";
        }
        else if(input.equals("Shona"))
        {
            returnString = "sn";
        }
        else if(input.equals("Sichuan"))
        {
            returnString = "ii";
        }
        else if(input.equals("Sinhalese"))
        {
            returnString = "si";
        }
        else if(input.equals("Slovak"))
        {
            returnString = "sk";
        }
        else if(input.equals("Somali"))
        {
            returnString = "so";
        }
        else if(input.equals("Spanish"))
        {
            returnString = "es";
        }
        else if(input.equals("Sundanese"))
        {
            returnString = "su";
        }
        else if(input.equals("Swahili"))
        {
            returnString = "sw";
        }
        else if(input.equals("Swedish"))
        {
            returnString = "sv";
        }
        else if(input.equals("Tagalog"))
        {
            returnString = "tl";
        }
        else if(input.equals("Tajik"))
        {
            returnString = "tg";
        }
        else if(input.equals("Tamil"))
        {
            returnString = "ta";
        }
        else if(input.equals("Telugu"))
        {
            returnString = "te";
        }
        else if(input.equals("Thai"))
        {
            returnString = "th";
        }
        else if(input.equals("Tibetan"))
        {
            returnString = "bo";
        }
        else if(input.equals("Tigrinya"))
        {
            returnString = "ti";
        }
        else if(input.equals("Tongan"))
        {
            returnString = "to";
        }
        else if(input.equals("Turkish"))
        {
            returnString = "tr";
        }
        else if(input.equals("Twi"))
        {
            returnString = "tw";
        }
        else if(input.equals("Ukrainian"))
        {
            returnString = "uk";
        }
        else if(input.equals("Urdu"))
        {
            returnString = "ur";
        }
        else if(input.equals("Uyghur"))
        {
            returnString = "ug";
        }
        else if(input.equals("Uzbek"))
        {
            returnString = "uz";
        }
        else if(input.equals("Vietnamese"))
        {
            returnString = "vi";
        }
        else if(input.equals("Welsh"))
        {
            returnString = "cy";
        }
        else if(input.equals("Wolof"))
        {
            returnString = "wo";
        }
        else if(input.equals("Yiddish"))
        {
            returnString = "yi";
        }
        else if(input.equals("Yoruba"))
        {
            returnString = "yo";
        }

        return returnString;
    }

    //Tesxt Field for flower type input
    @FXML
    private TextField Language;
    @FXML
    private ListView available;


    //Text field for room location input
    @FXML
    private TextField room;

    @FXML
    private JFXCheckBox Urgent;

    //Text field for additional specifications
    @FXML
    private javafx.scene.control.TextArea notes;

    @FXML
    private Button submitReuqest;



    @FXML
    public void quickTranslateToEnglish()
    {
        String inputText = translateText.getText();

        try {
            String translatedText = GoogleTranslate.translate(inputText);
            translateText.setText(translatedText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void quickTranslateToTarget()
    {
        String inputText = translateText.getText();


        if(Language.getText().length()>0)
        {
            try {
                String translatedText = GoogleTranslate.translate(getISOEncoding(Language.getText()),inputText);
                translateText.setText(translatedText);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            translateText.setText("Please input a language above");
        }


    }



    /**
     * This method is for the logout button which allows the user to go back to the welcome screen
     * @throws Exception: Any exception that is encountered
     */
    @FXML
    public void logout() throws Exception {
        Main.setScene("welcome");
    }

    /**
     * This method is for the logout button which allows the user to go back to the welcome screen
     * @throws Exception: Any exception that is encountered
     */
    @FXML
    private void navigateBack() throws Exception {
        Main.setScene("serviceRequests");
    }

    @FXML
    private void goToList() throws Exception {
        Main.setScene("serviceRequestsList");
    }


    /**
     * This method allows the user to create a flowers request using the button
     * @param : The action that is associated with making the flowers request
     */

    @FXML
    public void makeLanguageRequest() throws IOException {

        try {
            Exception e = new Exception();
            if (areFieldsEmpty()) {
                errorMessage.setText("You didn't answer all the required fields.");
                throw e;
            }
            new ServiceRequests().makeRequest("interpreter", room.getText(), Language.getText(), notes.getText(), Urgent.isSelected());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean areFieldsEmpty() {
        return room.getText().isEmpty() || Language.getText().isEmpty();
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
//TODO add user text
        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());
        ObservableList<String> list = FXCollections.observableArrayList();

        String query = "select * FROM users Where ACCOUNTINT = ?";
        Connection conn = new DatabaseUtils().getConnection();
        try{
            PreparedStatement s = conn.prepareStatement(query);
            s.setInt(1, 2);
            ResultSet rs = s.executeQuery();
            while(rs.next()){
                list.add(rs.getString(1));
                System.out.println(rs.getString(1));
            }
            for(String s1 : list){
                available.getItems().add(s1);
            }
            conn.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }


        //userText.setText("");
        TextFields.bindAutoCompletion(Language,languages);
    }
}


