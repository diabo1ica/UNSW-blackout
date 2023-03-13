package unsw.blackout;

public class File {
    private String filename;
    private String content;
    private int progress = 0;
    private boolean complete = false;

    public File(String filename, String content, boolean state) {
        this.filename = filename;
        this.content = content;
        this.complete = state;
    }

    public String getName() {
        return filename;
    }

    public String getContent() {
        return content;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setIsComplete() {
        this.complete = true;
    }

    public boolean checkProgress(String content) {
        return (this.content.length() >= content.length());
    }

    public void updateProgress(int prog) {
        progress =+ prog;
    }
}
