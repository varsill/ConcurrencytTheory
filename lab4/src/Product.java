import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Product {
    private Integer[] buffer = new Integer[MAX_SIZE];
    public final static int MAX_SIZE = 10;
    Lock lock = new ReentrantLock(true);
    Condition waitForProducing = lock.newCondition();
    Condition waitForConsuming = lock.newCondition();



    public Product()
    {
        for(int i=0; i<MAX_SIZE; i++)
        {
            buffer[i]=0;
        }
    }
    private int howManyFreePlaces()
    {
        int howManyFree = 0;
        for(int i=0; i<MAX_SIZE; i++)
        {
            if(buffer[i]==0)howManyFree++;
        }

        return howManyFree;

    }

    public void produce(int howMany) throws InterruptedException
    {


        lock.lock();

        try{
            int howManyFree = howManyFreePlaces();

            while(howMany>howManyFree)
            {
                waitForProducing.await();
                howManyFree = howManyFreePlaces();
            }

            for(int i=0; i<MAX_SIZE; i++)
            {
                if(buffer[i]==0 && howMany>0){
                    buffer[i]=1;
                    howMany--;
                }

            }

            waitForConsuming.signal();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }


    }

    public void consume(int howMany) throws InterruptedException
    {
        lock.lock();
        try{
            int howManyTaken = MAX_SIZE-howManyFreePlaces();
            while(howManyTaken==0)
            {
                waitForConsuming.await();
                howManyTaken = MAX_SIZE-howManyFreePlaces();
            }

            for(int i=0; i<MAX_SIZE; i++)
            {
                if(buffer[i]==1&&howMany>0){
                    buffer[i]=0;
                    howMany--;
                }
            }

            waitForProducing.signal();


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally{
            lock.unlock();
        }

    }

}
