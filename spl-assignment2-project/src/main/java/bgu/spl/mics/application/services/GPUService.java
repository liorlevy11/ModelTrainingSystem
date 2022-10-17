package bgu.spl.mics.application.services;

import bag.spl.mics.objects.Cluster;
import bag.spl.mics.objects.GPU;
import bag.spl.mics.objects.Model;
import bag.spl.mics.objects.Student;
import bgu.spl.mics.*;
import bgu.spl.mics.application.OutputFile;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * GPU service is responsible for handling the
 * {@link TrainModelEvent} and {@link TestModelEvent},
 * in addition to sending the .
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {
    private MessageBus messageBus;
    //private int time;
    private GPU gpu;
    private Model currentModel;
    private BlockingQueue<Model> modelQueue;
    private BlockingQueue<TrainModelEvent> trainModelEventBlockingQueue;

    private Cluster cluster = Cluster.getInstance();

    public GPUService(String name) {
        super("GPUService");
        //this.time = 0;
        messageBus = MessageBusImpl.getInstance();
    }

    public GPUService(String name, GPU gpu) {
        super(name);
        //this.time = 0;
        this.modelQueue = new LinkedBlockingQueue<>();
        this.gpu = gpu;
        messageBus = MessageBusImpl.getInstance();
        trainModelEventBlockingQueue = new LinkedBlockingQueue<>();
    }

    @Override
    protected void initialize() {
        messageBus.register(this);
        subscribeBroadcast(TerminateBroadcast.class, lastCall -> this.terminate());

        subscribeBroadcast(TickBroadcast.class, tickIncoming -> {
            //  System.out.println("checkkkkkkkk" + gpu.isFinished());
            if (gpu.isFinished()) {
                ////currentModel.getStudent().increaseI();
                complete(trainModelEventBlockingQueue.take(), null);

                //      System.out.println("helooooooo!!!!!!!!");

                // System.out.println("go to outputfill");



                gpu.setFirst_run(true);

                if (!modelQueue.isEmpty()) {
                    //  System.out.println("liotwin" + modelQueue.size());
                    modelQueue.remove();
                    //    System.out.println("liorwin" + modelQueue.size());
                }
                sendEvent(new TestModelEvent(gpu.getModel(), this));
                if (!modelQueue.isEmpty()) {
                    currentModel = modelQueue.peek();
                   // sendEvent(new Tr)
                    gpu.setCurrentUnProcessedBatchesIndex(0);
                    gpu.setModel(currentModel);
                    gpu.sendUnprocessedBatch();
                }
                System.out.println("TEST MODEL EVEN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");


            } else {
                cluster.getStatistics().increaseNumberOfGPUTimeUnitUsed();
                //  if (modelQueue.peek().getStatus() == Model.Status.PreTrained ||
                //  modelQueue.peek().getStatus() == Model.Status.Training)
                gpu.trainDataBatches();
            }
            /*if(!eventsQueue.isEmpty()){
                Event e = eventsQueue.remove();
                if(e instanceof TrainModelEvent){
                    ((TrainModelEvent)e)
                }
            }*/
        });

        subscribeEvent(TrainModelEvent.class, event -> {
            System.out.println("GPUService->TrainModelEvent  " + event.getModel().getName());
            if (modelQueue.isEmpty()) {
                if (currentModel == null)
                    currentModel = event.getModel();
                gpu.setModel(currentModel);
                gpu.sendUnprocessedBatch();
                System.out.println("GPUService->TrainModelEvent->modelQueue.isEmpty()---->successful");

            }

            this.modelQueue.add(event.getModel());
            this.trainModelEventBlockingQueue.add((event));
            System.out.println("GPUService->TrainModelEvent----->successful");

        });

        subscribeEvent(TestModelEvent.class, event -> {
            //this.eventsQueue.add(event);
            currentModel = event.getModel();
            System.out.println("TEST MODEL EVEN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            Model.Results valueOfTest;
            //masters
            if (event.getSenderStatus() == Student.Degree.MSc) {
                if (Math.random() <= 0.6) {
                    valueOfTest = Model.Results.Good;
                } else
                    valueOfTest = Model.Results.Bad;
            }
            //PhD
            else {
                if (Math.random() <= 0.8) {
                    valueOfTest = Model.Results.Good;
                } else
                    valueOfTest = Model.Results.Bad;
            }

            //Model eventModel = event.getModel();
            currentModel.setResults(valueOfTest);
            // System.out.println("we did set result"+currentModel.getResults().toString());
            currentModel.setStatus(Model.Status.Tested);
            //    System.out.println("we have ststus"+currentModel.getStatus().toString());
            cluster.addFinishedModelName(currentModel.getName());
            OutputFile outputFile = OutputFile.getInstance();
            if(currentModel.getStatus()== Model.Status.Tested){
            outputFile.addToStudentModel((currentModel));}
            // OutputFile outputFile = OutputFile.getInstance();
            // System.out.println("go to outputfill");
            // outputFile.addToStudentModel((currentModel));

            // System.out.println("GPUService->TestModelEvent->Model.Status.Tested---->successful");
            //System.out.println("GPUService- name models"+eventModel.getName());

            //   OutputFile outputFile = OutputFile.getInstance();

            //   outputFile.addToStudentModel(currentModel);

            complete(event, currentModel);
            System.out.println("GPUService->TestModelEvent->complete");


        });

    }

}
