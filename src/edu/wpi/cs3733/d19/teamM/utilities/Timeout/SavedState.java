package edu.wpi.cs3733.d19.teamM.utilities.Timeout;

public class SavedState {
    private String state;
    private String userName;
    public SavedState() {
        state = "home";
        userName = "";
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public String getUserName(){
        return this.userName;
    }
}
