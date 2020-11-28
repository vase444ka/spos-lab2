public class Counter {
    private Integer data = 0;

    public Counter() {}

    public void increment() {
        ++data;
    }

    public void decrement() {
        --data;
    }


    public String toString() {
        return data.toString();
    }
}
