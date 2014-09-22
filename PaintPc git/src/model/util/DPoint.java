package model.util;

import java.awt.Point;

public class DPoint {

    public double x;
    public double y;

    public DPoint(double _x, double _y){
        x = _x;
        y = _y;
    }
    public DPoint(int _x, int _y){
        x = _x;
        y = _y;
    }
    public DPoint(DPoint _dp){
        x = _dp.x;
        y = _dp.y;
    }
    public DPoint(Point _dp){
        x = _dp.x;
        y = _dp.y;
    }
    public DPoint(){
        x = 0;
        y = 0;
    }
    /**
     * @return the x
     */
    public double getX() {
        return x;
    }
    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }
    /**
     * @return the y
     */
    public double getY() {
        return y;
    }
    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }
}
