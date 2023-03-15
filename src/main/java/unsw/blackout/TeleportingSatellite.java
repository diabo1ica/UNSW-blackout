package unsw.blackout;

import unsw.utils.Angle;

public class TeleportingSatellite extends FileSatellite {
    /*
     * range = 200000;
     * speed = 1000 anticlockwise;
     * maxNumFile = Integer.MAX_VALUE;
     * maxFileCapacity = 200;
     * sendSpeed = 10;
     * receiveSpeed = 15;
     */

    public TeleportingSatellite(String satelliteId, double height, Angle position) {
        super(satelliteId, height, position, 200000, 1000);
        this.sendSpeed = 10;
        this.receiveSpeed = 15;
        this.maxNumFile = Integer.MAX_VALUE;
        this.maxFileCapacity = 200;
    }

    // TODO: write compare to 180 degrees method
    @Override
    public void setPos(Angle angle) {
        Angle newAngle = position.add(angle);
        if ((position.compareTo(Angle.fromDegrees(180)) <= 0 &&
        newAngle.compareTo(Angle.fromDegrees(180)) >= 0) ||
        (position.compareTo(Angle.fromDegrees(180)) >= 0 &&
        newAngle.compareTo(Angle.fromDegrees(180)) <= 0)) {
            newAngle = Angle.fromDegrees(0);
            changeDirection();
        }
        if (newAngle.compareTo(Angle.fromDegrees(0)) == -1) {
            newAngle = newAngle.add(Angle.fromDegrees(360));
        }
        if (newAngle.compareTo(Angle.fromDegrees(360)) >= 0) {
            newAngle = newAngle.subtract(Angle.fromDegrees(360));
        }
        position = newAngle;
    }

    public void changeDirection() {
        speed = speed * -1;
    }
}
