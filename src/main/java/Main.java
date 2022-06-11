import net.listerily.NinjaAdventure.App;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) {
        new App().startGame();
        ExecutorService service = Executors.newFixedThreadPool(2);
//        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue();
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//
//                service.submit(new Thread() {
//                    @Override
//                    public void run() {
//                        super.run();
//                        while(true) {
//                            try {
//                                queue.put("Fuck you.");
//                                sleep(2000);
//                            } catch (InterruptedException e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//                    }
//                });
//
//                service.submit(new Thread() {
//                    @Override
//                    public void run() {
//                        super.run();
//                        while(true) {
//                            try {
//                                System.out.println(queue.take());
//                            } catch (InterruptedException e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//                    }
//                });
//
//            }
//        }.start();
    }
}
