package bfs.server;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import bfs.service.impl.BFSChunkServer;
import bfs.domian.ChunkServerProperties;
import bfs.service.IChunkServerService;
import bfs.service.IMasterService;
import bfs.utils.Printer;

/**
 *  Description:
 * 〈〉
 *
 * @author 大蕉
 * @create 2017/11/5
 * @since 1.0.0
 */
public class ChunkServer {


    /**
     * arg[0] ip
     * arg[1] port
     *
     *
     */
    public static void main(String[] args) {
        try {
            //创建一个远程对象

            String ip = args[0];
            String port = args[1];
            ChunkServerProperties properties = new ChunkServerProperties();
            properties.setIp(ip);
            properties.setPort(port);

            IChunkServerService chunkServer = new BFSChunkServer(100,0,"/",ip);

            LocateRegistry.createRegistry(Integer.parseInt(port));

            String rmi = "rmi://"+properties.getServerIpPort()+"/chunk_server";
            Printer.println(rmi);
            Naming.bind(rmi,chunkServer);

            String masterRMI ="rmi://127.0.0.1:8888/master";

            IMasterService masterService =(IMasterService) Naming.lookup("rmi://127.0.0.1:8888/master");


            masterService.registerChunkServer(properties);

            Printer.println("register to master "+masterRMI + " succcess");

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}