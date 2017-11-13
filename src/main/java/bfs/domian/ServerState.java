package bfs.domian;

import java.io.Serializable;

/**
 * Created by callmedj on 17/9/1.
 */
public class ServerState implements Serializable{
    private boolean isActive;
    private int totalSpace;
    private int usedSpace;
    private int occupiedSpace;

    public int getOccupiedSpace() {
        return occupiedSpace;
    }

    public void setOccupiedSpace(int occupiedSpace) {
        this.occupiedSpace = occupiedSpace;
    }

    public ServerState(boolean isActive, int totalSpace, int usedSpace,int occupiedSpace) {
        this.isActive = isActive;
        this.totalSpace = totalSpace;
        this.usedSpace = usedSpace;
        this.occupiedSpace = occupiedSpace;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(int totalSpace) {
        this.totalSpace = totalSpace;
    }

    public int getUsedSpace() {
        return usedSpace;
    }

    public void setUsedSpace(int usedSpace) {
        this.usedSpace = usedSpace;
    }
}
