package unsw.blackout;

public class File {
    private String filename;
    private String content;

    public File(String filename, String content) {
        this.filename = filename;
        this.content = content;
    }

    public String getName() {
        return filename;
    }

    public String getContent() {
        return content;
    }
}
