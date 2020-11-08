import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;



class Consumer implements Runnable
{
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
                product.consume(rand.nextInt()%10+1);
                System.out.println("Consumer: "+Thread.currentThread().getId());
            }

        }catch(Exception e)
        {

        }
    }
}

class Producer implements  Runnable
{
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
            product.produce(rand.nextInt()%10+1);
            System.out.println("Producent: "+Thread.currentThread().getId());

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
        Product product = new Product();
        List<Thread> list = new ArrayList<Thread>();
        Runnable runnable;
        for(int i=0; i<N; i++)
        {

            if(i%2==0)
            {
                runnable = new Consumer(product);
            }
            else
            {
                runnable = new Producer(product);
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
