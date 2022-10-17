package bag.spl.mics.objects;

import bgu.spl.mics.application.services.GPUService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @pre this.getModel() is PreTrained
 * @pre start = gpu1.getTick()
 * @inv @pre != tested && (@pre gpu1.getModel().getData().getProcessed()+1<=@post gpu1.getModel().getData().getProcessed())
 * @post end = gpu1.getTick()
 * @post if(@ pre = = preTrained & & ( end - start)>=gpu1.getTickTime() ) -> m==Training
 * @pre this.getModel() Training
 * @pre start = gpu1.getTick()
 * @inv @pre != tested && (@pre gpu.getModel().getData().getProcessed()+1<=@post gpu1.getModel().getData().getProcessed())
 * @post end = gpu1.getTick()
 * @post if(@ pre = = Training & & ( end - start)>=gpu1.getTickTime() && !isFinished ())-> m = Training
 * @pre this.getModel() Training
 * @pre start = gpu1.getTick()
 * @inv @pre != tested && (@pre gpu.getModel().getData().getProcessed()+1<=@post gpu1.getModel().getData().getProcessed())
 * @post end = gpu1.getTick()
 * @post if(@ pre = = Training & & ( end - start)>=gpu1.getTickTime() && !isFinished ())-> m = Training
 * @pre this.getModel() Training
 * @pre start = gpu1.getTick()
 * @inv @pre != tested && (@pre gpu.getModel().getData().getProcessed()+1<=@post gpu1.getModel().getData().getProcessed())
 * @post end = gpu1.getTick()
 * @post if(@ pre = = Training & & ( end - start)>=gpu1.getTickTime() && !isFinished ())-> m = Training
 * @pre this.getModel() Training
 * @pre start = gpu1.getTick()
 * @inv @pre != tested && (@pre gpu.getModel().getData().getProcessed()+1<=@post gpu1.getModel().getData().getProcessed())
 * @post end = gpu1.getTick()
 * @post if(@ pre = = Training & & ( end - start)>=gpu1.getTickTime() && !isFinished ())-> m = Training
 * @pre this.getModel() Training
 * @pre start = gpu1.getTick()
 * @inv @pre != tested && (@pre gpu.getModel().getData().getProcessed()+1<=@post gpu1.getModel().getData().getProcessed())
 * @post end = gpu1.getTick()
 * @post if(@ pre = = Training & & ( end - start)>=gpu1.getTickTime() && !isFinished ())-> m = Training
 * @pre this.getModel() Training
 * @pre start = gpu1.getTick()
 * @inv @pre != tested && (@pre gpu.getModel().getData().getProcessed()+1<=@post gpu1.getModel().getData().getProcessed())
 * @post end = gpu1.getTick()
 * @post if(@ pre = = Training & & ( end - start)>=gpu1.getTickTime() && !isFinished ())-> m = Training
 * @pre this.getModel() Training
 * @pre start = gpu1.getTick()
 * @inv @pre != tested && (@pre gpu.getModel().getData().getProcessed()+1<=@post gpu1.getModel().getData().getProcessed())
 * @post end = gpu1.getTick()
 * @post if(@ pre = = Training & & ( end - start)>=gpu1.getTickTime() && !isFinished ())-> m = Training
 * @pre this.getModel() Training
 * @pre start = gpu1.getTick()
 * @inv @pre != tested && (@pre gpu.getModel().getData().getProcessed()+1<=@post gpu1.getModel().getData().getProcessed())
 * @post end = gpu1.getTick()
 * @post if(@ pre = = Training & & ( end - start)>=gpu1.getTickTime() && !isFinished ())-> m = Training
 * @pre this.getModel() Training
 * @pre start = gpu1.getTick()
 * @inv @pre != tested && (@pre gpu.getModel().getData().getProcessed()+1<=@post gpu1.getModel().getData().getProcessed())
 * @post end = gpu1.getTick()
 * @post if(@ pre = = Training & & ( end - start)>=gpu1.getTickTime() && !isFinished ())-> m = Training
 * @pre this.getModel() Training
 * @pre start = gpu1.getTick()
 * @inv @pre != tested && (@pre gpu.getModel().getData().getProcessed()+1<=@post gpu1.getModel().getData().getProcessed())
 * @post end = gpu1.getTick()
 * @post if(@ pre = = Training & & ( end - start)>=gpu1.getTickTime() && !isFinished ())-> m = Training
 */
/**
 * @pre this.getModel() Training
 * @pre start = gpu1.getTick()
 * @inv @pre != tested && (@pre gpu.getModel().getData().getProcessed()+1<=@post gpu1.getModel().getData().getProcessed())
 * @post end = gpu1.getTick()
 * @post if(@ pre = = Training & & ( end - start)>=gpu1.getTickTime() && !isFinished ())-> m = Training
 */

