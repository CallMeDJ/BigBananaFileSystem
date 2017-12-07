package bfs.domian;

/**
 * Created by callmedj on 17/8/27.
 */
public class BFSChunk {
    private  String chunkId;
    private byte[] file;
    private String md5;

    public String getChunkId() {
        return chunkId;
    }

    public void setChunkId(String chunkId) {
        this.chunkId = chunkId;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
