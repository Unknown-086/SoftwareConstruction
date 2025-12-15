package task1;

/**
 * A Runnable class that prints the squares of numbers from 1 to 10.
 */
public class SquarePrinter implements Runnable {

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println("Square of " + i + ": " + (i * i));
            try {
                Thread.sleep(100); // Small delay to show concurrent execution
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
