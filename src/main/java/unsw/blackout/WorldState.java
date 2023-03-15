package unsw.blackout;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class WorldState {
    List<Device> devices;
    List<Satellite> satellites;
    List<BandwidthState> bwState;

    public WorldState() {
        devices = new ArrayList<Device>();
        satellites = new ArrayList<Satellite>();
        bwState = new ArrayList<BandwidthState>();
    }

    public void addBwState(BandwidthState bws) {
        bwState.add(bws);
    }

    public BandwidthState getBwState(String id) {
        try {
            return bwState.stream().filter(bw -> bw.getObjId().equals(id)).findAny().get();
        }
        catch (Exception e) {
            throw new NoSuchElementException();
        }
    }

    public void resetBwState() {
        bwState = new ArrayList<BandwidthState>();
    }

    public List<Device> getDevices() {
        return devices;
    }

    public List<Satellite> getSatellites() {
        return satellites;
    }
}
