import java.util.ArrayList;
import java.util.List;
import java.util.Random;


class Consumer implements Runnable
{
    private static int MAX_SIZE_TO_TAKE = 5;
    Product product;
    Random rand = new Random();
    public Consumer(Product p)
    {
        product = p;
    }
    @Override
    public void run() {

        try {

            while(true){
                product.consume(rand.nextInt(MAX_SIZE_TO_TAKE-1)+1);
                //Thread.sleep((int) (Math.random() * 100));

            }

        }catch(Exception e)
        {

        }
    }
}

class Producer implements  Runnable
{
    private static int MAX_SIZE_TO_INSERT = 5;
    Product product;
    Random rand = new Random();
    public Producer(Product p)
    {
        product = p;
    }
    @Override
    public void run() {

        try{

        while(true) {
            product.produce(rand.nextInt(MAX_SIZE_TO_INSERT-1)+1);
            //  Thread.sleep((int) (Math.random() * 100));
        }
    }catch(Exception e)
    {

    }
    }
}

public class Main {
    public static final int howManyProducers = 2;
    public static final int howManyConsumers = 100;
    public static final int bufferSize = 10;
    public static void main(String[] args)
    {
        Product product =  new ProductWithHasWaiters(bufferSize);
        List<Thread> list = new ArrayList<Thread>();
        Runnable runnable;
        for(int i=0; i<howManyProducers; i++)
        {
            runnable = new Producer(product);
            Thread t = new Thread(runnable);
            list.add(t);
            t.start();
        }

        for(int i=0; i<howManyConsumers; i++)
        {
            runnable = new Consumer(product);
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
