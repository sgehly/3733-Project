package edu.wpi.cs3733.d19.teamM.utilities.AStar;

import java.io.FileWriter;
import java.io.PrintWriter;

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
        Node cur = null;
        Node start = paths.getPath().get(0);
        //angle = calcAngle(oldX, oldY, n.getX(), n.getY());
        //step = getDirectionChange(angle, oldAngle);
        if (paths.getPath().size() < 3) return "Figure it out";
        for (Path p : paths.getFloorPaths()) {
            path.append("\n<<<Directions for floor " + p.getFloorID() + ">>>\n\n");
            path.append("Start at " + p.getPath().get(0).getLongName() + ", move towards " + p.getPath().get(1).getLongName() + "\n");
            for (Node n : p.getPath().subList(1, p.getPath().size() - 1)) {
                angle = calcAngle(oldX, oldY, n.getX(), n.getY());
                step = getDirectionChange(angle, oldAngle);
                path.append("At " + n.getLongName() + ", " + step + "\n");
                oldX = n.getX();
                oldY = n.getY();
                oldAngle = angle;
                cur = n;
            }
        }
        path.append("Arrive at " + paths.getPath().get(paths.getPath().size() - 1).getLongName());
        try {
            PrintWriter writer = new PrintWriter("C:\\Users\\Jack\\Desktop\\the-file-name.txt", "UTF-8");
            writer.write(path.toString());
            writer.close();
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

        if (angle <= 10){
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
