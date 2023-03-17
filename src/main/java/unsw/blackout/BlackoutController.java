package unsw.blackout;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

public class BlackoutController {
    private WorldState ws = new WorldState();

    public void createDevice(String deviceId, String type, Angle position) {
        // TODO: Task 1a)
        switch (type) {
        case "HandheldDevice":
            ws.addDevice(new HandheldDevice(deviceId, position));
            break;
        case "LaptopDevice":
            ws.addDevice(new LaptopDevice(deviceId, position));
            break;
        case "DesktopDevice":
            ws.addDevice(new DesktopDevice(deviceId, position));
            break;
        default:
            break;
        }
    }

    public void removeDevice(String deviceId) {
        // TODO: Task 1b)
        ws.rmDevice(getDeviceById(deviceId));    
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        // TODO: Task 1c)
        switch (type) {
        case "StandardSatellite":
            ws.addSatellite(new StandardSatellite(satelliteId, height, position));
            break;
        case "TeleportingSatellite":
            ws.addSatellite(new TeleportingSatellite(satelliteId, height, position));
            break;
        case "RelaySatellite":
            ws.addSatellite(new RelaySatellite(satelliteId, height, position));
            break;
        default:
            break;
        }
    }

    public void removeSatellite(String satelliteId) {
        // TODO: Task 1d)
        ws.rmSatellite(getSatelliteById(satelliteId));
    }

    public List<String> listDeviceIds() {
        // TODO: Task 1e)
        List<String> idList = new ArrayList<String>();
        for (Device device : ws.getDevices()) {
            idList.add(device.getId());
        }
        return idList;
    }

    public List<String> listSatelliteIds() {
        // TODO: Task 1f)
        List<String> idList = new ArrayList<String>();
        for (Satellite satellite : ws.getSatellites()) {
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
        Machine m = ws.findMachById(id);
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
        for (Satellite s: ws.getSatellites()) {
            s.updatePos(ws);
        }
        ws.resetBwState();
        for (FileTransfer ft: ws.fileTransferList()) {
            ws.setBandwidthState(communicableEntitiesInRange(ft.getId()), ft);
        }
        for (FileTransfer ft: ws.fileTransferList()) {
            ws.updateFileState(ft);
        }
    }

    /**
     * Simulate for the specified number of minutes. You shouldn't need to modify
     * this function.
     */
    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }
    
    // TODO: Satellite and device
    public List<String> communicableEntitiesInRange(String id) {
        // TODO: Task 2 b)
        Machine mach = ws.findMachById(id);
        List<String> list = entitiesInRange(mach, mach);
        if (list.contains(mach.getId())) {
            list.remove(mach.getId());
        }
        return list;
    }

    // TODO: If desktop 
    public List<String> entitiesInRange(Machine src, Machine mach) {
        List<String> entityList = new ArrayList<String>();
        for (Machine m: ws.getMachineList()) {
            if (mach.visibleInRange(m) && src.isValidTransferType(m) && !entityList.contains(m.getId())) {
                entityList.add(m.getId());
                if (m instanceof RelaySatellite) {
                    entityList.addAll(entitiesInRange(src, m));
                }
            }
        }
        return entityList;
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        // TODO: Task 2 c)
        Machine from = ws.findMachById(fromId);
        Machine to = ws.findMachById(toId);
        if (!(from instanceof FileTransfer) || !(to instanceof FileTransfer) ||
        (!communicableEntitiesInRange(fromId).contains(toId))) {
            return;
        }
        FileTransfer ftFrom = (FileTransfer) from;
        FileTransfer ftTo = (FileTransfer) to;
        
        File file = ftFrom.getFileFromSrc(fileName);
        ftFrom.checkBandwidthConstraintFrom();
        ftTo.checkBandwidthConstraintTo();
        ftTo.checkFileAlreadyExists(fileName);
        ftTo.checkFileConstraint(file);
        
        ftTo.setFile(new File(file.getName(), "", file.getContent().length(), false));

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

    public Satellite getSatelliteById(String id) {
        return ws.getSatellites().stream().filter(x -> x.getId().equals(id)).findAny().get();
    }

    public Device getDeviceById(String id) {
        return ws.getDevices().stream().filter(x -> x.getId().equals(id)).findAny().get();
    }

    public String getType(Machine machine) {
        String type = String.valueOf(machine).substring(14);
        return type.split("@")[0];
    }
}
