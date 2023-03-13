package unsw.blackout;

import unsw.utils.Angle;

public class RelaySatellite extends Satellite {
    /*
     * range = 300000; 
     * speed = 1500 clockwise; 
     * maxNumFile = 0; 
     * maxFileCapacity = 0; 
     * sendSpeed = 0;
     * receiveSpeed = 0;
     */

    public RelaySatellite(String satelliteId, double height, Angle position) {
        super(satelliteId, height, position, 300000, -1500);
    }

    @Override
    public void setPos(Angle angle) {
        Angle newAngle;
        if ((position.compareTo(Angle.fromDegrees(345)) == -1 &&
        position.compareTo(Angle.fromDegrees(190)) >= 0) &&
        angle.compareTo(Angle.fromDegrees(0)) != -1) {
            newAngle = position.subtract(angle);
            changeDirection();
        }
        else if (((position.compareTo(Angle.fromDegrees(345)) >= 0 ||
        position.compareTo(Angle.fromDegrees(140)) <= 0)) &&
        angle.compareTo(Angle.fromDegrees(0)) != 1) {
            newAngle = position.subtract(angle);
            changeDirection();
        }
        else {
            newAngle = position.add(angle);
        }
        if (newAngle.compareTo(Angle.fromDegrees(0)) == -1) {
            newAngle = newAngle.add(Angle.fromDegrees(360));
        }
        position = newAngle;
    }

    public void changeDirection() {
        speed = speed * -1;
    }
}
