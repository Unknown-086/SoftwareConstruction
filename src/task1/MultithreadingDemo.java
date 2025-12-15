package task1;

/**
 * Lab Task 1: Introduction to Multithreading
 * 
 * This program demonstrates basic multithreading by creating two threads:
 * - One thread prints numbers from 1 to 10
 * - Another thread prints the squares of numbers from 1 to 10
 * 
 * Both threads run concurrently.
 */
public class MultithreadingDemo {

    public static void main(String[] args) {
        // Create two runnable instances
        NumberPrinter numberPrinter = new NumberPrinter();
        SquarePrinter squarePrinter = new SquarePrinter();

        // Create two threads
        Thread thread1 = new Thread(numberPrinter, "NumberThread");
        Thread thread2 = new Thread(squarePrinter, "SquareThread");

        // Start both threads
        System.out.println("Starting both threads...\n");
        thread1.start();
        thread2.start();

        // Wait for both threads to complete
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nBoth threads have completed execution.");
    }
}
