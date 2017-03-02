/**
 * Created by nishu on 3/1/2017.
 */
public class Stopwatch {
    private long startTime = 0;
    private long stopTime = 0;
    private boolean running = false;

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }

    public void stop() {
        this.stopTime = System.currentTimeMillis();
        this.running = false;
    }

    public long getElapsedTimeMillis() {
        long elapsed;
        if (running) {
            elapsed = (System.currentTimeMillis() - startTime);
        } else {
            elapsed = (stopTime-startTime);
        }
        return elapsed;
    }

    public void giveBuffer(long millis) {
        this.startTime -= millis;
    }

    public boolean isRunning() {
        return running;
    }

    public long getElapsedTimeSecs() {
        long elapsed;
        if (running) {
            elapsed = ((System.currentTimeMillis() - startTime) / 1000);
        } else {
            elapsed = ((stopTime - startTime) / 1000);
        }
        return elapsed;
    }

    public void reset() {
        startTime = 0;
        stopTime = 0;
        running = false;
    }
}
