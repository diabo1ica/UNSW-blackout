package unsw.blackout;

import unsw.utils.Angle;
import unsw.utils.MathsHelper;

abstract class Machine implements ConnectionBehaviour{
    private String id;
    private double height;
    protected Angle position;
    private int range;

    public Machine(String deviceId, double height, Angle position, int range) {
        this.id = deviceId;
        this.position = position;
        this.height = height;
    }

    public Angle getPos() {
        return position;
    }

    public boolean visibleInRange(Machine m) {
        if (MathsHelper.isVisible(this.getHeight(), this.getPos(), m.getHeight(), m.getPos()) &&
        (this.isValidTransferType(m)) && isInRange(this, m)) {
            return true;
        }
        return false;
    }

    public String getId() {
        return id;
    }

    public double getHeight() {
        return height;
    }

    public int getRange() {
        return range;
    }
}
