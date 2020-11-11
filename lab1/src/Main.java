import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static final int N = 100000;
    public static void main(String[] args)
    {
        startRunnables(N);
    }

    public static void startRunnables(int howMany){
        MyInteger x = new MyInteger();
        List<Thread> list = new ArrayList<Thread>();
        Semaphore sem = new Semaphore();

        for(int i=0; i<howMany; i++)
        {
            Runnable runnable;
            if(i%2==0){
                runnable= () -> {
                    sem.semwait();
                    x.increment();
                    sem.semsignal();
                };
            }
            else{
                runnable= () -> {
                    sem.semwait();
                    x.decrement();
                    sem.semsignal();
                };
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

        x.print();
    }

    public static void startThreads(int howMany){
        for(int i=0; i<howMany; i++)
        {
            MyThread t = new MyThread();
            t.start();
        }
    }
}
