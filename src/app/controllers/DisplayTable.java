package app.controllers;

import javafx.beans.property.StringProperty;
import javafx.beans.property.*;

public class DisplayTable {

    private StringProperty room;
    private StringProperty capacity;
    private StringProperty notes;
    private StringProperty roomType;
    private StringProperty type;
    private IntegerProperty requestid;
    private StringProperty date;
    private StringProperty filledBy;


    public DisplayTable(){
        this.room = new SimpleStringProperty();
        this.capacity = new SimpleStringProperty();
        this.notes = new SimpleStringProperty();
        this.roomType = new SimpleStringProperty();
        this.type = new SimpleStringProperty();
        this.requestid = new SimpleIntegerProperty();
        //this.date = new SimpleStringProperty();
        this.filledBy = new SimpleStringProperty();
    }


    public StringProperty roomProperty(){
        return room;
    }
    public StringProperty capacityProperty(){
        return capacity;
    }
    public StringProperty roomTypeProperty(){
        return roomType;
    }
    public StringProperty typeProperty(){
        return type;
    }
    public StringProperty notesProperty(){
        return notes;
    }
    
    public IntegerProperty idProperty(){return requestid;}
    public StringProperty dateProperty(){return date;}
    public StringProperty filledByProperty(){return filledBy;}

    //getters
    public String getRoom(){
        return roomProperty().get();
    }
    public String getCapacity(){
        return capacityProperty().get();
    }
    public String getNotes(){
        return notesProperty().get();
    }
    public String setRoomType(){
        return roomTypeProperty().get();
    }

    public String setType(){
        return roomTypeProperty().get();
    }

    public Integer  getID(){
        return idProperty().get();
    }
    public String getDate(){return dateProperty().get();}
    public String getFilledBy(){return filledByProperty().get();}

    //setters
    public void setRoom(String room){
        this.roomProperty().set(room);
    }
    public void setCapacity(String capacity){
        this.capacityProperty().set(capacity);
    }
    public void setRoomType(String type){
        this.roomTypeProperty().set(type);
    }
    public void setType(String type){
        this.typeProperty().set(type);
    }

    public void setNotes(String notes){
        this.notesProperty().set(notes);
    }

    public void setId(Integer id ){this.idProperty().set(id);}

    public void setDate(String Date){this.dateProperty();}

    public void setFilledBy(String filledBy){this.filledByProperty().set(filledBy);}

}
