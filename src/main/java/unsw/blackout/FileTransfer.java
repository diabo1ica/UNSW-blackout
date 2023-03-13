package unsw.blackout;

import java.util.ArrayList;
import java.util.List;

interface FileTransfer {
    public List<File> getFile();

    public void setFile(File file);

    public default List<File> getIncompleteFiles() {
        return new ArrayList<File>();
    }

    public default boolean checkFileConstraint(File inFile) throws FileTransferException {
        return true;
    }
}
