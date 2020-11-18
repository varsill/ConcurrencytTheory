import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
 enum STATE{
    EMPTY, UNDER_PRODUCTION, FILLED, UNDER_CONSUMPTION
 }
public class Monitor {

    LinkedList<Integer> freeElementsQueue = new LinkedList<>();
    LinkedList<Integer> takenElementsQueue = new LinkedList<>();
    Lock lock = new ReentrantLock();
    Condition CZEK_PROD = lock.newCondition();
    Condition CZEK_KONS = lock.newCondition();
    //Condition[] PRZYDZ_KONS;
    STATE[] state;
    int bufferSize;
    //boolean[] isPortionReady;
    public Monitor(int bufferSize)
    {
        this.bufferSize=bufferSize;
        //PRZYDZ_KONS = new Condition[bufferSize];
        //isPortionReady = new boolean[bufferSize];
        state = new STATE[bufferSize];
        for(int i=0; i<bufferSize; i++)
        {
            //PRZYDZ_KONS[i]=lock.newCondition();
        }
        for(int i=0; i<bufferSize; i++)
        {
            freeElementsQueue.add(i);
        }
        for(int i=0; i<bufferSize; i++)
        {
            //isPortionReady[i]=false;
        }
        for(int i=0; i<bufferSize; i++)
        {
            state[i]=STATE.EMPTY;
        }
    }

    private void howManyInUse()
    {

        int empty, underProduction, filled, underConsumption;
        empty=underProduction=filled=underConsumption=0;
        for(int i=0; i<bufferSize; i++)
        {
            if(state[i]==STATE.EMPTY)empty++;
            else if(state[i]==STATE.UNDER_PRODUCTION)underProduction++;
            else if(state[i]==STATE.FILLED)filled++;
            else if(state[i]==STATE.UNDER_CONSUMPTION)underConsumption++;
        }
        System.out.println("EMPTY: "+empty+" UNDER PRODUCTION: "+underProduction+" FILLED: "+filled+" UNDER CONSUMPTION: "+underConsumption);
    }

    public int startProducing() throws InterruptedException {
        lock.lock();
        howManyInUse();
        while(freeElementsQueue.isEmpty())CZEK_PROD.await();
        int i = freeElementsQueue.pop();

        //isPortionReady[i]=false;
        state[i]=STATE.UNDER_PRODUCTION;
        CZEK_KONS.signal();
        System.out.println("Producer: "+Thread.currentThread().getId()+" has started producing on: "+i);
        lock.unlock();
        return i;

    }

    public void finishProducing(int i)
    {
        lock.lock();
        howManyInUse();
        //isPortionReady[i]=true;
        takenElementsQueue.add(i);
        state[i]=STATE.FILLED;
        CZEK_KONS.signal();
        //PRZYDZ_KONS[i].signal();
        System.out.println("Producer: "+Thread.currentThread().getId()+" has finished producing on: "+i);
        lock.unlock();
    }
    public int startConsuming() throws InterruptedException {
        lock.lock();
        howManyInUse();
        while(takenElementsQueue.isEmpty())CZEK_KONS.await();
        int i = takenElementsQueue.pop();
        //while(!isPortionReady[i])PRZYDZ_KONS[i].await();

        state[i]=STATE.UNDER_CONSUMPTION;
        System.out.println("Consumer: "+Thread.currentThread().getId()+" has started consuming on: "+i);
        lock.unlock();
        return i;
    }

    public void finishConsuming(int i)
    {
        lock.lock();
        howManyInUse();
        System.out.println("Consumer: "+Thread.currentThread().getId()+" has finished consuming on: "+i);
        state[i]=STATE.EMPTY;
        freeElementsQueue.add(i);
        CZEK_PROD.signal();
        lock.unlock();
    }

}
