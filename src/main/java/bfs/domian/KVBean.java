package bfs.domian;

/**
 * Created by callmedj on 17/12/7.
 */
public class KVBean<U,V> {
    private  U key;
    private  V value;

    public KVBean(U key, V value) {
        this.key = key;
        this.value = value;
    }

    public U getKey() {
        return key;
    }

    public void setKey(U key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
