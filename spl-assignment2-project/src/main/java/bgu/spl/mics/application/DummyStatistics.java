package bgu.spl.mics.application;

import bag.spl.mics.objects.Statistics;

public class DummyStatistics {
    private int cpuTimeUsed;
    private int gpuTimeUsed;
    private int batchesProcessed;
    public DummyStatistics(Statistics statistics){
        this.batchesProcessed = statistics.getNumberOfProcessedBatches();
        this.cpuTimeUsed = statistics.getCpuTimeUsed();
        this.gpuTimeUsed = statistics.getGpuTimeUsed();
    }
}