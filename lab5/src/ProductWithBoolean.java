import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProductWithBoolean implements Product{
    private Integer[] buffer;
    public int MAX_SIZE;
    ReentrantLock lock = new ReentrantLock();
    Condition PIERWSZYPROD = lock.newCondition();
    Condition PIERWSZYKONS = lock.newCondition();
    Condition RESZTAPROD = lock.newCondition();
    Condition RESZTAKONS = lock.newCondition();
    private Integer PIERWSZYPROD_counter = 0;
    private Integer PIERWSZYKONS_counter = 0;
    private Integer RESZTAPROD_counter = 0;
    private Integer RESZTAKONS_counter = 0;
    private boolean isFirstProducerQueueFree= true;
    private boolean isFirstConsumerQueueFree= true;
    public ProductWithBoolean(int x)
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

    private int howManyPlacesTaken()
    {
        int howManyTaken = 0;
        for(int i=0; i<MAX_SIZE; i++)
        {
            if(buffer[i]==1)howManyTaken++;
        }

        return howManyTaken;

    }

    private void printCounters()
    {
        System.out.println("RESZTAKONS: "+Integer.toString(RESZTAKONS_counter)+" PIERWSZYKONS: "+Integer.toString(PIERWSZYKONS_counter)+" RESZTAPROD: "+Integer.toString(RESZTAPROD_counter)+" PIERWSZYPROD: "+Integer.toString(PIERWSZYPROD_counter));
    }

    public void produce(int howMany)
    {
        lock.lock();
        System.out.println("Producer: "+Thread.currentThread().getId()+" entered. He wants to produce: "+howMany+" elements.");
        printCounters();
        try{

            while(!isFirstProducerQueueFree)
            {
                System.out.println("Producer: "+Thread.currentThread().getId()+" is waiting on RESZTAPROD condition.");
                RESZTAPROD_counter++;
                RESZTAPROD.await();
                RESZTAPROD_counter--;
            }


            int howManyFree = howManyFreePlaces();
            while(howManyFree<howMany)
            {
                System.out.println("Producer: "+Thread.currentThread().getId()+" is waiting on PIERWSZYPROD condition.");
                PIERWSZYPROD_counter++;
                isFirstProducerQueueFree=false;
                PIERWSZYPROD.await();
                PIERWSZYPROD_counter--;
                howManyFree = howManyFreePlaces();
            }
            isFirstProducerQueueFree=true;
            //PRODUCING
            System.out.println("Producer: "+Thread.currentThread().getId()+" is producing "+howMany+" elements.");
            int oldHowMany = howMany;
            for(int i=0; i<MAX_SIZE; i++)
            {
                if(buffer[i]==0 && howMany>0){
                    buffer[i]=1;
                    howMany--;
                }
            }
            howManyFree = howManyFreePlaces();
            System.out.println("In buffer there are now: "+(howManyFree)+" places.");
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

    public void consume(int howMany)
    {
        lock.lock();
        printCounters();
        System.out.println("Consumer: "+Thread.currentThread().getId()+" has entered. He wants to consume: "+howMany+" elements.");
        try{

            while(!isFirstConsumerQueueFree)
            {
                System.out.println("Consumer: "+Thread.currentThread().getId()+" is waiting on RESZTAKONS condition.");
                RESZTAKONS_counter++;
                RESZTAKONS.await();
                RESZTAKONS_counter--;
            }

            int howManyTaken = howManyPlacesTaken();

            while(howManyTaken<howMany)
            {
                System.out.println("Consumer: "+Thread.currentThread().getId()+" is waiting on PIERWSZKONS condition.");
                PIERWSZYKONS_counter++;
                isFirstConsumerQueueFree=false;
                PIERWSZYKONS.await();
                PIERWSZYKONS_counter--;
                howManyTaken = howManyPlacesTaken();
            }
            isFirstConsumerQueueFree=true;

            //CONSUMING
            System.out.println("Consumer: "+Thread.currentThread().getId()+" is consuming "+howMany+" elements.");
            int oldHowMany = howMany;
            for(int i=0; i<MAX_SIZE; i++)
            {
                if(buffer[i]==1 && howMany>0){
                    buffer[i]=0;
                    howMany--;
                }
            }
            int howManyFree = howManyFreePlaces();
            System.out.println("In buffer there are now: "+(howManyFree)+" places.");

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
