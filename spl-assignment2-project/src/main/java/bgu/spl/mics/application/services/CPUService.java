package bgu.spl.mics.application.services;

import bag.spl.mics.objects.CPU;
import bag.spl.mics.objects.Cluster;
import bgu.spl.mics.*;

/**
 * CPU service is responsible for handling the {@link DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class CPUService extends MicroService {

    private MessageBus messageBus;
    private int time;
    private CPU cpu;
    private Cluster cluster = Cluster.getInstance();

    public CPUService(String name) {
        super(name);
        this.time = 0;
        messageBus = MessageBusImpl.getInstance();
    }

    public CPUService(String name, CPU cpu) {
        super(name);
        this.time = 0;
        this.cpu = cpu;
        messageBus = MessageBusImpl.getInstance();
    }

    protected void initialize() {
        messageBus.register(this);
        subscribeBroadcast(TerminateBroadcast.class, lastCall -> this.terminate());

        subscribeBroadcast(TickBroadcast.class, tickIncoming -> {
            cluster.getStatistics().increaseNumberOfCPUTimeUnitUsed();
            cpu.processDataBatches();
        });

    }
}