import java.util.List;
import java.util.Map;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        Test test = new Test();
        Map<Class<?>, List<EventCaller.Handler>> map = EventCaller.register(test);

        long start = System.nanoTime();
        Map<Class<?>, List<EventCaller.Handler>> map1 = EventCaller.register(test);
        long end = System.nanoTime() - start;
        System.out.println(end);
        for(int i = 0; i < 100; ++i) {
            map.get(MessageEvent.class).get(0).accept(new MessageEvent("dadasdasd"));
            Thread.sleep(150);
        }
    }
    @SubscribeEvent
    public void onMsg(MessageEvent e) {
        System.out.println(e.getString());
    }
}
