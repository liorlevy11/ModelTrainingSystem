package bag.spl.mics.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {
    private int cores;
    private BlockingDeque<DataBatch> processingData;
    private Cluster cluster;
    // private List<Integer> tick_times;
    private HashMap<DataBatch, AtomicInteger> timeToTrainABatch;//from the GPU
    private int i = 0;

    public CPU(int cores, List<DataBatch> processingData) {
        this.cores = cores;
        this.processingData = pushDataBatchToProcessingData(processingData);
        this.cluster = Cluster.getInstance();
       // this.tick_times = computeTime();
        setTimeToTrainABatch();
    }

    public CPU(int cores) {
        this.cores = cores;
        this.processingData = new LinkedBlockingDeque<>();
        //  this.tick_times = computeTime();
        this.timeToTrainABatch = new HashMap<>();
        this.cluster = Cluster.getInstance();

    }


    /**
     * @pre b = this.getCluster().getStatistics().getNumber_of_processed_batches()
     * @inv b>=0
     * @post (b. @ pre = = b. @ post - 1)
     */

    ///DELETE?
    private List<Integer> computeTime() {
        int size = 32 / cores;
        List<Integer> compute_times = new ArrayList<>();
        for (DataBatch dataBatch : processingData) {
            Data.Type type = dataBatch.getData().getType();
            if (type == Data.Type.Images) {
                compute_times.add(size * 4);
            } else if (type == Data.Type.Text) {
                compute_times.add(size * 2);
            } else {
                compute_times.add(size * 1);
            }
        }
        return compute_times;
    }


    public void setTimeToTrainABatch() {//receives a processed batch from the cluster and adds the batch to the toTrainMap
        for (DataBatch dataBatch : processingData) {
            this.timeToTrainABatch.put(dataBatch, new AtomicInteger(getTimeToProcess(dataBatch)));
        }
    }

    public int getTimeToProcess(DataBatch dataBatch) {
        int size = 32 / cores;
        Data.Type type = dataBatch.getData().getType();
        if (type == Data.Type.Images)
            return size * 4;
        else if (type == Data.Type.Text) {
            return size * 2;
        } else
            return size * 1;
    }
    //System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++Processing the data cpu :" + i++);
    //if (!this.timeToTrainABatch.isEmpty()) {
    // System.out.println("look here" + dataBatch == null);
    //  System.out.println("lior !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
    // System.out.println("befor " + this.timeToTrainABatch.get(dataBatch).doubleValue() + " cpu " + this.toString());
//  System.out.println("after " + this.timeToTrainABatch.get(dataBatch).doubleValue() + " cpu " + this.toString());


    public void processDataBatches() {

        DataBatch dataBatch = this.processingData.peek();
        if (dataBatch != null&&timeToTrainABatch.get(dataBatch)!=null) {
            Integer temp = this.timeToTrainABatch.get(dataBatch).decrementAndGet();
            if (finishedTraining(temp)) {
                System.out.println("CPU->processDataBatches->finishedTraining");
                sendProcessedBatch(dataBatch);
                System.out.println("CPU->processDataBatches->sendProcessedBatch");
                if (!processingData.remove(dataBatch)) {
                    System.out.println("didnt remove from Q");
                } else {
                    this.timeToTrainABatch.remove(dataBatch);
                }
            }
        }
    }

    // }

    private void sendProcessedBatch(DataBatch dataBatch) {//(DataBatch dataBatch)
        if (dataBatch != null) {
            this.cluster.sendProcessedBatch(dataBatch);
           // System.out.println("CPU->sendProcessedBatch->successful");

        }
    }
    //  if (dataBatch != null) {
    //  System.out.println("tree");
    //  System.out.println("time to proccess " + timeToTrainABatch.get(dataBatch));

    //TODO look/if where we push 1 dataBanch to CPU
    public void getUnProcessedBatchFromCluster(DataBatch dataBatch) {//receives a processed batch from the cluster and adds the batch to the toTrainMap
    //    System.out.println("CPU->getUnProcessedBatchFromCluster");
        this.processingData.add(dataBatch);
        this.timeToTrainABatch.put(dataBatch, new AtomicInteger(getTimeToProcess(dataBatch)));
    //    System.out.println("CPU->getUnProcessedBatchFromCluster-successful");
        //  } else
        //     throw new IllegalArgumentException();

    }


    public BlockingDeque<DataBatch> pushDataBatchToProcessingData(List<DataBatch> listToProcessingData) {
        BlockingDeque<DataBatch> queueProcessingData = new LinkedBlockingDeque<>();
        for (DataBatch dataBatch : listToProcessingData) {
            queueProcessingData.add(dataBatch);
            getUnProcessedBatchFromCluster(dataBatch);

        }
        return queueProcessingData;

    }

    ////////////////////delete?
    public synchronized boolean isFinished() {
        Boolean isEmpty = true;
        for (DataBatch dataBatch : processingData) {
            if (dataBatch != null)
                isEmpty = false;
        }
        return isEmpty;
    }


    public boolean finishedTraining(Integer temp) {
   //     System.out.println("CPU->finishedTraining-> the Temp is " + temp);
        return temp == 0;
    }

    //   public List<Integer> getTick_times() {
    //       return tick_times;
    //  }

    public Cluster getCluster() {
        return cluster;
    }

    public int getCores() {
        return cores;
    }


    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public void setCores(int cores) {
        this.cores = cores;
    }


    public void receiveBatchToProcess(DataBatch dataBatch) {
  //      System.out.println("first");
        getUnProcessedBatchFromCluster(dataBatch);
    }
}