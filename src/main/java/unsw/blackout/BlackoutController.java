package unsw.blackout;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.util.List;
import unsw.blackout.FileTransferException.*;
import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public class BlackoutController {
    private List<Device> devices = new ArrayList<Device>();
    private List<Satellite> satellites = new ArrayList<Satellite>();

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
        File file = new File(filename, content, true);
        Device device = this.getDeviceById(deviceId);
        device.setFile(file);
    }

    public EntityInfoResponse getInfo(String id) {
        // TODO: Task 1h)
        // Loop thru Device list
        EntityInfoResponse eir;
        try {
            eir = getSatelliteInfo(id);
        }
        catch (Exception e) {
            eir = getDeviceInfo(id);
        }
        return eir;
    }

    public EntityInfoResponse getDeviceInfo(String id) {
        Device device = getDeviceById(id);
        if (device.equals(null)) {
            return null;
        }
        Map<String, FileInfoResponse> fileMap = new HashMap<>();
        for (File file : device.getFile()) {
            FileInfoResponse fir = new FileInfoResponse(file.getName(), file.getContent(), file.getContent().length(),
                    true);
            fileMap.put(file.getName(), fir);
        }
        return new EntityInfoResponse(id, device.getPos(), device.getHeight(), getType(device), fileMap);
    }

    public EntityInfoResponse getSatelliteInfo(String id) {
        Satellite s = getSatelliteById(id);
        Map<String, FileInfoResponse> fileMap = new HashMap<>();
        if (!(s instanceof RelaySatellite)) {
            FileSatellite fs = (FileSatellite) s;
            for (File file : fs.getFile()) {
                FileInfoResponse fir = new FileInfoResponse(file.getName(), file.getContent(), file.getContent().length(),
                        true);
                fileMap.put(file.getName(), fir);
            }
            return new EntityInfoResponse(id, fs.getPos(), fs.getHeight(), getType(fs), fileMap);
        }
        else {
            return new EntityInfoResponse(id, s.getPos(), s.getHeight(), getType(s), fileMap);
        }
    }

    public void simulate() {
        // TODO: Task 2a)
        for (Satellite s: satellites) {
            double displacement = s.getSpeed() / s.getHeight();
            Angle angle = Angle.fromRadians(displacement);
            s.setPos(angle);
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

    public List<String> communicableEntitiesInRange(String id) {
        // TODO: Task 2 b)
        Machine mach;
        try {
            mach = getDeviceById(id);
        }
        catch (Exception e) {
            mach = getSatelliteById(id);
        }
        List<String> list = entitiesInRange(mach);
        if (list.contains(mach.getId())) {
            list.remove(mach.getId());
        }
        return list;
    }

    public List<String> entitiesInRange(Machine mach) {
        List<String> entityList = new ArrayList<String>();
        for (Device d: devices) {
            if (MathsHelper.isVisible(mach.getHeight(), mach.getPos(), d.getPos()) &&
            !(mach instanceof StandardSatellite && d instanceof DesktopDevice) &&
            isInRange(mach, d) &&
            !entityList.contains(d.getId())) {
                entityList.add(d.getId());
            }
        }
        for (Satellite s: satellites) {
            if (MathsHelper.isVisible(mach.getHeight(), mach.getPos(), s.getHeight(), s.getPos()) &&
            isInRange(mach, s) &&
            !entityList.contains(s.getId())) {
                if (s instanceof RelaySatellite) {
                    entityList.addAll(entitiesInRange(s));
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
        if (from instanceof FileTransfer) {
            FileTransfer i = (FileTransfer) from;
            try {
                file = i.getFile().stream().filter(f -> f.getName().equals(fileName)).findAny().get();
            }
            catch (Exception e) {
                throw new VirtualFileNotFoundException("File doesn't exist from id: " + fromId);
            }
        }
        else {
            return;
        }
        if (to instanceof FileTransfer) {
            // typecast
            FileTransfer i = (FileTransfer) to;
            Optional<File> op = Optional.of(i.getFile().stream()
            .filter(f -> f.getName().equals(fileName)).findAny().get());
            if (op.isPresent()) {
                throw new VirtualFileAlreadyExistsException("File already exists in id: " + toId);
            }
            i.checkFileConstraint(i.getFile().stream().filter(f -> f.getName().equals(fileName)).findAny().get());
            File newFile = new File(file.getName(), "", false);
            i.setFile(newFile);
        }
        // Update file progress
        if (from instanceof FileSatellite) {
            FileSatellite fs = (FileSatellite) from;
            fs.addFileProgress(new FileProgress(toId, "to", fileName));
        }
        else {
            Device d = (Device) from;
            d.addFileProgress(new FileProgress(toId, "to", fileName));
        }
        if (to instanceof FileSatellite) {
            FileSatellite fs = (FileSatellite) from;
            fs.addFileProgress(new FileProgress(fromId, "from", fileName));
        }
        else {
            Device d = (Device) from;
            d.addFileProgress(new FileProgress(fromId, "from", fileName));
        }
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
}
