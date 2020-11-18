import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


class Consumer implements Runnable
{

    Monitor monitor;
    Buffer buffer;

    public Consumer(Monitor m, Buffer buffer)
    {
        this.buffer = buffer;
        monitor = m;
    }
    @Override
    public void run() {

        try {

            while(true){
                int i = monitor.startConsuming();
                Thread.sleep((int) (Math.random() * 100));
                int value = buffer.consume(i);
                monitor.finishConsuming(i);

            }

        }catch(Exception e)
        {

        }
    }
}

class Producer implements  Runnable
{

    private Monitor monitor;
    Buffer buffer;

    public Producer(Monitor m, Buffer buffer)
    {
        this.buffer = buffer;
        monitor = m;
    }
    @Override
    public void run() {

        try{

        while(true) {
            int i = monitor.startProducing();
            Thread.sleep((int) (Math.random() * 100));
            buffer.produce(i, 1);
            monitor.finishProducing(i);
        }
    }catch(Exception e)
    {

    }
    }
}

public class Main {
    public static final int howManyProducers = 100;
    public static final int howManyConsumers = 100;
    public static final int bufferSize = 100;
    public static Buffer buffer = new Buffer(bufferSize);
    public static void main(String[] args)
    {
        Monitor monitor = new Monitor(bufferSize);
        List<Thread> list = new ArrayList<Thread>();
        Runnable runnable;
        for(int i=0; i<howManyProducers; i++)
        {
            runnable = new Producer(monitor, buffer);
            Thread t = new Thread(runnable);
            list.add(t);
            t.start();
        }

        for(int i=0; i<howManyConsumers; i++)
        {
            runnable = new Consumer(monitor, buffer);
            Thread t = new Thread(runnable);
            list.add(t);
            t.start();
        }


        for(Thread t: list)
        {
            try{
                t.join();
            }
            catch (Exception e)
            {

            }
        }


    }

}
