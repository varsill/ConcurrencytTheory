import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Buffer {
    HashMap<Integer, Integer> buffer = new HashMap<Integer, Integer>();
    public Buffer(int bufferSize)
    {
        for(int i=0; i<bufferSize; i++)
        {
            buffer.put(i, 0);
        }
    }

    
    public void produce(int index, int item)
    {
        buffer.replace(index, item);
    }

    public int consume(int index)
    {
        int i = buffer.get(index);
        buffer.replace(index, 0);
        return i;
    }

}
