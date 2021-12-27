package tech.gralerfics.ai;

public class AIInterruptThread extends Thread {
    private ArtificialPlayer ap;
    public int limitTime = 10000;
    public boolean needed = false;
    private long ts;

    AIInterruptThread(ArtificialPlayer ap) {
        this.ap = ap;
        ts = System.currentTimeMillis();
    }

    @Override
    public void run() {
        while (true) {
            if (!needed) return;
            long tn = System.currentTimeMillis();
            if (Math.abs(tn - ts) > limitTime) {
//                ap.interrupt = true;
                System.out.println("Stopped!");
                return;
            }
        }
    }
}
