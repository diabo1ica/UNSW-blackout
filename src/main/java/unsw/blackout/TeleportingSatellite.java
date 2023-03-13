package unsw.blackout;

import unsw.utils.Angle;

public class TeleportingSatellite extends FileSatellite{
    private int sendSpeed = 10;
    private int receiveSpeed = 15;
    private int capacity = 0;
    private int maxNumFile = Integer.MAX_VALUE;
    private int maxFileCapacity = 200;
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
    }

    // TODO: write compare to 180 degrees method
    @Override
    public void setPos(Angle angle) {
        Angle newAngle = position.add(angle);
        if ((angle.compareTo(Angle.fromDegrees(180)) <= 0 &&
        newAngle.compareTo(Angle.fromDegrees(180)) >= 0) ||
        (angle.compareTo(Angle.fromDegrees(180)) >= 0 &&
        newAngle.compareTo(Angle.fromDegrees(180)) <= 0)) {
            newAngle = Angle.fromDegrees(0);
            changeDirection();
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
