public class Semaphore
{
    private boolean value;


    public Semaphore()
    {
        value = true;
    }

    public synchronized void semsignal()
    {
        value=true;
        notify();
    }

    public synchronized void semwait()
    {
        while(value==false)
        {
            try
            {
                wait();
            }catch(Exception e)
            {

            }
        }
        value = false;
    }
}
