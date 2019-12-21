public class Test {
    public static void main(String[] args) {
        while(!Thread.currentThread().isInterrupted()) {
            System.out.println("dasdasdasdas");
            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
