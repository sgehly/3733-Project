package edu.wpi.cs3733.d19.teamM.utilities;

/**
 * This controller helps deal with map points on the screen
 */
public class MapPoint {

    //The instances of the x,y values
    public double x;
    public double y;

    /**
     * The constructor for the map point which sets both the x and y values for it
     * @param x: The double x value
     * @param y: The double y value
     */
    public MapPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
}