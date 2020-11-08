import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Product {
    private Integer[] buffer = new Integer[MAX_SIZE];
    public final static int MAX_SIZE = 10;



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

    public synchronized void produce(int value) throws InterruptedException
    {
            int howManyFree = howManyFreePlaces();

            while(howManyFree==0)
            {
                wait();
                howManyFree = howManyFreePlaces();
            }

            for(int i=0; i<MAX_SIZE; i++)
            {
                if(buffer[i]==0){
                    buffer[i]=value;
                    continue;
                }

            }

            notify();

    }

    public synchronized int consume() throws InterruptedException
    {
            int howManyTaken = MAX_SIZE-howManyFreePlaces();
            while(howManyTaken==0)
            {
                wait();
                howManyTaken = MAX_SIZE-howManyFreePlaces();
            }
            int result = 0;
            for(int i=0; i<MAX_SIZE; i++)
            {
                if(buffer[i]!=0){
                    result = buffer[i];
                    buffer[i]=0;
                    continue;
                }
            }


            notify();
            return result;


    }

}
