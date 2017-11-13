package bfs.command.impl;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import bfs.command.Command;
import bfs.command.CommandCenter;
import bfs.domian.ChunkServerProperties;
import bfs.domian.ChunkToChunkServerPair;
import bfs.domian.ClientRequestBundle;
import bfs.domian.ClientResponseBundle;
import bfs.service.IChunkServerService;
import bfs.service.IMasterService;
import bfs.utils.Printer;

/**
 *  Description:
 * 〈get command〉
 *
 * @author 大蕉
 * @create 2017/11/7
 * @since 1.0.0
 */
public class GetCommand implements Command {
    public static String COMMAND_NAME = "get";

    @Override
    public byte[] execute(String[] args,IMasterService masterService)
        throws RemoteException, MalformedURLException, NotBoundException {

        String fileName = args[1];
        ClientRequestBundle requestBundle = new ClientRequestBundle();
        requestBundle.setFileName(fileName);

        ClientResponseBundle bundle = masterService.get(requestBundle);

        List<ChunkToChunkServerPair> pairs = bundle.getChunkToServerMap();

        int chunkSize = pairs.size();
        List<byte[]> fileListWithIndex = new ArrayList<byte[]>();
        for(int i = 0 ; i < chunkSize ; i++){
            ChunkToChunkServerPair current = pairs.get(i);

            boolean isFind = false;

            String chunkId = current.getChunkId();
            List<ChunkServerProperties> servers = current.getChunkServer();
            int serverSize = servers.size();

            for(int j = 0 ; j<servers.size() && !isFind ; j++){
                ChunkServerProperties currentServer = servers.get(j);

                IChunkServerService chunkServerService =  CommandCenter.getOrCreateConnection(currentServer);
                byte[] file = chunkServerService.getChunk(chunkId);

                if(file == null) continue;
                else {
                    isFind = true;
                    fileListWithIndex.add(file);
                }
            }
        }

        for(byte[] file : fileListWithIndex){
            System.out.print(new String(file));
        }


        Printer.println("");
        return new byte[0];
    }

}