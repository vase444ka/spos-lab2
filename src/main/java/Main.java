public class Main {

    static public void main(String[] args) throws InterruptedException {

        boolean isValid = true;
        CounterValidator counterCheck = new CounterValidator(new BakeryLock(10), 10);
        for (int i = 0; i < 10; ++i) {
            isValid &= counterCheck.runValidation();
        }

        System.out.println(isValid ? "The best lock algo ever" : "Govno vah code");
    }
}
