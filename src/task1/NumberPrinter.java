package task1;

/**
 * A Runnable class that prints numbers from 1 to 10.
 */
public class NumberPrinter implements Runnable {

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println("Number: " + i);
            try {
                Thread.sleep(100); // Small delay to show concurrent execution
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
