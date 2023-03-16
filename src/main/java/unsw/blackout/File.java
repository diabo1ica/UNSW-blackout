package unsw.blackout;

public class File {
    private String filename;
    private String content;
    private int finalStrLen = 0;
    private boolean complete = false;

    public File(String filename, String content, int finalStrLen, boolean state) {
        this.filename = filename;
        this.content = content;
        this.finalStrLen = finalStrLen;
        this.complete = state;
    }

    public String getName() {
        return filename;
    }

    public String getContent() {
        return content;
    }

    public void addContent(String endContent, int bandwidth) {
        int increment = content.length() + bandwidth;
        if (increment + content.length() >= finalStrLen) {
            increment = finalStrLen;
            this.complete = true;
        }
        content = content + endContent.substring(content.length(), increment);
    }

    public int getFinalStrLen() {
        return finalStrLen;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setIsComplete() {
        this.complete = true;
    }

}
