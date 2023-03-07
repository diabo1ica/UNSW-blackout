package unsw.blackout;

import java.util.List;
import unsw.utils.Angle;

public class Satellite extends Machine {
    private int speed;
    private int sendSpeed;
    private int receiveSpeed;
    private int capacity = 0;
    private int maxNumFile;
    private int maxFileCapacity;

    public Satellite(String satelliteId, double height, Angle position, int range, 
            int speed, int sSpeed, int rSpeed, int maxNumFile, int maxFileCapacity) {
        super(satelliteId, height, position, range);
        this.speed = speed;
        this.sendSpeed = sSpeed;
        this.receiveSpeed = rSpeed;
        this.maxNumFile = maxNumFile;
        this.maxFileCapacity = maxFileCapacity;
    }

    public boolean checkFileConstraint(File inFile) {
        final List<File> files = getFile();
        for (File file : files) {
            capacity = file.getContent().length() + capacity;
        }
        capacity = capacity + inFile.getContent().length();
        return (files.size() < maxNumFile) && (capacity < maxFileCapacity);
    }

    public int getSpeed() {
        return speed;
    }

    public int getSendSpeed() {
        return sendSpeed;
    }

    public int getReceiveSpeed() {
        return receiveSpeed;
    }
}
