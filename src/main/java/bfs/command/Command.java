package bfs.command;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import bfs.service.IMasterService;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大蕉
 * @create 2017/11/7
 * @since 1.0.0
 */
public interface Command {
    byte[] execute(String[] args, IMasterService masterService)
        throws RemoteException, MalformedURLException, NotBoundException;
}