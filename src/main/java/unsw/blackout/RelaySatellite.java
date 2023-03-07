package unsw.blackout;

import unsw.utils.Angle;

public class RelaySatellite extends Satellite {
    /*
     * range = 300000; 
     * speed = 1500; 
     * maxNumFile = 0; 
     * maxFileCapacity = 0; 
     * sendSpeed = 0;
     * receiveSpeed = 0;
     */
    
    public RelaySatellite(String satelliteId, double height, Angle position) {
        super(satelliteId, height, position, 300000, 1500, 0, 0, 0, 0);
    }
}
