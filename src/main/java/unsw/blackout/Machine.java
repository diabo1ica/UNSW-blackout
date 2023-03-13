package unsw.blackout;

import unsw.utils.Angle;

abstract class Machine {
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

    public void setPos(Angle angle) {
        Angle newAngle = position.add(angle);
        if (newAngle.compareTo(Angle.fromDegrees(0)) == -1) {
            newAngle = newAngle.add(Angle.fromDegrees(360));
        }
        this.position = newAngle;
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
