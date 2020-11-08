import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static final int N = 1000;
    public static void main(String[] args)
    {
        startRunnables(N);
    }

    public static void startRunnables(int howMany){
        MyClass x = new MyClass();
        List<Thread> list = new ArrayList<Thread>();
        Random random = new Random();
        for(int i=0; i<howMany; i++)
        {
            Runnable runnable;
            if(i%2==0){
                runnable= () -> {
                    x.increment();
                    try{
                        Thread.sleep(random.nextInt()%1000);
                    }catch(Exception e){

                    }
                };
            }
            else{
                runnable= () -> {
                    x.decrement();
                    try{
                        Thread.sleep(10);
                    }
                    catch(Exception e){

                    }
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
