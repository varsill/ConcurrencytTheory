import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Main {
    public static final int N = 5;
    public static void main(String[] args)
    {
        ArrayList<Semaphore> forks = new ArrayList<>();
        for(int i=0; i<N; i++)
        {
            forks.add(new Semaphore());
        }
        List<Thread> list = new ArrayList<Thread>();
        for(int i=0; i<N; i++)
        {

            int finalI = i;
            Runnable runnable = () ->
            {
                while(true) {
                    Semaphore left_fork, right_fork;
                    if (finalI == N - 1) {
                        left_fork = forks.get((finalI+1) % N);
                        right_fork = forks.get((finalI) % N);
                    } else {
                        left_fork = forks.get((finalI) % N);
                        right_fork = forks.get((finalI + 1) % N);
                    }
                    left_fork.semwait();
                    right_fork.semwait();
                    //eating


                    System.out.println(finalI);

                    right_fork.semsignal();
                    left_fork.semsignal();
                }
            };

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
