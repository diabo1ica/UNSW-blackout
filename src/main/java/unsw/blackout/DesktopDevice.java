package unsw.blackout;

import unsw.utils.Angle;

public class DesktopDevice extends Device {
    // Range of Standard Satellite 200000;
    public DesktopDevice(String deviceId, Angle position) {
        super(deviceId, position, 200000);
    }
}
