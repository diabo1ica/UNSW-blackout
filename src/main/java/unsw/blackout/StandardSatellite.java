package unsw.blackout;

import unsw.utils.Angle;

public class StandardSatellite extends Satellite {
    /*
    * range = 150000;
    * receiveSpeed = 1;
    * speed = 2500;
    * maxNumFile = 3;
    * maxFileCapacity = 80;
    * sendSpeed = 1;
    */

    public StandardSatellite(String satelliteId, double height, Angle position) {
        super(satelliteId, height, position, 150000, 2500, 1, 1, 3, 80);
    }
}
