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
