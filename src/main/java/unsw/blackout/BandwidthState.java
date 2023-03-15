package unsw.blackout;

public class BandwidthState {
    private String id;
    private int receivingBandwidth;
    private int sendingBandwidth;

    public BandwidthState(String id, int rb, int sb) {
        this.id = id;
        this.receivingBandwidth = rb;
        this.sendingBandwidth = sb;
    }

    public String getObjId() {
        return id;
    }

    public int getReceiveBandwidth() {
        return receivingBandwidth;
    }

    public int getSendBandwidth() {
        return sendingBandwidth;
    }
}
