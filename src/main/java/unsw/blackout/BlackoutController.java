package unsw.blackout;

import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.List;
import unsw.blackout.FileTransferException.*;
import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public class BlackoutController {
    private WorldState ws = new WorldState();
    private List<Device> devices = ws.getDevices();
    private List<Satellite> satellites = ws.getSatellites();

    public void createDevice(String deviceId, String type, Angle position) {
        // TODO: Task 1a)
        switch (type) {
        case "HandheldDevice":
            devices.add(new HandheldDevice(deviceId, position));
            break;
        case "LaptopDevice":
            devices.add(new LaptopDevice(deviceId, position));
            break;
        case "DesktopDevice":
            devices.add(new DesktopDevice(deviceId, position));
            break;
        default:
            break;
        }
    }

    public void removeDevice(String deviceId) {
        // TODO: Task 1b)
        devices.remove(getDeviceById(deviceId));
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        // TODO: Task 1c)
        switch (type) {
        case "StandardSatellite":
            satellites.add(new StandardSatellite(satelliteId, height, position));
            break;
        case "TeleportingSatellite":
            satellites.add(new TeleportingSatellite(satelliteId, height, position));
            break;
        case "RelaySatellite":
            satellites.add(new RelaySatellite(satelliteId, height, position));
            break;
        default:
            break;
        }
    }

    public void removeSatellite(String satelliteId) {
        // TODO: Task 1d)
        satellites.remove(getSatelliteById(satelliteId));
    }

    public List<String> listDeviceIds() {
        // TODO: Task 1e)
        List<String> idList = new ArrayList<String>();
        for (Device device : devices) {
            idList.add(device.getId());
        }
        return idList;
    }

    public List<String> listSatelliteIds() {
        // TODO: Task 1f)
        List<String> idList = new ArrayList<String>();
        for (Satellite satellite : satellites) {
            idList.add(satellite.getId());
        }
        return idList;
    }

    public void addFileToDevice(String deviceId, String filename, String content) {
        // TODO: Task 1g)
        File file = new File(filename, content, content.length(), true);
        Device device = this.getDeviceById(deviceId);
        device.setFile(file);
    }

    public EntityInfoResponse getInfo(String id) {
        // TODO: Task 1h)
        return getMachInfo(id);
    }

    public EntityInfoResponse getMachInfo(String id) {
        Machine m = findMachById(id);
        Map<String, FileInfoResponse> fileMap = new HashMap<>();
        if (!(m instanceof RelaySatellite)) {
            FileTransfer ft = (FileTransfer) m;
            for (File file : ft.getFile()) {
                int len = file.getFinalStrLen();
                FileInfoResponse fir = new FileInfoResponse(file.getName(), file.getContent(), len,
                file.isComplete());
                fileMap.put(file.getName(), fir);
            }
        }
        return new EntityInfoResponse(id, m.getPos(), m.getHeight(), getType(m), fileMap);
    }

    public void simulate() {
        // TODO: Task 2a)
        for (Satellite s: satellites) {
            updateSatPosition(s);
        }
        updateFileState(machList());
    }

    public void updateSatPosition(Satellite s) {
        double displacement = s.getSpeed() / s.getHeight();
        Angle angle = Angle.fromRadians(displacement);
        s.setPos(angle);
    }

    public void updateFileState(List<Machine> mach) {
        setBandwidthStateAll(mach);
        for (FileTransfer ft: fileTransferList()) {
            // TODO: fix this
            BandwidthState bws;
            try {
                bws = ws.getBwState(ft.getId());
            }
            catch (Exception e) {
                continue;
            }
            for (File f: ft.getIncompleteFiles()) {
                List<FileProgress> fromFp = ft.getFileProgressByType("from");
                FileProgress fp = fromFp.stream().filter(n -> n.getFileName().equals(f.getName())).findAny().get();
                String id = fp.getId();
                BandwidthState bwsFrom = ws.getBwState(id);
                File file = fp.getFile();
                int bandwidth = Math.min(bws.getReceiveBandwidth(), bwsFrom.getSendBandwidth());
                f.addContent(file.getContent(), bandwidth);
                if (f.isComplete()) {
                    ft.removeProgress(f.getName());
                }
            }
        }
    }

    public void setBandwidthStateAll(List<Machine> mach) {
        ws.resetBwState();
        for (FileTransfer ft: fileTransferList()) {
            int nReceive;
            int nSend;
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
            ws.addBwState(bws);
        }
    }

    /**
     * Simulate for the specified number of minutes. You shouldn't need to modify
     * this function.
     */
    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            System.out.print("i");
            System.out.println(i);
            simulate();
        }
    }

    public List<String> communicableEntitiesInRange(String id) {
        // TODO: Task 2 b)
        Machine mach;
        try {
            mach = getDeviceById(id);
        }
        catch (Exception e) {
            mach = getSatelliteById(id);
        }
        boolean isStandardSatellite = false;
        if (mach instanceof StandardSatellite) {
            isStandardSatellite = true;
        }
        List<String> list = entitiesInRange(mach, isStandardSatellite);
        if (list.contains(mach.getId())) {
            list.remove(mach.getId());
        }
        return list;
    }

    // TODO: If desktop 
    public List<String> entitiesInRange(Machine mach, boolean isStandardSatellite) {
        List<String> entityList = new ArrayList<String>();
        for (Device d: devices) {
            if (MathsHelper.isVisible(mach.getHeight(), mach.getPos(), d.getPos()) &&
            !(isStandardSatellite && d instanceof DesktopDevice) &&
            isInRange(mach, d) &&
            !entityList.contains(d.getId())) {
                entityList.add(d.getId());
            }
        }
        for (Satellite s: satellites) {
            if (MathsHelper.isVisible(mach.getHeight(), mach.getPos(), s.getHeight(), s.getPos()) &&
            isInRange(mach, s) &&
            !entityList.contains(s.getId())) {
                if (s instanceof RelaySatellite && mach instanceof StandardSatellite) {
                    entityList.addAll(entitiesInRange(s, true));
                }
                else if (s instanceof RelaySatellite) {
                    entityList.addAll(entitiesInRange(s, isStandardSatellite));
                }
                else {
                    entityList.add(s.getId());
                }
            }
        }
        return entityList;
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        // TODO: Task 2 c)
        File file;
        Machine from = findMachById(fromId);
        Machine to = findMachById(toId);
        if (!(from instanceof FileTransfer) || !(to instanceof FileTransfer) ||
        (!communicableEntitiesInRange(fromId).contains(toId))) {
            return;
        }
        FileTransfer ftFrom = (FileTransfer) from;
        FileTransfer ftTo = (FileTransfer) to;
        
        try {
            file = ftFrom.getFile().stream().filter(f -> f.getName().equals(fileName)).findAny().get();
        }
        catch (Exception e) {
            throw new VirtualFileNotFoundException("File doesn't exist from id: " + fromId);
        }

        if (ftFrom.getSendSpeed() < ftFrom.getFileProgressByType("to").size()) {
            throw new VirtualFileNoBandwidthException("Send bandwidth exceeded for id" + ftFrom.getId());
        }
        if (ftTo.getReceiveSpeed() < ftTo.getFileProgressByType("from").size()) {
            throw new VirtualFileNoBandwidthException("Receive bandwidth exceeded for id" + ftTo.getId());
        }

        try {
            ftTo.getFile().stream().filter(f -> f.getName().equals(fileName)).findAny().get();
            throw new VirtualFileAlreadyExistsException("File already exists in id: " + toId);
        }
        catch (VirtualFileAlreadyExistsException e) {
            throw new VirtualFileAlreadyExistsException("File already exists in id: " + toId);
        }
        catch (Exception e) {
            ftTo.checkFileConstraint(file);
            File newFile = new File(file.getName(), "", file.getContent().length(), false);
            ftTo.setFile(newFile);
        }

        // Init file progress
        ftFrom.addFileProgress(new FileProgress(toId, "to", file));
        ftTo.addFileProgress(new FileProgress(fromId, "from", file));
    }

    public void createDevice(String deviceId, String type, Angle position, boolean isMoving) {
        createDevice(deviceId, type, position);
        // TODO: Task 3
    }

    public void createSlope(int startAngle, int endAngle, int gradient) {
        // TODO: Task 3
        // If you are not completing Task 3 you can leave this method blank :)
    }

    public List<Machine> machList() {
        final List<Machine> listOfMachines = new ArrayList<Machine>();
        listOfMachines.addAll(devices);
        listOfMachines.addAll(satellites);
        return listOfMachines;
    }

    public List<FileTransfer> fileTransferList() {
        List<Machine> fileMachs =  machList().stream().filter(m -> m instanceof FileTransfer)
        .collect(Collectors.toList());
        List<FileTransfer> fileTransferList = new ArrayList<FileTransfer>();
        for (Machine fm: fileMachs) {
            FileTransfer ft = (FileTransfer) fm;
            fileTransferList.add(ft);
        }
        return fileTransferList;
    }

    public Machine findMachById(String id) {
        for (Machine m : this.machList()) {
            if (m.getId().equals(id)) {
                return m;
            }
        }
        return null;
    }

    public String getType(Machine machine) {
        String type = String.valueOf(machine).substring(14);
        return type.split("@")[0];
    }

    public Device getDeviceById(String id) {
        return this.devices.stream().filter(x -> x.getId().equals(id)).findAny().get();
    }

    public Satellite getSatelliteById(String id) {
        return this.satellites.stream().filter(x -> x.getId().equals(id)).findAny().get();
    }

    public boolean isInRange(Machine m1, Machine m2) {
        return MathsHelper.getDistance(m1.getHeight(), m1.getPos(), m1.getHeight(), m2.getPos()) >= 
        Math.min(m1.getRange(), m2.getRange());
    }

    public int div(int a, int b) {
        if (b == 0) {
            b = 1;
        }
        return a/b;
    }
}
