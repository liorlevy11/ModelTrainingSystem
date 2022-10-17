package bag.spl.mics.objects;

import java.util.Collection;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class Statistics {
    private List<String> name_of_models_trained;
    private AtomicInteger number_of_processed_batches;
    private AtomicInteger cpu_time_unit_used;
    private AtomicInteger gpu_time_unit_used;

    public Statistics() {
        this.name_of_models_trained = new Vector<>();
        this.number_of_processed_batches = new AtomicInteger(0);
        cpu_time_unit_used = new AtomicInteger(0);
        gpu_time_unit_used = new AtomicInteger(0);
    }

    public int getCpuTimeUsed() {
        return cpu_time_unit_used.intValue();
    }

    public int getGpuTimeUsed() {
        return this.gpu_time_unit_used.intValue();
    }

    public int getNumberOfProcessedBatches() {
        return this.number_of_processed_batches.intValue();
    }
    public void addFinishedModelName(String name) {
        this.name_of_models_trained.add(name);
    }

    public void increaseNumberOfProcessedBatches() {
        this.number_of_processed_batches.incrementAndGet();
        System.out.println("number of processed: "+this.number_of_processed_batches);
    }

    public void increaseNumberOfGPUTimeUnitUsed() {
        this.gpu_time_unit_used.incrementAndGet();
    }



    public void increaseNumberOfCPUTimeUnitUsed() {
        this.cpu_time_unit_used.incrementAndGet();
    }

    public int getName_of_models_trained() {
        return this.name_of_models_trained.size();
    }
}