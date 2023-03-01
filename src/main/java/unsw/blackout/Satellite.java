package unsw.blackout;

import unsw.utils.Angle;

public class Satellite extends Machine {
    private double height;
    public Satellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, type, position);
        this.height = height;
    }

    public double getHeight() {
        return height;
    }
}
