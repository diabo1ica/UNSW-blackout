package unsw.blackout;
import unsw.utils.Angle;
import java.util.ArrayList;
import java.util.List;

public class Machine {
    private List<File> files = new ArrayList<File>();
    private String id;
    private String sType;
    private Angle position;

    public Machine(String deviceId, String type, Angle position) {
        this.id = deviceId;
        this.sType = type;
        this.position = position;
    }

    public void setFile(File file) {
        files.add(file);
    }

    public List<File> getFile() {
        return files;
    }

    public String getType() {
        return sType;
    }

    public Angle getPos() {
        return position;
    }

    public String getId() {
        return id;
    }
}
