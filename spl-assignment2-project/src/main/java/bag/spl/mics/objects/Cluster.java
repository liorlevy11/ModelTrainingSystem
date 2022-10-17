package bag.spl.mics.objects;


import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {



    /*
    public static Cluster getClusterInstance() {
        if (clusterInstance == null) clusterInstance = new Cluster();
        return clusterInstance;
    }*/

    private int currentBatch = 0;
    private List<GPU> gpus;
    private List<Pair<CPU, Integer>> cpus;
    private int currentCPU = 0;
    private Statistics statistics;
    private ConcurrentHashMap<DataBatch, GPU> mapProcessedDataToGPU;
    //private ConcurrentHashMap<CPU, BlockingDeque<DataBatch>> batchesToProcess;

    /**
     * Retrieves the single instance of this class.
     */
/*
    private static Cluster clusterInstance = null;

*/
    private static class SingletonHolder {
        private static Cluster cluster = new Cluster();
    }

    public static synchronized Cluster getInstance() {
        // TODO Auto-generated method stub
        return SingletonHolder.cluster;
    }

    private Cluster() {

        this.gpus = new ArrayList<>();
        this.cpus = new ArrayList<>();
        this.statistics = new Statistics();
        this.mapProcessedDataToGPU = new ConcurrentHashMap<>();
        //this.batchesToProcess = new ConcurrentHashMap<>();
    }

    public List<Pair<CPU, Integer>> setCpus(List<CPU> cpus) {
        List<Pair<CPU, Integer>> list = new ArrayList<>();
        for (CPU cpu : cpus) {
            list.add(new Pair<>(cpu, cpu.getCores() / 8));
        }
        this.cpus = list;
        return list;
    }


    public Statistics getStatistics() {
        return this.statistics;
    }

    public void addFinishedModelName(String name) {
        this.statistics.addFinishedModelName(name);

        System.out.println("Cluster -> name: "+ name+"****************************************");
    }

    public void receiveProcessedBatch(DataBatch processedDataBatch) {
        System.out.println("c'mon");
        this.statistics.increaseNumberOfProcessedBatches();
        GPU gpu = this.mapProcessedDataToGPU.get(processedDataBatch);
        if (gpu.canReceiveFromCluster()) {
            gpu.getProcessedBatchFromCluster(processedDataBatch);
        } else {
            System.out.println("probably an error");
        }
    }

    public void sendUnProcessedBatch(DataBatch dataBatch) {
        Pair<CPU,Integer> cpu = cpus.get(this.currentCPU % cpus.size());
        if (this.currentBatch < cpu.getSecond()) {
            cpu.getFirst().receiveBatchToProcess(dataBatch);
            this.currentBatch++;
        } else {
            currentCPU++;
            currentBatch = 0;
        }
    }

    public void sendProcessedBatch(DataBatch dataBatch)//receive
    {
        receiveProcessedBatch(dataBatch);
        /*System.out.println("c'mon");
        this.mapProcessedDataToGPU.get(dataBatch).getProcessedBatchFromCluster(dataBatch);*/
    }

    public void receiveUnProcessedBatch(GPU gpu,DataBatch batchToProcess) {
        this.mapProcessedDataToGPU.put(batchToProcess,gpu);//should be processed
        sendUnProcessedBatch(batchToProcess);
    }

    public void setGpus(List<GPU> gpuArrayList) {
        this.gpus = gpuArrayList;
    }
}