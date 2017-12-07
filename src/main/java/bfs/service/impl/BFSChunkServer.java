package bfs.service.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import bfs.domian.ServerState;
import bfs.domian.BFSChunk;
import bfs.service.IChunkServerService;
import bfs.utils.MD5Util;
import bfs.utils.Printer;

/**
 * Created by callmedj on 17/8/27.
 */
public class BFSChunkServer extends UnicastRemoteObject implements IChunkServerService{
    public int totalSpace;
    public int usedSpace;
    public String fileSpace;
    public String ip;
    public int occupiedSpace;


    @Override
    public int getOccupiedSpace() throws RemoteException {
        return occupiedSpace;
    }

    @Override
    public void setOccupiedSpace(int occupiedSpace) throws RemoteException {
        this.occupiedSpace = occupiedSpace;
    }

    @Override
    public String getChunkMD5(String chunkId) {
        return MD5Util.encode(this.chunks.get(chunkId).getFile());
    }


    @Override
    public String toString(){
    return "server "+ip+" ',totalSpace is "+totalSpace+" ,usedSpace is"+usedSpace;
    }

    public Map<String,BFSChunk> chunks = new HashMap<String,BFSChunk>();


    public BFSChunkServer(int totalSpace,int usedSpace,String fileSpace,String ip) throws RemoteException {
        super();
        this.totalSpace = totalSpace;
        this.usedSpace = usedSpace;
        this.fileSpace = fileSpace;
        this.ip = ip;
    }


    @Override
    public boolean isActive() throws RemoteException{
        return (this.usedSpace+this.getOccupiedSpace()/this.totalSpace) <= 0.9D;
    }


    @Override
    public void storeChunk(byte[] file , String chunkId) throws RemoteException {

        BFSChunk chunk = new BFSChunk();
        chunk.setChunkId(chunkId);
        chunk.setFile(file);

        int chunkByte = chunk.getFile().length;
        if(this.usedSpace + chunkByte > totalSpace){
            throw new RemoteException("disk is full");
        }
        this.usedSpace += chunkByte;

        this.chunks.put(chunkId,chunk);
        Printer.println(" store file "+chunkId+" in " +this.ip + " success! and totalSpace is "+ totalSpace +" ,usedSpace is "+usedSpace+" byte");
    }

    @Override
    public byte[] getChunk(String chunkId){
        BFSChunk chunk =  chunks.get(chunkId);
        if(chunk == null) {
            return null;
        }
        return chunk.getFile();
    }

    @Override
    public ServerState getServerState() throws RemoteException {
        return new ServerState(this.isActive(),this.totalSpace,this.usedSpace,this.occupiedSpace);
    }






    @Override
    public void deletChunk(String chunkId){
        if(this.chunks.containsKey(chunkId)) {
            BFSChunk chunk = chunks.get(chunkId);
            int chunkByte = chunk.getFile().length;
            this.usedSpace -= chunkByte;
            this.chunks.remove(chunkId);
            Printer.println(this.ip + " delete file "+chunkId+" success! and totalSpace is "+ totalSpace +" ,usedSpace is "+usedSpace+" byte");

        }
    }

}
