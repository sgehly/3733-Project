package app.controllers;

import javafx.beans.property.StringProperty;
import javafx.beans.property.*;

/**
 * This controller deals with the display tables
 */
public class DisplayTable {

    //Create the instances of the different aspects of the table
    private StringProperty room;
    private StringProperty notes;
    private StringProperty type;

    /**
     * The constructor for the display table that creates the new properties
     */
    public DisplayTable(){
        this.room = new SimpleStringProperty();
        this.notes = new SimpleStringProperty();
        this.type = new SimpleStringProperty();
    }

    /**
     * This is for getting the room property
     * @return StringProperty: The room string
     */
    public StringProperty roomProperty(){
        return room;
    }

    /**
     * This is for getting the notes property
     * @return StringProperty: The notes string
     */
    public StringProperty notesProperty(){
        return notes;
    }

    /**
     * This is for getting the type property
     * @return StringProperty: The type string
     */
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
