package unsw.blackout;

import unsw.utils.Angle;

public class TeleportingSatellite extends Satellite{
    /*
     * range = 200000;
     * speed = 1000;
     * maxNumFile = Integer.MAX_VALUE;
     * maxFileCapacity = 200;
     * sendSpeed = 10;
     * receiveSpeed = 15;
     */

    public TeleportingSatellite(String satelliteId, double height, Angle position) {
        super(satelliteId, height, position, 200000, 1000, 10, 15, Integer.MAX_VALUE, 200);
    }
}
