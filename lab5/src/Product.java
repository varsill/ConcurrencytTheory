public interface Product {
    void consume(int howMany) throws InterruptedException;
    void produce(int howMany) throws InterruptedException;
}
