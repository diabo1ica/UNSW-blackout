package unsw.blackout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import unsw.utils.Angle;
import unsw.blackout.FileTransferException.*;

abstract class FileSatellite extends Satellite implements FileTransfer{
    private List<File> files = new ArrayList<File>();
    private List<FileProgress> fListProgress = new ArrayList<FileProgress>();
    private int sendSpeed;
    private int receiveSpeed;
    private int capacity;
    private int maxNumFile;
    private int maxFileCapacity;

    public FileSatellite(String satelliteId, double height, Angle position, int range, int speed) {
        super(satelliteId, height, position, range, speed);
    }

    public void setFile(File file) {
        files.add(file);
    }

    public List<File> getFile() {
        return files;
    }

    public List<FileProgress> getListProgress() {
        return fListProgress;
    }

    public void addFileProgress(FileProgress fileProgress) {
        fListProgress.add(fileProgress);
    }

    public void removeProgress(String fileName) {
        fListProgress.removeIf(fp -> fp.getFileName().equals(fileName));
    }

    @Override
    public List<File> getIncompleteFiles() {
        List<File> incFiles;
        try {
            incFiles = files.stream().filter(f -> f.isComplete() == false).collect(Collectors.toList());
            return incFiles;
        }
        catch (Exception e) {
            return new ArrayList<File>();
        }
    }

    public int getSendSpeed() {
        return sendSpeed;
    }

    public int getReceiveSpeed() {
        return receiveSpeed;
    }

    @Override
    public boolean checkFileConstraint(File newFile) throws FileTransferException{
        for (File file : files) {
            capacity = file.getContent().length() + capacity;
        }
        capacity = capacity + newFile.getContent().length();
        if (files.size() < maxNumFile) {
            throw new VirtualFileNoStorageSpaceException("Max Files Reached");
        }
        if (capacity > maxFileCapacity) {
            throw new VirtualFileNoStorageSpaceException("Max Storage Reached");
        }
        return true;
    }
}
