package unsw.blackout;

import unsw.utils.Angle;

public class HandheldDevice extends Device {
    // Range of Hand held device 50000;
    public HandheldDevice(String deviceId, Angle position) {
        super(deviceId, position, 50000);
    }
}
