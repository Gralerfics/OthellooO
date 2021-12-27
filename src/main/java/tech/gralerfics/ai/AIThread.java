package tech.gralerfics.ai;

public class AIThread extends Thread {
    private ArtificialPlayer aip = new ArtificialPlayer();
    public int result = -2; // -2: 闲置, -1: 运算中, 其他: 结果.

    public void go(int [][] mp, int cur, int depth, boolean cheating) {
        aip.init(mp, cur, depth, cheating);
        result = -1;
    }

    @Override
    public void run() {
        while (true) {
            if (result == -1) {
                int aipRst = aip.process();
                result = aipRst;
            }
            try {
                sleep(120);
            } catch (InterruptedException e) {
                // sleep 故障
            }
        }
    }
}
