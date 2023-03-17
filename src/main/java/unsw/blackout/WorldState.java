package unsw.blackout;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.List;

public class WorldState {
    List<Device> devices;
    List<Satellite> satellites;
    List<BandwidthState> bwState;

    public WorldState() {
        devices = new ArrayList<Device>();
        satellites = new ArrayList<Satellite>();
        bwState = new ArrayList<BandwidthState>();
    }

    // Move to file transfer
    public void updateFileState(FileTransfer ft) {
        BandwidthState bws;
        try {
            bws = this.getBwState(ft.getId());
        }
        catch (Exception e) {
            return;
        }
        for (File f: ft.getIncompleteFiles()) {
            try {
                FileProgress fp = ft.getFileProgByTypeName(f.getName(), "from");
                String id = fp.getId();
                FileTransfer src = (FileTransfer) findMachById(id);
                BandwidthState bwsFrom = this.getBwState(id);
                ft.updateFile(f, fp, src, bws.getReceiveBandwidth(), bwsFrom.getSendBandwidth());
            }
            catch (Exception e) {
                continue;
            }
        }
    }

    public void setBandwidthState(List<String> srcListReachable, FileTransfer ft) {
        int sendBandwidth = ft.currentBandwidth(srcListReachable,"to");;
        int receiveBandwidth = ft.currentBandwidth(srcListReachable, "from");;

        if ((sendBandwidth == 0 && receiveBandwidth == 0)) {
            return;
        }

        BandwidthState bws = new BandwidthState(ft.getId(), receiveBandwidth, sendBandwidth);
        this.addBwState(bws);
    }

    public void addBwState(BandwidthState bws) {
        bwState.add(bws);
    }

    public BandwidthState getBwState(String id) {
        return bwState.stream().filter(bw -> bw.getObjId().equals(id)).findAny().get();
    }

    public void resetBwState() {
        bwState = new ArrayList<BandwidthState>();
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void addDevice(Device d) {
        devices.add(d);
    }

    public void rmDevice(Device d) {
        devices.remove(d);
    }

    public List<Satellite> getSatellites() {
        return satellites;
    }

    public void addSatellite(Satellite s) {
        satellites.add(s);
    }

    public void rmSatellite(Satellite s) {
        satellites.remove(s);
    }

    public List<Machine> getMachineList() {
        final List<Machine> listOfMachines = new ArrayList<Machine>();
        listOfMachines.addAll(satellites);
        listOfMachines.addAll(devices);
        return listOfMachines;
    }

    public Machine findMachById(String id) {
        return this.getMachineList().stream().filter(x -> x.getId().equals(id)).findAny().get();
    }

    public List<FileTransfer> fileTransferList() {
        List<Machine> fileMachs =  this.getMachineList().stream().filter(m -> m instanceof FileTransfer)
        .collect(Collectors.toList());
        List<FileTransfer> fileTransferList = new ArrayList<FileTransfer>();
        for (Machine fm: fileMachs) {
            FileTransfer ft = (FileTransfer) fm;
            fileTransferList.add(ft);
        }
        return fileTransferList;
    }

    public void teleportIncomingFiles(FileTransfer ft, FileProgress fp) throws FileTransferException {
        FileTransfer src = (FileTransfer) findMachById(fp.getId());
        ft.teleportFileTransfer(src, ft, fp.getFileName());
        src.removeProgress(fp.getFileName(), "to");
    }

    public void teleportSendingFiles(FileTransfer ft, FileProgress fp) throws FileTransferException {
        FileTransfer end = (FileTransfer) findMachById(fp.getId());
        ft.teleportFileTransfer(ft, end, fp.getFileName());
        end.removeProgress(fp.getFileName(), "from");
    }
}
