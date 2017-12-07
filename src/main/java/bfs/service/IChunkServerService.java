package bfs.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import bfs.domian.ServerState;

/**
 *  Description:
 * 〈chunkServer 方法〉
 *
 * @author 大蕉
 * @create 2017/11/5
 * @since 1.0.0
 */
public interface IChunkServerService extends Remote {
     boolean isActive() throws RemoteException;
     void storeChunk(byte[] file , String chunkId) throws RemoteException;
     byte[] getChunk(String chunkId) throws RemoteException;

     ServerState getServerState() throws RemoteException;

     void deletChunk(String chunkId) throws RemoteException;

      int getOccupiedSpace() throws RemoteException;

      void setOccupiedSpace(int occupiedSpace) throws RemoteException ;

     String getChunkMD5(String chunkId) throws RemoteException ;

    }