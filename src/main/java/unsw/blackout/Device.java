package unsw.blackout;

import unsw.utils.Angle;

public class Device extends Machine {
    // 69911 jupiter radians
    public Device(String deviceId, Angle position, int range) {
        super(deviceId, 69911, position, range);
    }
}
