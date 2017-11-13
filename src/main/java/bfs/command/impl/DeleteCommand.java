package bfs.command.impl;

import java.rmi.RemoteException;

import bfs.command.Command;
import bfs.domian.ClientRequestBundle;
import bfs.service.IMasterService;

/**
 *  Description:
 * 〈delete command〉
 *
 * @author 大蕉
 * @create 2017/11/7
 * @since 1.0.0
 */
public class DeleteCommand implements Command {
    public static String COMMAND_NAME = "delete";

    @Override
    public byte[] execute(String[] args,IMasterService masterService) throws RemoteException {

        ClientRequestBundle requestBundle = new ClientRequestBundle();
        requestBundle.setFileName(args[1]);
        masterService.delete(requestBundle);
        return new byte[0];
    }


}