import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Product {
    private Integer[] buffer = new Integer[MAX_SIZE];
    public final static int MAX_SIZE = 10;
    ReentrantLock lock = new ReentrantLock(true);
    Condition PIERWSZYPROD = lock.newCondition();
    Condition PIERWSZYKONS = lock.newCondition();
    Condition RESZTAPROD = lock.newCondition();
    Condition RESZTAKONS = lock.newCondition();
    private int PIERWSZYPROD_counter = 0;
    private int PIERWSZYKONS_counter = 0;
    private int RESZTAPROD_counter = 0;
    private int RESZTAKONS_counter = 0;

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

            while(lock.hasWaiters(PIERWSZYPROD))
            {
                RESZTAPROD_counter++;
                System.out.println("RESZTAKONS: "+Integer.toString(RESZTAKONS_counter)+" PIERWSZYKONS: "+Integer.toString(PIERWSZYKONS_counter)+" RESZTAPROD: "+Integer.toString(RESZTAPROD_counter)+" PIERWSZYPROD: "+Integer.toString(PIERWSZYPROD_counter));
                RESZTAPROD.await();
                RESZTAPROD_counter--;
            }


            int howManyFree = howManyFreePlaces();

            while(howManyFree<howMany)
            {
                PIERWSZYPROD_counter++;
                PIERWSZYPROD.await();
                howManyFree = howManyFreePlaces();
                PIERWSZYPROD_counter--;
            }


            for(int i=0; i<MAX_SIZE; i++)
            {
                if(buffer[i]==0 && howMany>0){
                    buffer[i]=1;
                    howMany--;
                }
            }

            RESZTAPROD.signal();
            PIERWSZYKONS.signal();
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
            while(lock.hasWaiters(PIERWSZYKONS))
            {
                RESZTAKONS_counter++;
                System.out.println("RESZTAKONS: "+Integer.toString(RESZTAKONS_counter)+" PIERWSZYKONS: "+Integer.toString(PIERWSZYKONS_counter)+" RESZTAPROD: "+Integer.toString(RESZTAPROD_counter)+" PIERWSZYPROD: "+Integer.toString(PIERWSZYPROD_counter));
                RESZTAKONS.await();
                RESZTAKONS_counter--;
            }



            int howManyFree = howManyFreePlaces();

            while(MAX_SIZE-howManyFree<howMany)
            {
                PIERWSZYKONS_counter++;
                PIERWSZYKONS.await();
                PIERWSZYKONS_counter--;
                howManyFree = howManyFreePlaces();
            }


            for(int i=0; i<MAX_SIZE; i++)
            {
                if(buffer[i]==1 && howMany>0){
                    buffer[i]=0;
                    howMany--;
                }
            }

            RESZTAKONS.signal();
            PIERWSZYPROD.signal();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }

    }

}
