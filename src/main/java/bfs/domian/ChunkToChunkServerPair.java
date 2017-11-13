package bfs.domian;

import java.io.Serializable;
import java.util.List;

/**
 * Created by callmedj on 17/9/8.
 */
public     class ChunkToChunkServerPair implements Serializable {
    private  String chunkId;
    private List<ChunkServerProperties> chunkServer;

    public String getChunkId() {
        return chunkId;
    }

    public ChunkToChunkServerPair setChunkId(String chunkId) {
        this.chunkId = chunkId;
        return this;
    }

    public List<ChunkServerProperties> getChunkServer() {
        return chunkServer;
    }

    public ChunkToChunkServerPair setChunkServer(List<ChunkServerProperties> chunkServer) {
        this.chunkServer = chunkServer;
        return this;
    }

}
