public class MyThread extends Thread {
    public void run() {
        System.out.println("Thread "+currentThread().getId()+"is running.");
        try {
            for(int i = 0; i < 4; i++) {
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            System.out.println("Thread "+currentThread().getId()+" is interrupted.");
        }
        System.out.println("Thread "+currentThread().getId()+" is exiting.");
    }
}
