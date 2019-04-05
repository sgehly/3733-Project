package edu.wpi.cs3733.d19.teamM.utilities.AStar;

public class PathToString {

    /**
     * Convert a path to instruction
     * Path must come in correct order to work
     * @param p
     * @return
     */
    public static String getDirections(Path p){
        String completePath = "";
        String step = "";
        int oldX = -1;
        int oldY = -1;
        double angle = 0.0;
        double oldAngle = 0.0;
        Node cur = null;
        Node start = p.getPath().get(0);
        for (Node n : p.getPath()){
            if (oldX == -1 && oldY == -1)completePath = "Start at " + n.getLongName();
            else if (cur.getId().equals(start.getId())){
                completePath = completePath + ", go towards " + n.getLongName() + "\n";
                angle = calcAngle(oldX, oldY, n.getX(), n.getY());
            }
            else {
                angle = calcAngle(oldX, oldY, n.getX(), n.getY());
                step = getDirectionChange(angle, oldAngle);
                completePath = completePath + "At " + cur.getLongName() + ", " + step + "\n";
            }
            oldX = n.getX();
            oldY = n.getY();
            oldAngle = angle;
            cur = n;
        }

        return completePath + "Arrive at " + cur.getLongName();
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
