package unsw.blackout;

import java.util.List;
import java.util.stream.Collectors;

interface TransferProgress {
    public void addFileProgress(FileProgress fileProgress);

    public List<FileProgress> getListProgress();

    public FileProgress getFileProgressByFilename(String fileName);

    public default List<FileProgress> getFileProgressByType(String type) {
        return getListProgress().stream().filter(fp -> fp.getFileType().equals(type)).collect(Collectors.toList());
    }

    public default FileProgress getFileProgByTypeName(String name, String type) {
        List<FileProgress> fromFp = this.getFileProgressByType(type);
        return fromFp.stream().filter(n -> n.getFileName().equals(name)).findAny().get();
    }

    public void removeProgress(String fileName);
}
