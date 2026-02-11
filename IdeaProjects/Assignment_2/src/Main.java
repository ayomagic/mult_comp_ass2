public class Main {
    public static void main(String[] args) {
        int[] threadCounts = {1, 2, 4, 8};
        for (int n : threadCounts) {
            System.out.println("Testing with " + n + " threads:");
            int result = PIncrement.parallelIncrement(0, n);
            System.out.println("Result: " + result);
            System.out.println();
        }
    }
}