package bfs.utils;

import java.util.List;

/**
 * Created by callmedj on 17/4/13.
 */
public class Printer<V> {
    public static void println(Object object){
        System.out.println(object);
    }


    public  void printList(List<V> objectList){
        for(V object : objectList){
            println(object);
        }
    }
}
