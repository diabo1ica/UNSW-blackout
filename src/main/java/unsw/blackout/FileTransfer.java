package unsw.blackout;

import unsw.utils.Helper;
import unsw.blackout.FileTransferException.*;
import java.util.ArrayList;
import java.util.List;

interface FileTransfer extends TransferProgress {
    public String getId();

    public List<File> getFile();

    public void setFile(File file);
    
    public void removeFile(File file);

    public default int getSendSpeed() {
        return Integer.MAX_VALUE;
    }

    public default int getReceiveSpeed() {
        return Integer.MAX_VALUE;
    }

    public default File getFileFromSrc(String fileName) throws FileTransferException {
        try {
            return getFile().stream().filter(f -> f.getName().equals(fileName)).findAny().get();
        }
        catch (Exception e) {
            throw new VirtualFileNotFoundException("File doesn't exist from id: " + getId());
        }
    }

    public default void checkBandwidthConstraintTo() throws FileTransferException {
        if (getSendSpeed() < getFileProgressByType("to").size()) {
            throw new VirtualFileNoBandwidthException("Send bandwidth exceeded for id: " + getId());
        }
    }

    public default void checkBandwidthConstraintFrom() throws FileTransferException {
        if (getReceiveSpeed() < getFileProgressByType("from").size()) {
            throw new VirtualFileNoBandwidthException("Receive bandwidth exceeded for id: " + getId());
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
    
    public default int currentBandwidth(List<String> srcListReachable, String type) {
        int nFile = 0;
        int bandSpeed = 0;
        try {
            List<FileProgress> progList = this.getFileProgressByType(type);
            for (FileProgress fp: progList) {
                if (!srcListReachable.contains(fp.getId())) {
                    progList.remove(fp);
                    File file = fp.getFile();
                    this.removeFile(file);
                    this.removeProgress(file.getName(), type);
                }
            }
            nFile = progList.size();
            bandSpeed = this.getReceiveSpeed();
        }
        catch (Exception e) {
            return 0;
        }
        return Helper.div(bandSpeed, nFile);
    }

    public default void updateFile(File f, FileProgress fp, FileTransfer src, int rBandwidth, int sBandwidth) {
        File file = fp.getFile();
        int bandwidth = Math.min(rBandwidth, sBandwidth);
        f.addContent(file.getContent(), bandwidth);
        src.getListProgress().forEach((fe) -> {System.out.println(fe.getFileName() + fe.getType() + fe.getId());});
        if (f.isComplete()) {
            this.removeProgress(file.getName(), "from");
            src.removeProgress(file.getName(), "to");
        }
    }

    public default void teleportFileTransfer(FileTransfer src, FileTransfer end,
    String fileName) throws FileTransferException {
        File srcFile = src.getFileFromSrc(fileName);
        File endFile = end.getFileFromSrc(fileName);
        String teleportedSrcContent = srcFile.getContent().replace("t", "");
        File teleportedFile = new File(fileName, teleportedSrcContent, teleportedSrcContent.length(), true);
        end.removeFile(endFile);
        if (src instanceof Device) {
            src.removeFile(srcFile);
            src.setFile(teleportedFile);
            return;
        }
        end.setFile(teleportedFile);
    }
}
