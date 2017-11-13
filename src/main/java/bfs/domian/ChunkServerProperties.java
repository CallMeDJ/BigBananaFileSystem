package bfs.domian;

import java.io.Serializable;

/**
 *  Description:
 * 〈chunk服务器关键信息〉
 *
 * @author 大蕉
 * @create 2017/11/5
 * @since 1.0.0
 */
public class ChunkServerProperties implements Serializable{
    public String ip;
    public String port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getServerIpPort(){
        return this.getIp()+":"+this.getPort();
    }
}