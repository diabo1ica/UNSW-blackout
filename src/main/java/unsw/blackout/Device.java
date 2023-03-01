package unsw.blackout;

import unsw.utils.Angle;

public class Device extends Machine{
    // 69911 jupiter radians
    public Device(String deviceId, String type, Angle position) {
        super(deviceId, type, 69911, position);
    }
}
