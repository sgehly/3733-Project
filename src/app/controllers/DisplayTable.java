package app.controllers;

import javafx.beans.property.StringProperty;
import javafx.beans.property.*;

public class DisplayTable {

    private StringProperty room;
    private StringProperty notes;
    private StringProperty type;

    public DisplayTable(){
        this.room = new SimpleStringProperty();
        this.notes = new SimpleStringProperty();
        this.type = new SimpleStringProperty();
    }

    public StringProperty roomProperty(){
        return room;
    }
    public StringProperty notesProperty(){
        return notes;
    }
    public StringProperty typeProperty(){
        return type;
    }
    //getters
    public String getRoom(){
        return roomProperty().get();
    }
    public String getNotes(){
        return notesProperty().get();
    }
    public String getType(){
        return typeProperty().get();
    }
    //setters
    public void setRoom(String room){
        this.roomProperty().set(room);
    }

    public void setNotes(String notes){
        this.notesProperty().set(notes);
    }

    public void setType(String type){
        this.typeProperty().set(type);
    }
}
