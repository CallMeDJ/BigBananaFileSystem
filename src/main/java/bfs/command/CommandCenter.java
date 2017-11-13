package bfs.command;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import bfs.command.impl.DeleteCommand;
import bfs.command.impl.GetCommand;
import bfs.command.impl.LsCommand;
import bfs.command.impl.PutCommand;
import bfs.domian.ChunkServerProperties;
import bfs.service.IChunkServerService;
import bfs.service.IMasterService;

import static java.rmi.Naming.lookup;

/**
 *  Description:
 * 〈command center〉
 *
 * @author 大蕉
 * @create 2017/11/7
 * @since 1.0.0
 */
public class CommandCenter {
    private  static IMasterService masterService;

    private static Map<String,IChunkServerService>  chunkServerBundles;

    static{

        try {
            masterService  =(IMasterService) lookup("rmi://127.0.0.1:8888/master");
            chunkServerBundles = new HashMap<String,IChunkServerService>();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private  Map<String,Command> commands;
    public CommandCenter() {

        commands = new HashMap<String, Command>();

        register(LsCommand.COMMAND_NAME,new LsCommand());
        register(DeleteCommand.COMMAND_NAME,new DeleteCommand());
        register(PutCommand.COMMAND_NAME,new PutCommand());
        register(GetCommand.COMMAND_NAME,new GetCommand());
    }

    private void register(String cmd,Command command){
        commands.put(cmd,command);
    }

    public byte[] execute(String commandStr) throws RemoteException, MalformedURLException, NotBoundException {
        String[] commandArray = commandStr.split(" ");

        Command command = commands.get(commandArray[0]);

        if(command == null){
            return "command not found".getBytes();
        }

       return  command.execute(commandArray,masterService);

    }

    public static IChunkServerService getOrCreateConnection(ChunkServerProperties chunkServerProperties)

        throws RemoteException, NotBoundException, MalformedURLException {

        if(!chunkServerBundles.containsKey(chunkServerProperties.getServerIpPort())){
            IChunkServerService chunkServerService =(IChunkServerService) lookup("rmi://"+chunkServerProperties.getServerIpPort()+"/chunk_server");
            chunkServerBundles.put(chunkServerProperties.getServerIpPort(),chunkServerService);
        }

        return chunkServerBundles.get(chunkServerProperties.getServerIpPort());
    }



}