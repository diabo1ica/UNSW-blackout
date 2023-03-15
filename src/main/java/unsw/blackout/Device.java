package unsw.blackout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import unsw.utils.Angle;

abstract class Device extends Machine implements FileTransfer{
    private List<File> files = new ArrayList<File>();
    private List<FileProgress> fListProgress = new ArrayList<FileProgress>();
    // 69911 jupiter radians
    public Device(String deviceId, Angle position, int range) {
        super(deviceId, 69911, position, range);
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

    public FileProgress getFileProgressByFilename(String fileName) {
        return fListProgress.stream().filter(fp -> fp.getFileName().equals(fileName)).findAny().get();
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
}
