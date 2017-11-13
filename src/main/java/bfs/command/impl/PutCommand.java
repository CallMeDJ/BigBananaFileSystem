package bfs.command.impl;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import bfs.command.Command;
import bfs.command.CommandCenter;
import bfs.domian.ChunkServerProperties;
import bfs.domian.ChunkToChunkServerPair;
import bfs.domian.ClientRequestBundle;
import bfs.domian.ClientResponseBundle;
import bfs.service.IChunkServerService;
import bfs.service.IMasterService;
import bfs.service.impl.BFSMaster;
import bfs.utils.Printer;

/**
 *  Description:
 * 〈put command〉
 *
 * @author 大蕉
 * @create 2017/11/7
 * @since 1.0.0
 */
public class PutCommand implements Command {
    public static String COMMAND_NAME = "put";

    @Override
    public byte[] execute(String[] args,IMasterService masterService) throws RemoteException {


        int blockSize = BFSMaster.blockSize;

        String file = args[1];
        String fileName = args[2];



        byte[] fileBytes = file.getBytes();

        ClientRequestBundle requestBundle = new ClientRequestBundle();
        requestBundle.setFileName(fileName);
        requestBundle.setFileSize(fileBytes.length);

        int fileSpliSize = fileBytes.length / blockSize + 1;

        requestBundle.setFileSplit(fileSpliSize);

        ClientResponseBundle responseBundle= null;
        try {
            responseBundle = masterService.put(requestBundle);
        } catch (Exception e) {
            e.printStackTrace();
            Printer.println("disk is full");
            return null;
        }


        List<ChunkToChunkServerPair> pairs = responseBundle.getChunkToServerMap();

        for(int i = 0 ; i < fileSpliSize ; i++){
            byte[] current = Arrays.copyOfRange(fileBytes,i*blockSize,(i+1)*blockSize);

            ChunkToChunkServerPair currentPair = pairs.get(i);
            List<ChunkServerProperties> servers = currentPair.getChunkServer();
            String chunkId = currentPair.getChunkId();
            for(ChunkServerProperties server : servers){
                try {
                    IChunkServerService chunkServerService =  CommandCenter.getOrCreateConnection(server);
                    chunkServerService.storeChunk(current,chunkId);
                } catch (Exception e) {
                    e.printStackTrace();
                    ClientRequestBundle request = new ClientRequestBundle();
                    requestBundle.setFileName(fileName);
                    masterService.delete(request);
                    break;
                }
            }

        }

        Printer.println("store file"+fileName +" success");
        return new byte[0];
    }


}