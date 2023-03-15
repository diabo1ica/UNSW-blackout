package unsw.blackout;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

interface FileTransfer {
    public String getId();

    public List<File> getFile();

    public void setFile(File file);

    public default int getSendSpeed() {
        return Integer.MAX_VALUE;
    }

    public default int getReceiveSpeed() {
        return Integer.MAX_VALUE;
    }

    public void addFileProgress(FileProgress fileProgress);

    public List<FileProgress> getListProgress();

    public FileProgress getFileProgressByFilename(String fileName);

    public default List<FileProgress> getFileProgressByType(String type) {
        return getListProgress().stream().filter(fp -> fp.getFileType().equals(type)).collect(Collectors.toList());
    }

    public void removeProgress(String fileName);

    public default List<File> getIncompleteFiles() {
        return new ArrayList<File>();
    }

    public default boolean checkFileConstraint(File inFile) throws FileTransferException {
        return true;
    }
}
