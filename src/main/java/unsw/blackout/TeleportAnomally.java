package unsw.blackout;

interface TeleportAnomally {
    public default void teleportOccurence(WorldState ws, FileTransfer ft, FileProgress fp) {
        try {
            if (fp.getType().equals("from")) {
                ws.teleportIncomingFiles(ft, fp);
            }
            else if (fp.getType().equals("to")) {
                ws.teleportSendingFiles(ft, fp);
            }
        }
        catch (Exception e) {
            System.out.println("File does not exist");
        }
    }
}
