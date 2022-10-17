package bgu.spl.mics;

import bag.spl.mics.objects.Model;

import java.util.concurrent.atomic.AtomicInteger;

public class TrainModelEvent implements Event<Model> {
    private AtomicInteger ithEventTrainModel;
    private Model model;
    private MicroService sender;
    private Future<Model> future;
    public TrainModelEvent(Model model , MicroService m) {
        this.model = model;
        this.sender = m;
        future = null;
        this.ithEventTrainModel = new AtomicInteger(0);
    }

    public AtomicInteger get_ithEvent() {
        return ithEventTrainModel;
    }


    public void set_ithEvent() {
        this.ithEventTrainModel.incrementAndGet();
    }

    public MicroService getSender() {
        return this.sender;
    }

    public Model getModel() {
        return this.model;
    }
    public Future<Model> getFuture() {
        return this.future;
    }
}
