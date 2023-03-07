package unsw.blackout;

import java.util.ArrayList;
import java.util.Map;

import java.util.HashMap;
import java.util.List;

import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

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
        List<Machine> machines = new ArrayList<Machine>();
        machines.addAll(devices);
        devices.remove(findMachById(machines, deviceId));
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        // TODO: Task 1c)
        switch (type) {
        case "StandardSatellite":
            satellites.add(new StandardSatellite(satelliteId, height, position));
            break;
        case "TeleportingSatellilte":
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
        List<Machine> machines = new ArrayList<Machine>();
        machines.addAll(satellites);
        satellites.remove(findMachById(machines, satelliteId));
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
        File file = new File(filename, content);
        for (Device device : devices) {
            if (device.getId() == deviceId) {
                device.setFile(file);
            }
        }
    }

    public EntityInfoResponse getInfo(String id) {
        // TODO: Task 1h)
        // Loop thru Device list
        final Map<String, FileInfoResponse> fileMap = new HashMap<>();
        final List<Machine> listOfMachines = new ArrayList<Machine>();
        listOfMachines.addAll(devices);
        listOfMachines.addAll(satellites);
        Machine machine = findMachById(listOfMachines, id);
        List<File> files = machine.getFile();
        for (File file : files) {
            FileInfoResponse fir = new FileInfoResponse(file.getName(), file.getContent(), file.getContent().length(),
                    true);
            fileMap.put(file.getName(), fir);
        }
        return new EntityInfoResponse(id, machine.getPos(), machine.getHeight(), getType(machine), fileMap);
    }

    public void simulate() {
        // TODO: Task 2a)
        
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
        return new ArrayList<>();
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        // TODO: Task 2 c)
    }

    public void createDevice(String deviceId, String type, Angle position, boolean isMoving) {
        createDevice(deviceId, type, position);
        // TODO: Task 3
    }

    public void createSlope(int startAngle, int endAngle, int gradient) {
        // TODO: Task 3
        // If you are not completing Task 3 you can leave this method blank :)
    }

    public Machine findMachById(List<Machine> machines, String id) {
        for (Machine m : machines) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    public String getType(Machine machine) {
        String type = String.valueOf(machine).substring(14);
        return type.split("@")[0];
    }
}
