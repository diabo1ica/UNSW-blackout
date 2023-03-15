package unsw.blackout;

public class FileProgress {
    private String id;
    private String type;
    private File file;
    private String fileName;

    public FileProgress(String id, String type, File file) {
        this.id = id;
        this.type = type;
        this.file = file;
        this.fileName = file.getName();
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public File getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return type;
    }

}
