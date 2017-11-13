package bfs.command.impl;

import java.rmi.RemoteException;

import bfs.command.Command;
import bfs.domian.ClientRequestBundle;
import bfs.service.IMasterService;
import bfs.utils.Printer;

/**
 * Description:
 * 〈list command〉
 *
 * @author 大蕉
 * @create 2017/11/7
 * @since 1.0.0
 */
public class LsCommand implements Command {
    public static String COMMAND_NAME = "ls";

    @Override
    public byte[] execute(String[] args, IMasterService masterService) throws RemoteException {

        ClientRequestBundle requestBundle = new ClientRequestBundle();


        String fileName = args.length < 2 ? "/" : args[1];
        requestBundle.setFileName(fileName);
        return masterService.ls(requestBundle).getValues();
    }
}
