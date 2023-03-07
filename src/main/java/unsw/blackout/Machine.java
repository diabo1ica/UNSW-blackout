package unsw.blackout;

import unsw.utils.Angle;
import java.util.ArrayList;
import java.util.List;

public class Machine {
    private List<File> files = new ArrayList<File>();
    private String id;
    private double height;
    private Angle position;
    private int range;

    public Machine(String deviceId, double height, Angle position, int range) {
        this.id = deviceId;
        this.position = position;
        this.height = height;
    }

    public void setFile(File file) {
        files.add(file);
    }

    public List<File> getFile() {
        return files;
    }

    public Angle getPos() {
        return position;
    }

    public void setPos(Angle pos) {
        this.position = pos;
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
