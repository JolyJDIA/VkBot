public class Test {
    public static void main(String[] args) {
        long start = System.nanoTime();
        for(int i = 0; i < 5; ++i) {
            if(i != 2) {
                System.out.println(i);
            }
        }
        long end = System.nanoTime() - start;
        System.out.println(end+" наносек");
        //249900 //335500
        //326000 //266400
        //297200 //253200
    }
}
