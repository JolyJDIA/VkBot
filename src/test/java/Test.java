public class Test {
    private static long s;
    public static void main(String[] args) {
        for(int i = 0; i < 50; ++i) {
            try {
                isOwner();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static boolean isOwner() {
        if(s > 15) {
            System.out.println("Обновляю");
            s = 0;
        } else {
            s = System.currentTimeMillis()/1000;//начало
            System.out.println(s);
        }
        return true;
    }
}
