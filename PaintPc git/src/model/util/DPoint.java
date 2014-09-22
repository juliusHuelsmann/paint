package model.util;

import java.awt.Point;

public class DPoint {

    public double x, y;

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
}
