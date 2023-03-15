package unsw.blackout;

import unsw.utils.Angle;

public class Satellite extends Machine {
    protected int speed;

    public Satellite(String satelliteId, double height, Angle position, int range, int speed) {
        super(satelliteId, height, position, range);
        this.speed = speed;
    }

    public void updatePos() {
        double displacement = this.getSpeed() / this.getHeight();
        Angle angle = Angle.fromRadians(displacement);
        Angle newAngle = position.add(angle);
        if (newAngle.compareTo(Angle.fromDegrees(0)) == -1) {
            newAngle = newAngle.add(Angle.fromDegrees(360));
        }
        this.position = newAngle;
    }

    public int getSpeed() {
        return speed;
    }
}
