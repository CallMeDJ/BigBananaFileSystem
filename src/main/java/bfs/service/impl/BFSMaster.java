package bfs.service.impl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import bfs.domian.*;
import bfs.service.IChunkServerService;
import bfs.service.IMasterService;
import bfs.utils.Printer;

/**
 * Created by callmedj on 17/8/27.
 */
public class BFSMaster extends UnicastRemoteObject implements IMasterService{


    /*
   *
   *   复制份数,单位为份,默认主+副本个数为3
   */
    public static int copySize = 3;

    /*
     *
     *   块大小,单位为byte
     */
    public static int blockSize = 5;
    public List<ChunkLog> logList = new ArrayList<ChunkLog>();
    public List<ChunkServerProperties> servers = new ArrayList<ChunkServerProperties>();

    public Map<String,List<String>> fileChunkMap = new HashMap<String,List<String>>();

    public Map<String,List<ChunkServerProperties>> chunkServerMap = new HashMap<String,List<ChunkServerProperties>>();

    public Map<String,String> chunkMD5Map = new HashMap<String,String>();

    public List<String> namespace = new ArrayList<String>();


    private Map<String,IChunkServerService> chunkServerBundles = new HashMap<String,IChunkServerService> ();

    private BFSMaster self;

    public BFSMaster() throws RemoteException {
        super();
        self = this;
    }



    @Override
    public ClientResponseBundle ls(ClientRequestBundle request) throws RemoteException {
        self.log("ls "+request.getFileName());


        String filePath = request.getFileName();

        int filePathLength = filePath.length();
        Set<String> fileSet = new HashSet<String>();
        for(String file : namespace){
            int fileIndex = file.indexOf(filePath);
            if(fileIndex == 0 && !file.endsWith(".delete")){


                String menuList = filePathLength == 1 ? file.substring(filePathLength) : file.substring(filePathLength+1);

                int menuIndex = menuList.indexOf('/');

                if(menuIndex != -1){
                    menuList = menuList.substring(0,menuIndex);
                }
                fileSet.add(menuList);
            }
        }

        StringBuilder result = new StringBuilder();

        for(String file : fileSet){
            result.append(file+"\r\n");
        }

        ClientResponseBundle responseBundle = new ClientResponseBundle();

        responseBundle.setValues(result.toString().getBytes());
        return responseBundle;
    }

    private void log(String handle){
        ChunkLog chunkLog = new ChunkLog(handle);
        self.logList.add(chunkLog);
        Printer.println(chunkLog.date +","+chunkLog.handle);
    }

    @Override
    public ClientResponseBundle get(ClientRequestBundle request){
        self.log("get "+request.getFileName());
        ClientResponseBundle response = new ClientResponseBundle();
        String file = request.getFileName();

        List<String> chunks = self.fileChunkMap.get(file);

        if(chunks == null){
            return response;
        }

        if(chunks.isEmpty()){
            return  response;
        }

        List<ChunkToChunkServerPair> pairs = new ArrayList<ChunkToChunkServerPair>();
        for(String chunk : chunks){
            pairs.add(new ChunkToChunkServerPair().setChunkId(chunk).setChunkServer(self.chunkServerMap.get(chunk)));
        }

        response.setChunkToServerMap(pairs);
        return response;
    }

    @Override
    public ClientResponseBundle md5(ClientRequestBundle request) throws RemoteException {

        this.chunkMD5Map.put(request.getChunkId(),request.getMd5());

        ClientResponseBundle responseBundle = new ClientResponseBundle();

        return null;
    }



    @Override
    public ClientResponseBundle put(ClientRequestBundle request) throws RemoteException{
        self.log("put "+request.getFileName());

        String file = request.getFileName();
        int fileSplit = request.getFileSplit();


        ClientResponseBundle response = new ClientResponseBundle();

        List<ChunkToChunkServerPair> pairs = new ArrayList<ChunkToChunkServerPair>();


        self.namespace.add(file);
        List<String> chunkList = new ArrayList<String>();
        for(int i = 0 ; i < fileSplit; i++) {
            String chunkId = UUID.randomUUID().toString();
            chunkList.add(chunkId);
        }

        self.fileChunkMap.put(file,chunkList);

        //Printer.println("adding map from file to chunkId");
        for(String chunkId : chunkList){
            List<ChunkServerProperties> chunkServers = getActiveServer();
            pairs.add(new ChunkToChunkServerPair().setChunkId(chunkId).setChunkServer(chunkServers));
            self.chunkServerMap.put(chunkId,chunkServers);
        }

        response.setChunkToServerMap(pairs);

        for(ChunkToChunkServerPair pair : pairs){
            List<ChunkServerProperties> chunkServers = pair.getChunkServer();
            for(ChunkServerProperties current : chunkServers){
                self.chunkServerBundles.get(current.getServerIpPort()).setOccupiedSpace(0);
            }
        }

        // Printer.println("adding map from chunkId to servers");
        return response;
    }

