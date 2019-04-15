package edu.wpi.cs3733.d19.teamM.utilities.AStar;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Collections;

public class PathToString {

    /**
     * Convert a path to instruction
     * Path must come in correct order to work
     * @param paths
     * @return
     */
    public static String getDirections(Path paths){
        String completePath = "";
        StringBuilder path = new StringBuilder();
        String step = "";
        int oldX = -1;
        int oldY = -1;
        double angle = 0.0;
        double oldAngle = 0.0;
        Collections.reverse(paths.getPath());
        //angle = calcAngle(oldX, oldY, n.getX(), n.getY());
        //step = getDirectionChange(angle, oldAngle);
        if (paths.getPath().size() < 3) {
            try {
                FileWriter fstream = new FileWriter("resource.txt", false);
                BufferedWriter out = new BufferedWriter(fstream);
                out.write("Figure it out, it's literally next to you. Have a good day");
                out.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return "Figure it out";
        }
        for (Path p : paths.getFloorPaths()) {
            if (p.getPath().size() > 1) {
                path.append("\n<<<Directions for floor " + p.getFloorID() + ">>>\n\n");
                path.append("Start at " + p.getPath().get(0).getLongName() + ", move towards " + p.getPath().get(1).getLongName() + "\n");
                oldAngle = calcAngle(p.getPath().get(0).getX(), p.getPath().get(0).getY(), p.getPath().get(1).getX(), p.getPath().get(1).getY());
                oldX = p.getPath().get(1).getX();
                oldY = p.getPath().get(1).getY();
            }
            for (int i = 1; i < p.getPath().size() - 2; i++) {
                Node n = p.getPath().get(i);
                Node next = p.getPath().get(i + 1);
                angle = calcAngle(oldX, oldY, next.getX(), next.getY());
                step = getDirectionChange(angle, oldAngle);
                path.append("At " + n.getLongName() + ", " + step + "\n");
                oldX = next.getX();
                oldY = next.getY();
                oldAngle = angle;
            }
            path.append("Arrive at " + p.getPath().get(p.getPath().size() - 1).getLongName());
        }

        try {
            FileWriter fstream = new FileWriter("resource.txt", false);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(path.toString());
            out.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return path.toString();
    }

    private static double calcAngle(double oldX, double oldY, double x, double y){
        double changeX = x - oldX;
        double changeY = oldY - y;
        return Math.toDegrees(Math.atan2(changeY, changeX));
    }

    private static String getDirectionChange(double angle, double lastAngle){

        String direction = "";
        String severity = "";

        //normalize last angle first
        if (lastAngle < 0) lastAngle += 360;

        if (angle < 0) angle += 360; // normalize negatives
        double offset = 360 - lastAngle;
        angle = (angle + offset) % 360; // get offset

        if (angle < 180){
            direction = "left"; // normalized for severity
        }
        else{
            direction = "right";
            angle = 360 - angle; // normalize severity
        }

        if (angle <= 20){
            return "remain straight";
        }
        else if (angle <= 40){
            return "bear " + direction;
        }
        else if (angle <= 60){
            return "take a slight " + direction + " turn";
        }
        else if (angle <= 95){
            return "take a " + direction + " turn";
        }
        else {
            return "take a sharp " + direction + " turn";
        }
    }

}
