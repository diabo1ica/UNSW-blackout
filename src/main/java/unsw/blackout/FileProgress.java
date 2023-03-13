package unsw.blackout;

public class FileProgress {
    private String id;
    private String type;
    private String fileName;

    public FileProgress(String id, String type, String fileName) {
        this.id = id;
        this.type = type;
        this.fileName = fileName;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getFileName() {
        return fileName;
    }
}
