package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import javafx.beans.property.StringProperty;
import javafx.beans.property.*;

/**
 * This controller deals with the display tables
 */
public class DisplayTable {

    //Create the instances of the different aspects of the table
    private StringProperty type;
    private StringProperty room;
    private StringProperty subType;
    private StringProperty description;
    private StringProperty checkbox;
    private IntegerProperty requestId;
    private StringProperty date;
    private StringProperty filledBy;


    /**
     * The constructor for the display table that creates the new properties
     */
    public DisplayTable(){
        this.type = new SimpleStringProperty();
        this.room = new SimpleStringProperty();
        this.subType = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.checkbox = new SimpleStringProperty();
        this.date = new SimpleStringProperty();
        this.filledBy = new SimpleStringProperty();
        this.requestId = new SimpleIntegerProperty();
    }

    public StringProperty typeProperty(){
        return type;
    }
    public StringProperty roomProperty(){
        return room;
    }
    public StringProperty subTypeProperty(){
        return subType;
    }
    public StringProperty descriptionProperty(){
        return description;
    }
    public StringProperty checkboxProperty(){
        return checkbox;
    }
    public StringProperty dateProperty(){
        return date;
    }
    public StringProperty filledByProperty(){return filledBy;}
    public IntegerProperty requestIdProperty(){return requestId;}

    //getters
    public String getType(){
        return typeProperty().get();
    }
    public String getRoom(){
        return roomProperty().get();
    }
    public String getSubType(){
        return subTypeProperty().get();
    }
    public String getDescription(){
        return descriptionProperty().get();
    }
    public String getCheckbox(){return checkboxProperty().get();}
    public String getDate(){return dateProperty().get();}
    public String getFilledBy(){return filledByProperty().get();}
    public Integer getRequestId(){return requestIdProperty().get();}

    //setters
    public void setType(String type){
        this.typeProperty().set(type);
    }
    public void setRoom(String room){
        this.roomProperty().set(room);
    }
    public void setSubType(String subType){
        this.subTypeProperty().set(subType);
    }
    public void setDescription(String description){
        this.descriptionProperty().set(description);
    }
    public void setCheckbox(String checkbox){
        this.checkboxProperty().set(checkbox);
    }
    public void setDate(String date){
        this.dateProperty().set(date);
    }
    public void setFilledBy(String fulfilledBy){this.filledByProperty().set(fulfilledBy);}
    public void setRequestId(Integer requestId){this.requestIdProperty().set(requestId);}

}