/**
 * @pre this.getModel()  Training
 * @pre start = gpu1.getTick()
 * @inv @pre != tested && (@pre gpu.getModel().getData().getProcessed()+1<=@post gpu1.getModel().getData().getProcessed())
 * @post end = gpu1.getTick()
 * @post if(@ pre = = Training & & ( end - start)>=gpu1.getTickTime() && isFinished ()) -> m = Trained
 */

public class GPU {

    private boolean first_run;

    /**
     * Enum representing the type of the GPU.
     */
    public enum Type {RTX3090, RTX2080, GTX1080}

    private int i = 0;
    public Type type;
    private Cluster cluster;
    private Model model;
    private final int MAX_UNPROCESSED_BATCHES_IN_CLUSTER = 10000;
    private int currentUnProcessedBatchesIndex = 0;
    private int VRAM;
    private int currentAvailableMemory;
    private LinkedList<DataBatch> dataBatches;
    private BlockingQueue<Pair<DataBatch, AtomicInteger>> timeToTrainABatch;//from the processing ones

    public GPU(Type type, Model model, GPUService gpuService) {
        this.cluster = Cluster.getInstance();
        this.model = model;
        // this.gpuService = gpuService;

    }

    public void setFirst_run(boolean first_run) {
        this.first_run = first_run;
    }

    public GPU(Type type) {

        switch (type) {
            case RTX3090:
                this.currentAvailableMemory = 32;
                this.VRAM = 32;
                break;
            case RTX2080:
                this.currentAvailableMemory = 16;
                this.VRAM = 16;
                break;
            case GTX1080:
                this.currentAvailableMemory = 8;
                this.VRAM = 8;
                break;
        }
        this.timeToTrainABatch = new LinkedBlockingQueue<>();
        dataBatches = new LinkedList<>();
        this.cluster = Cluster.getInstance();
        this.type = type;
        this.first_run = true;
    }

    public int getTimeToProcess() {
        switch (type) {
            case RTX3090:
                return 1;
            case RTX2080:
                return 2;
            case GTX1080:
                return 4;
            default:
                throw new IllegalArgumentException();
        }
    }

    public boolean canReceiveFromCluster() {
        //    System.out.println("GPU->canReceiveFromCluster->this currentAvailableMemory is "+currentAvailableMemory+" my GPU is"+this);

        return currentAvailableMemory <= this.VRAM && this.currentAvailableMemory > 0;
    }

    public boolean finishedTraining(Integer temp) {
        return temp == 0;
    }

    public void trainDataBatches() {
        //     System.out.println("train gpu " + i++);
        if (!timeToTrainABatch.isEmpty()) {
            Pair<DataBatch,AtomicInteger> dataBatch = this.timeToTrainABatch.peek();
            Integer temp = dataBatch.getSecond().decrementAndGet();
            //       System.out.println("temp" + temp);
            if (finishedTraining(temp)) {
                System.out.println("effective Train");
                this.timeToTrainABatch.remove(dataBatch);
                sendUnprocessedBatch();
                currentAvailableMemory++;

            }
        }
    }


    public void getProcessedBatchFromCluster(DataBatch dataBatch) {//receives a processed batch from the cluster and adds the batch to the toTrainMap

        //    System.out.println("check");
        if (dataBatch != null) {
            this.currentAvailableMemory -= 1;
            this.currentUnProcessedBatchesIndex--;
            this.dataBatches.remove(dataBatch);//TODO: check if needed
            this.timeToTrainABatch.add(new Pair<>(dataBatch, new AtomicInteger(getTimeToProcess())));
        } else
            throw new IllegalArgumentException();

    }


    public void divideDataToBatches() {
        //     System.out.println("deviding the data" + i);
        for (int i = 0; i < model.getData().getSize(); i += 1000) {
            Data data = new Data(getModel().getData().type, 1000);
            DataBatch dataBatch = new DataBatch(data, 0);//might be 0
            dataBatches.add(dataBatch);
        }
        first_run = false;
    }

    public boolean isFinished() {

        boolean bool = dataBatches.isEmpty() & !first_run;
        if (bool)
            System.out.println("finshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
        return bool;
    }

    public void sendUnprocessedBatch() {

        while (this.currentUnProcessedBatchesIndex < this.MAX_UNPROCESSED_BATCHES_IN_CLUSTER && dataBatches.size() != 0) {
            if (this.dataBatches.isEmpty()) {
                //    this.currentUnProcessedBatchesIndex--;//????
            } else {
                DataBatch dataBatch = this.dataBatches.pop();
                this.cluster.receiveUnProcessedBatch(this, dataBatch);
                this.currentUnProcessedBatchesIndex++;

            }
        }

    }


    public Model getModel() {
        return model;
    }

    public Type getType() {
        return type;
    }

    public void setModel(Model model) {
        this.model = model;
        this.dataBatches.clear();//TODO: check this line
        divideDataToBatches();
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setCurrentUnProcessedBatchesIndex(int a) {
        this.currentUnProcessedBatchesIndex = a;
    }
}