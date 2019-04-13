package edu.wpi.cs3733.d19.teamM.controllers.LogIn;

public class TwoFactor {
    private static TwoFactor newFactor;
    private int theCode;

    synchronized public static TwoFactor getTwoFactor(){
        if(newFactor == null){
            newFactor = new TwoFactor();
        }
        return newFactor;
    }

    public int getTheCode() {
        return theCode;
    }

    public void setTheCode(int theCode) {
        this.theCode = theCode;
    }
}
