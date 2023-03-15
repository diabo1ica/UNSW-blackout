package unsw.blackout;

import unsw.utils.Angle;

public class StandardSatellite extends FileSatellite {
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
        this.sendSpeed = 1;
        this.receiveSpeed = 1;
        this.maxNumFile = 3;
        this.maxFileCapacity = 80;
    }

    @Override
    public boolean isValidTransferType(Machine m) {
        if (m instanceof DesktopDevice) {
            return false;
        }
        return true;
    }

    @Override
    public int getSendSpeed() {
        return sendSpeed;
    }

    @Override
    public int getReceiveSpeed() {
        return receiveSpeed;
    }
}
