package edu.wpi.cs3733.d19.teamM.utilities.AStar;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        int distance = 0;
        double angle = 0.0;
        double oldAngle = 0.0;
        Collections.reverse(paths.getPath());
        //angle = calcAngle(oldX, oldY, n.getX(), n.getY());
        //step = getDirectionChange(angle, oldAngle);
        if (paths.getPath().size() < 3) {
            try {
                FileWriter fstream = new FileWriter("resource.txt", false);
                BufferedWriter out = new BufferedWriter(fstream);
                out.write("It should be next to you. Have a good day");
                out.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return "It should be next to you. Have a good day";
        }
        for (Path p : paths.getFloorPaths()) {
            if (p.getPath().size() > 1) {
                path.append("\n<<<Directions for floor " + p.getFloorID() + ">>>\n\n");
                path.append("Start at " + p.getPath().get(0).getLongName() + ", move towards " + p.getPath().get(1).getLongName() + "\n");
                oldAngle = calcAngle(p.getPath().get(0).getX(), p.getPath().get(0).getY(), p.getPath().get(1).getX(), p.getPath().get(1).getY(), distance);
                oldX = p.getPath().get(1).getX();
                oldY = p.getPath().get(1).getY();
            }
            for (int i = 1; i < p.getPath().size() - 2; i++) {
                Node n = p.getPath().get(i);
                Node next = p.getPath().get(i + 1);
                angle = calcAngle(oldX, oldY, next.getX(), next.getY(), distance);
                step = getDirectionChange(angle, oldAngle);
                distance = (int)(getDistance(oldX, oldY, next.getX(), next.getY()) * 0.34);
                path.append("In " + distance + " feet, at " + n.getLongName() + ", " + step + "\n");
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

    /***
     * Get instuctions for the robot
     * @param paths - The path to get instructions for
     *
     * @return A 2D array of instructions
     *         - The first element of each array is the angle to take
     *              > [0][0] is the angle to take if robot is facing east
     *         - The second element of each array is the distance to travel for
     */
    public static List<List<Double>> pathToInstructions(Path paths){
        List<List<Double>> instructions = new ArrayList<>();
        double angle, oldAngle=0, distance = 0;
        int oldX =0, oldY = 0;
        if (paths.getPath().size() < 3) return null;

        for (Path p : paths.getFloorPaths()) {
            if (p.getPath().size() > 1) {
                oldAngle = calcAngle(p.getPath().get(0).getX(), p.getPath().get(0).getY(), p.getPath().get(1).getX(), p.getPath().get(1).getY(), distance);
                distance = (getDistance(p.getPath().get(0).getX(), p.getPath().get(0).getY(), p.getPath().get(1).getX(), p.getPath().get(1).getY()) * 0.34);
                oldX = p.getPath().get(1).getX();
                oldY = p.getPath().get(1).getY();
                instructions.add(Arrays.asList(oldAngle, distance));
            }
            for (int i = 1; i < p.getPath().size() - 2; i++) {
                Node n = p.getPath().get(i);
                Node next = p.getPath().get(i + 1);
                angle = calcAngle(oldX, oldY, next.getX(), next.getY(), distance);
                distance = (getDistance(oldX, oldY, next.getX(), next.getY()) * 0.34);

                instructions.add(Arrays.asList((angle - oldAngle), distance));
                oldX = next.getX();
                oldY = next.getY();
                oldAngle = angle;
            }
        }
        return instructions;
    }

    private static double getDistance(int x, int y, int x1, int y1){
        double deltaX = x - x1;
        double deltaY = y - y1;

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    private static double calcAngle(double oldX, double oldY, double x, double y, double distance){
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
