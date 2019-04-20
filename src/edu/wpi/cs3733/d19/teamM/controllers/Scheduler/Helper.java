package edu.wpi.cs3733.d19.teamM.controllers.Scheduler;

import java.util.TimerTask;

//the scheduler instance that is currently running


public class Helper extends TimerTask {
    //the scheduler instance that is currently running
    public Scheduler baseScheduler;

    public Helper(Scheduler scheduler) {
        baseScheduler = scheduler;
    }

    public void run(){
        baseScheduler.randomizeRandomRoomColors();
    }
}
