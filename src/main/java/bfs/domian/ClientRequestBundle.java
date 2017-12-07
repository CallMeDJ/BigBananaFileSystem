package bfs.domian;

import java.io.Serializable;
import java.util.List;

/**
 * Created by callmedj on 17/8/27.
 */
public class ClientRequestBundle implements Serializable {

    private  String fileName;
    private int fileSize;
    private int fileSplit;
    private String chunkId;
    private String md5;

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getChunkId() {
        return chunkId;
    }

    public void setChunkId(String chunkId) {
        this.chunkId = chunkId;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public int getFileSplit() {
        return fileSplit;
    }

    public void setFileSplit(int fileSplit) {
        this.fileSplit = fileSplit;
    }
}
