package bgu.spl.mics.application.services;

import bag.spl.mics.objects.Model;
import bag.spl.mics.objects.Pair;
import bag.spl.mics.objects.Student;
import bgu.spl.mics.*;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Student is responsible for sending the {@link TrainModelEvent},
 * {@link TestModelEvent} and {@link PublishResultsEvent}.
 * In addition, it must sign up for the conference publication broadcasts.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class StudentService extends MicroService {
    MessageBus messageBus = MessageBusImpl.getInstance();
    private Student student;
    private Vector<Pair<Model, Boolean>> modelList;// TODO :the boolean is in order to not send the same event 10000 times

    private Future<Model> futureTrain;
    private Future<Model> futureTest;
    private final Object lock1 = new Object();

    public StudentService(String name, List<Model> modelList, Student student) {
        super(name);
        this.modelList = new Vector<>();
        for (Model model : modelList) {
            this.modelList.add(new Pair<>(model, false));
        }
        this.student = student;
        this.futureTest = null;
        this.futureTrain = null;
    }

    public Student getStudent() {
        return this.student;
    }

    @Override
    protected void initialize() {
        messageBus.register(this);

        subscribeBroadcast(TerminateBroadcast.class, lastCall ->
        {
/*
            outputFileCreator output = outputFileCreator.getInstance();
            //sending the data and storing it in a Thread Safe DS
            output.getDataFromStudentMS(self.getMyStudent(), self.getMyModels());*/
            terminate();

        });
        subscribeBroadcast(TickBroadcast.class, tickIncoming -> onTick());

        subscribeBroadcast(PublishConferenceBroadcast.class, broadcast -> {
              List<Model> modelList = broadcast.getModels();
              if(modelList!=null){
          for (Model model : modelList) {
               System.out.println("we are in StudentService -PublishConferenceBroadcast-check models ->equals(student) ");
                if (model.getStudent().equals(student))
                    this.student.increasePublications();
               else
                    this.student.increaseNumberOfReadPaper();
           }}
              else {
                  System.out.println("model list is not good");
              }
        });


    }

    public void onTick() {

        if (student.getI() < this.modelList.size() && this.modelList.get(student.getI()).getFirst().getStatus() == Model.Status.PreTrained) {

      //      this.modelList.get(student.getI()).setSecond(true);//visited
            this.modelList.get(student.getI()).getFirst().setStatus(Model.Status.Training);

            Model model = this.modelList.get(student.getI()).getFirst();
            TrainModelEvent trainModelEvent = new TrainModelEvent(model, this);//send if not visited
            futureTrain = super.sendEvent(trainModelEvent);
     //       System.out.println("send this?");
            futureTrain.get();
            System.out.println("yes !!!!! send this");
            this.modelList.get(student.getI()).getFirst().setStatus(Model.Status.Trained);
            futureTest = sendEvent(new TestModelEvent(model, this));
     //       System.out.println("its futue");
            model = futureTest.get();
           System.out.println("its model" + model.getStatus());
            modelStatusCases(model, futureTest.get().getStatus());
       //     System.out.println("its casese" + this.student.getI());
            //  if(student.getI()==0||model.getStatus()== Model.Status.Tested){
                System.out.println("look" + model.getStatus()+ "i is "+this.student.getI()+" s- name is "+ this.student.getName());

            //  System.out.println("and now i is "+this.student.getI()+" s- name is "+ this.student.getName());}
            System.out.println("i is bigger" + this.student.getI());
        }
    }

    //}
/*}
        //   futureTrain = student.getFutureModel();
        //AtomicInteger currentModelToSend = new AtomicInteger(this.student.getNumberOfTestedModels().intValue());
        //System.out.println("future***********"+currentModelToSend);
        System.out.println("model size of+_+_+_+_+_+" +this.modelList);
//student.getI()-1<this.modelList.size()&&
        synchronized (lock1) {
        if ( student.getNumberOfTestedModels().intValue() < this.modelList.size()&&student.getI()<this.modelList.size()&&
                !this.modelList.get(student.getI()).getSecond() ){
            System.out.println(this.modelList.size()+"model size ofodel size of+_+_+_+_+_" +this.modelList.get(student.getI()).getFirst().getName()+"    "+ this.modelList+"  the size is"+this.modelList.capacity());

            System.out.println(student.getI() + " this is i");

            Model model = this.modelList.get(student.getI()).getFirst();
            System.out.println("studentservice  modelname " + model.getName());
            TrainModelEvent trainModelEvent = new TrainModelEvent(model, this);//send if not visited
          //  futureTrain = super.sendEvent(trainModelEvent);
             super.sendEvent(trainModelEvent);

            System.out.println("futuer trainname is" +" futureTrain.get().getName()");
            System.out.println("now i is" + this.student.getI());
            System.out.println("futuer train status is " + model.getStatus());
            this.modelList.get(student.getI()).setSecond(true);//visited
                futureTest = sendEvent(new TestModelEvent(model, this));
                // model = futureTrain.get();
          //  synchronized (lock1) {
                model = futureTest.get();

                System.out.println("checck if model is null befor send to modelstatus" + model.getName());
                System.out.println("futuer test status is " + model.getStatus() + " " + model.getName());
                modelStatusCases(model, futureTest.get().getStatus());
            this.student.increaseI();
            }


        }
    }*/


    private void modelStatusCases(Model currentModel, Model.Status status) {
        /*if (status == Model.Status.Trained) {
            TestModelEvent testModelEvent = new TestModelEvent(currentModel, this);
            this.student.setModel(sendEvent(testModelEvent));
        } else*/
   //     System.out.println(" status is " + status);
        if (status == Model.Status.Tested) {
            this.student.increaseI();
     //       System.out.println("the result is" + currentModel.getResults());
            //this.student.setNumberOfTestedModels();
            if (currentModel.getResults() == Model.Results.Good) {

                PublishResultsEvent publishEvent = new PublishResultsEvent(currentModel);//TODO
                System.out.println(" befor send future test +publishEvent");
                sendEvent(publishEvent);
                System.out.println("future test +publishEvent");
                /*System.out.println("lior levy");
                System.out.println("-----------------------------a-" + futureTrain.get().getStudent().getName());
                this.student.setFuture(futureTrain);
                //this.student.setNumberOfTestedModels();//TODO look at this function
                this.i++;*/
            }
            //this.student.setFuture(null);
        }


    }


}