    private double factor = 0.9;
    private List<ChunkServerProperties>  getActiveServer() throws RemoteException {
        List<ChunkServerProperties> chunkServers = new ArrayList<ChunkServerProperties>();
        Collections.sort(self.servers, new Comparator<ChunkServerProperties>() {
            @Override
            public int compare(ChunkServerProperties o1, ChunkServerProperties o2) {
                try {
                ServerState o1ServerState = self.chunkServerBundles.get(o1.getServerIpPort()).getServerState();
                ServerState   o2ServerState = self.chunkServerBundles.get(o2.getServerIpPort()).getServerState();
                double o1Percentage = Double.valueOf(o1ServerState.getUsedSpace()+o1ServerState.getOccupiedSpace())/Double.valueOf(o1ServerState.getTotalSpace());
                double o2Percentage = Double.valueOf(o2ServerState.getUsedSpace()+o2ServerState.getOccupiedSpace())/Double.valueOf(o2ServerState.getTotalSpace());
                return o1Percentage==o2Percentage?0:(o1Percentage<o2Percentage?-1:1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
               return 0;
            }
        });

        int serverCount = 0;
        for(ChunkServerProperties chunkServer : self.servers){
            if(serverCount >= copySize) {
                break;
            }

            IChunkServerService currentChunkServer =  self.chunkServerBundles.get(chunkServer.getServerIpPort());
            ServerState   serverState = currentChunkServer.getServerState();

            int percentage = serverState.getUsedSpace()/serverState.getTotalSpace();

            if (percentage >= factor) {
                continue;
            }

            currentChunkServer.setOccupiedSpace(serverState.getOccupiedSpace()+blockSize);
            chunkServers.add(chunkServer);
            serverCount++;
        }
        if (serverCount!=copySize){
            throw new RemoteException("server not enough");
        }


        return chunkServers;

    }


    @Override
    public void checkSum() throws RemoteException {
       Runnable check = new Runnable() {
           @Override
           public void run() {
            Printer.println("checking md5");
               for (String file : self.namespace) {
                   List<String> chunkIds = self.fileChunkMap.get(file);

                   Set<String> rightFile = new HashSet<String>();
                   Set<String> wrong = new HashSet<String>();

                    for(String chunkId : chunkIds){
                       List<ChunkServerProperties> serverProperties = self.chunkServerMap.get(chunkId);

                        IChunkServerService rightServer = null;
                        Set<ChunkServerProperties> worngServer = new HashSet<ChunkServerProperties>();

                        for(ChunkServerProperties propertie : serverProperties){
                          IChunkServerService chunkServer = self.chunkServerBundles.get(propertie.getServerIpPort());

                        try {
                                String md5 = chunkServer.getChunkMD5(chunkId);
                                if(md5.equals(self.chunkMD5Map.get(chunkId))){
                                    rightServer = chunkServer;
                                }
                                else {
                                    worngServer.add(propertie);
                                }

                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }

                        if(rightServer == null){
                            self.log("chunkId" + chunkId + "in file" + file + "destoryed");
                        }
                        else {
                            for (ChunkServerProperties worngProperty : worngServer) {
                                IChunkServerService currentWrongServer = self.chunkServerBundles.get(propertie.getServerIpPort());
                                try {
                                    currentWrongServer.storeChunk(rightServer.getChunk(chunkId),chunkId);

                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }


                            }
                        }

                    }


               }
           }
       };

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(check,0,3,TimeUnit.SECONDS);
    }





    @Override
    public void gc()  throws RemoteException{
        Runnable gc = new Runnable() {
            @Override
            public void run() {
                try {
                    Set<String> fileToGC = new HashSet<String>();

                    for (String file : self.namespace) {
                        if (file.endsWith(".delete")) {
                            fileToGC.add(file);
                        }
                    }

                    Set<String> chunkIdsToGC = new HashSet<String>();

                    for (String file : fileToGC) {
                        List<String> chunkIds = self.fileChunkMap.get(file);
                        for (String currentChunk : chunkIds) {
                            chunkIdsToGC.add(currentChunk);
                            List<ChunkServerProperties> chunkServers = self.chunkServerMap.get(currentChunk);
                            for (ChunkServerProperties currentServer : chunkServers) {
                                self.chunkServerBundles.get(currentServer.getServerIpPort()).deletChunk(currentChunk);
                            }
                        }
                    }

                    for(String file : fileToGC){
                        self.namespace.remove(file);
                        self.fileChunkMap.remove(file);
                    }
                    for(String chunk : chunkIdsToGC){
                        self.chunkServerMap.remove(chunk);
                    }


                    if(!fileToGC.isEmpty()) {
                        Printer.println("gc complete delete files -> " + fileToGC.toString());
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(gc,0,3,TimeUnit.SECONDS);
    }

    @Override
    public ClientResponseBundle delete(ClientRequestBundle request){

        self.log("delete "+request.getFileName());
        ClientResponseBundle response = new ClientResponseBundle();
        String file = request.getFileName();

        namespace.remove(file);
        namespace.add(file+".delete");

        List<String> chunks = self.fileChunkMap.get(file);

        self.fileChunkMap.remove(file);

        self.fileChunkMap.put(file + ".delete",chunks);

        return response;
    }

    @Override
    public void registerChunkServer(ChunkServerProperties chunkServer)
        throws RemoteException, NotBoundException, MalformedURLException {
        self.servers.add(chunkServer);

        IChunkServerService chunkServerService =(IChunkServerService) Naming.lookup("rmi://"+chunkServer.getServerIpPort()+"/chunk_server");

        self.chunkServerBundles.put(chunkServer.getServerIpPort(),chunkServerService);
        self.log("chunk server "+chunkServer.getServerIpPort()+" register success");
    }


}
