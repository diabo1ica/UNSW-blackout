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

    public void updateFileState() {
        setBandwidthStateAll();
        for (FileTransfer ft: fileTransferList()) {
            // TODO: fix this
            BandwidthState bws;
            try {
                bws = this.getBwState(ft.getId());
            }
            catch (Exception e) {
                continue;
            }
            for (File f: ft.getIncompleteFiles()) {
                List<FileProgress> fromFp = ft.getFileProgressByType("from");
                FileProgress fp = fromFp.stream().filter(n -> n.getFileName().equals(f.getName())).findAny().get();
                String id = fp.getId();
                BandwidthState bwsFrom = this.getBwState(id);
                File file = fp.getFile();
                int bandwidth = Math.min(bws.getReceiveBandwidth(), bwsFrom.getSendBandwidth());
                f.addContent(file.getContent(), bandwidth);
                if (f.isComplete()) {
                    ft.removeProgress(f.getName());
                }
            }
        }
    }

    public void setBandwidthStateAll() {
        this.resetBwState();
        for (FileTransfer ft: fileTransferList()) {
            int nReceive, nSend;
            int sendSpeed = 0;
            int receiveSpeed = 0;

            try {
                nReceive = ft.getFileProgressByType("from").size();
                receiveSpeed = ft.getReceiveSpeed();
            }
            catch (Exception e) {
                nReceive = 0;
            }

            try {
                nSend = ft.getFileProgressByType("to").size();
                sendSpeed = ft.getSendSpeed();
            }
            catch (Exception e) {
                nSend = 0;
            }

            if ((nReceive == 0 && nSend == 0) || ft instanceof RelaySatellite) {
                continue;
            }

            BandwidthState bws = new BandwidthState(ft.getId(), div(receiveSpeed, nReceive), div(sendSpeed, nSend));
            this.addBwState(bws);
        }
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

    public int div(int a, int b) {
        if (b == 0) {
            b = 1;
        }
        return a/b;
    }
}
