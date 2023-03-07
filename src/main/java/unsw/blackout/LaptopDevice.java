package unsw.blackout;

import unsw.utils.Angle;

public class LaptopDevice extends Device {
    // Range of Laptop Device 100000;
    public LaptopDevice(String deviceId, Angle position) {
        super(deviceId, position, 100000);
    }
}
