import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;



class Consumer implements Runnable
{
    int MAX_TO_CONSUME;
    Product product;
    Random rand = new Random();
    public Consumer(Product p, int max_to_consume)
    {
        MAX_TO_CONSUME = max_to_consume;
        product = p;
    }
    @Override
    public void run() {

        try {
            while(true){
                product.consume(rand.nextInt(MAX_TO_CONSUME-1)+1);

            }

        }catch(Exception e)
        {

        }
    }
}

class Producer implements  Runnable
{
    int MAX_TO_PRODUCE;
    Product product;
    Random rand = new Random();
    public Producer(Product p, int max_to_produce)
    {
        MAX_TO_PRODUCE = max_to_produce;
        product = p;
    }
    @Override
    public void run() {

        try{
        while(true) {
            product.produce(rand.nextInt(MAX_TO_PRODUCE-1)+1);

        }
    }catch(Exception e)
    {

    }
    }
}
public class Main {
    public static final int N = 20;
    public static void main(String[] args)
    {
        int max_to_produce = 10;
        int max_to_consume = 10;
        Product product = new Product(10);
        List<Thread> list = new ArrayList<Thread>();
        Runnable runnable;
        for(int i=0; i<N; i++)
        {

            if(i%2==0)
            {
                runnable = new Consumer(product, max_to_consume);
            }
            else
            {
                runnable = new Producer(product, max_to_produce);
            }

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
