package unsw.blackout;

import unsw.utils.Angle;

public class Satellite extends Machine {
    protected int speed;

    public Satellite(String satelliteId, double height, Angle position, int range, int speed) {
        super(satelliteId, height, position, range);
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }
}
