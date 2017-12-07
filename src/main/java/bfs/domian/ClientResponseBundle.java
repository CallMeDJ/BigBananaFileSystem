package bfs.domian;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bfs.domian.ChunkToChunkServerPair;

/**
 * Created by callmedj on 17/8/27.
 */
public class ClientResponseBundle implements Serializable {

    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private List<ChunkToChunkServerPair> chunkToServerMap = new ArrayList<ChunkToChunkServerPair>();

    private byte[] values;

    public List<ChunkToChunkServerPair> getChunkToServerMap() {
        return chunkToServerMap;
    }

    public void setChunkToServerMap(List<ChunkToChunkServerPair> chunkToServerMap) {
        this.chunkToServerMap = chunkToServerMap;
    }

    public byte[] getValues() {
        return values;
    }

    public void setValues(byte[] values) {
        this.values = values;
    }
}
