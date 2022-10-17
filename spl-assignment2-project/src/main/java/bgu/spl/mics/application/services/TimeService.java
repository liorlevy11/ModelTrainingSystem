package bgu.spl.mics.application.services;

import bag.spl.mics.objects.Cluster;
import bgu.spl.mics.Broadcast;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.TickBroadcast;
import bgu.spl.mics.TerminateBroadcast;
import bgu.spl.mics.application.CRMSRunner;
import bgu.spl.mics.application.OutputFile;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService {

    private int currentTick;
    private int tickTime;//the length of a tick in seconds
    private int duration;

    public TimeService(int tickTime, int duration) {
        super("TimeService");
        this.currentTick = 0;
        this.tickTime = tickTime;
        this.duration = duration;
    }

    @Override
    protected void initialize() {
        // TODO Implement this
        subscribeBroadcast(TerminateBroadcast.class, lastCall -> {
            this.terminate();
        });
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (currentTick < duration) {
                    System.out.println("cuurent tick "+currentTick);
                    currentTick += tickTime;
                    TickBroadcast tick = new TickBroadcast(currentTick);
                    sendBroadcast((Broadcast) tick);
                } else {
                    OutputFile outputFile = OutputFile.getInstance();
                    try {
                        //outputFile.trainStudents();
                        outputFile.createJson(CRMSRunner.test);
                        System.out.println(Cluster.getInstance().getStatistics().getGpuTimeUsed());
                        System.out.println(Cluster.getInstance().getStatistics().getNumberOfProcessedBatches());
                        System.out.println(Cluster.getInstance().getStatistics().getName_of_models_trained()/2);
                        System.exit(0);


                    } catch (Exception e) {
//                        e.printStackTrace();
                    }

                    sendBroadcast(new TerminateBroadcast());
                    timer.cancel();
                }
            }
        }, 100, tickTime);
    }

    public int getCurrentTick() {
        return currentTick;
    }
}


