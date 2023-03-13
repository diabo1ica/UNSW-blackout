package unsw.blackout;

import unsw.utils.Angle;

public class StandardSatellite extends FileSatellite {
    private int sendSpeed = 1;
    private int receiveSpeed = 1;
    private int capacity = 0;
    private int maxNumFile = 3;
    private int maxFileCapacity = 80;
    /*
    * range = 150000;
    * receiveSpeed = 1;
    * speed = 2500;
    * maxNumFile = 3;
    * maxFileCapacity = 80;
    * sendSpeed = 1;
    */

    public StandardSatellite(String satelliteId, double height, Angle position) {
        super(satelliteId, height, position, 150000, -2500);
    }
}
