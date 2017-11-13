package bfs.server;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
 * Created by callmedj on 17/8/27.
 */
public class BFSClient {

    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {


        Scanner scanner = new Scanner(System.in);
        CommandCenter commandCenter = new CommandCenter();

        Printer.println("input\n" +
                "ls to list the file eg: ls /bfs \r\n" +
                "get to get the file eg: get /bfs/some.log \r\n" +
                "put to put a file eg: put 'some' /bfs/some.log \r\n"+
                "delete to delete a file delete /bfs/some.log");
        Printer.println("if you want to exit the command , input :exit");
        while (scanner.hasNext()){
            String command = scanner.nextLine();
           byte[] result =  commandCenter.execute(command);
            Printer.println(new String(result));
        }

    }
}
