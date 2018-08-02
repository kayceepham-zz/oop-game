package byog.Core;

import java.util.concurrent.TimeUnit;

public class Time extends KeyReader {
    long starts;

    public Time() {
        reset();
    }

    public static Time start() {
        return new Time();
    }

    private Time reset() {
        starts = System.nanoTime();
        return this;
    }

    public long timeTaken() {
        long ends = System.nanoTime();
        return ends - starts;
    }

    public long timeTaken(TimeUnit unit) {
        return unit.convert(timeTaken(), TimeUnit.NANOSECONDS);
    }

    public String TimeRecorder() {
        String timeResult = ("Total Time: " + String.format("%d min, %d sec", timeTaken(TimeUnit.MINUTES),
                timeTaken(TimeUnit.SECONDS) - timeTaken(TimeUnit.MINUTES) * 60));

        return timeResult;
    }

}
