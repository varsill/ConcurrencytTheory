import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Product {
    private Integer[] buffer;
    public int MAX_SIZE;
    Lock lock = new ReentrantLock(true);
    Condition waitForProducing = lock.newCondition();
    Condition waitForConsuming = lock.newCondition();



    public Product(int x)
    {
        MAX_SIZE = x;
        buffer = new Integer[MAX_SIZE];
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

    public void produce(int howMany)
    {


        lock.lock();
        System.out.println("Producer: "+Thread.currentThread().getId()+" entered. He wants to produce: "+howMany+" elements.");
        try{
            int howManyFree = howManyFreePlaces();

            while(howMany>howManyFree)
            {
                System.out.println("Producer: "+Thread.currentThread().getId()+" is waiting. ");
                waitForProducing.await();
                howManyFree = howManyFreePlaces();
            }
            //PRODUCING
            System.out.println("Producer: "+Thread.currentThread().getId()+" is producing "+howMany+" elements.");
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

    public void consume(int howMany)
    {
        lock.lock();
        System.out.println("Consumer: "+Thread.currentThread().getId()+" entered. He wants to produce: "+howMany+" elements.");
        try{
            int howManyTaken = MAX_SIZE-howManyFreePlaces();
            while(howManyTaken==0)
            {
                System.out.println("Consumer: "+Thread.currentThread().getId()+" is waiting. ");
                waitForConsuming.await();
                howManyTaken = MAX_SIZE-howManyFreePlaces();
            }
            //CONSUMING
            System.out.println("Consumer: "+Thread.currentThread().getId()+" is consuming "+howMany+" elements.");
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
