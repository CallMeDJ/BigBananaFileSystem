package bfs.domian;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by callmedj on 17/8/27.
 */
public class ChunkLog {

    public static SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd hhmmss");
    public String date;
    public String handle;

    public ChunkLog(String handle){
        this.date = sdf.format(new Date());
        this.handle = handle;
    }
}
