package bfs.service;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import bfs.domian.ChunkServerProperties;
import bfs.domian.ClientRequestBundle;
import bfs.domian.ClientResponseBundle;

/**
 *  Description:
 * 〈客户端服务调用〉
 *
 * @author 大蕉
 * @create 2017/11/5
 * @since 1.0.0
 */
public interface IMasterService extends Remote{

     ClientResponseBundle delete(ClientRequestBundle request) throws RemoteException;

     ClientResponseBundle put(ClientRequestBundle request) throws RemoteException ;

    ClientResponseBundle ls(ClientRequestBundle request) throws RemoteException ;

     ClientResponseBundle get(ClientRequestBundle request) throws RemoteException ;

    ClientResponseBundle md5(ClientRequestBundle request) throws RemoteException ;

    void checkSum() throws RemoteException ;


    void registerChunkServer(ChunkServerProperties chunkServer)
         throws RemoteException, NotBoundException, MalformedURLException;

    void gc()  throws RemoteException ;
}