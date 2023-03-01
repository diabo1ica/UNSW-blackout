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
        Device device = new Device(deviceId, type, position);
        devices.add(device);
    }

    public void removeDevice(String deviceId) {
        // TODO: Task 1b)
        for (Device device : devices) {
            if (device.getId() == deviceId) {
                devices.remove(device);
                return;
            }
        }
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        // TODO: Task 1c)
        Satellite satellite = new Satellite(satelliteId, type, height, position);
        satellites.add(satellite);
    }

    public void removeSatellite(String satelliteId) {
        // TODO: Task 1d)
        for (Satellite satellite : satellites) {
            if (satellite.getId() == satelliteId) {
                satellites.remove(satellite);
                return;
            }
        }
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
        Machine machine = new Machine("id", "id", 0, null);
        // Loop thru Device list
        final Map<String, FileInfoResponse> fileMap = new HashMap<>();
        for (Device device : devices) {
            if (device.getId() == id) {
                machine = device; 
            }
        }
        // Loop thru Satellite list
        for (Satellite satellite : satellites) {
            if (satellite.getId() == id) {
                machine = satellite;
            }
        }
        List<File> files = machine.getFile();
        for (File file: files) {
            fileMap.put(file.getName(), new FileInfoResponse(file.getName(),
            file.getContent(), file.getContent().length(), true));
        }
        return new EntityInfoResponse(id, machine.getPos(), machine.getHeight(), machine.getType(), fileMap);
    }

    public void simulate() {
        // TODO: Task 2a)
    }

    /**
     * Simulate for the specified number of minutes.
     * You shouldn't need to modify this function.
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
}
