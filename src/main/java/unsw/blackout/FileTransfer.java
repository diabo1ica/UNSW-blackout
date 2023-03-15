package unsw.blackout;

import unsw.blackout.FileTransferException.*;
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

    public default File getFileInFrom(String fileName) throws FileTransferException {
        try {
            return getFile().stream().filter(f -> f.getName().equals(fileName)).findAny().get();
        }
        catch (Exception e) {
            throw new VirtualFileNotFoundException("File doesn't exist from id: " + getId());
        }
    }

    public default void checkBandwidthConstraintTo() throws FileTransferException {
        if (getSendSpeed() < getFileProgressByType("to").size()) {
            throw new VirtualFileNoBandwidthException("Send bandwidth exceeded for id" + getId());
        }
    }

    public default void checkBandwidthConstraintFrom() throws FileTransferException {
        if (getReceiveSpeed() < getFileProgressByType("from").size()) {
            throw new VirtualFileNoBandwidthException("Receive bandwidth exceeded for id" + getId());
        }
    }

    public default void checkFileAlreadyExists(String fileName) throws FileTransferException {
        try {
            getFile().stream().filter(f -> f.getName().equals(fileName)).findAny().get();
        }
        catch (Exception e) {
            return;
        }
        throw new VirtualFileAlreadyExistsException("File already exists in id: " + getId());
    }

    public default List<File> getIncompleteFiles() {
        return new ArrayList<File>();
    }

    public default boolean checkFileConstraint(File inFile) throws FileTransferException {
        return true;
    }
}
