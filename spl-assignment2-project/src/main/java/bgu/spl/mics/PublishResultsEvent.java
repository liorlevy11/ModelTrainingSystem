package bgu.spl.mics;

import bag.spl.mics.objects.Model;
import bag.spl.mics.objects.Student;

import java.util.concurrent.atomic.AtomicInteger;

public class PublishResultsEvent implements Event<Model> {
    private AtomicInteger ithEventPublishResultsEvent;
    private Model model;

    public PublishResultsEvent(Model model){
        this.ithEventPublishResultsEvent = new AtomicInteger(0);
        this.model = model;
    }

    @Override
    public void set_ithEvent() {
        this.ithEventPublishResultsEvent.incrementAndGet();
    }

    @Override
    public AtomicInteger get_ithEvent() {
        return ithEventPublishResultsEvent;
    }

    public Model getModel() {
        return this.model;
    }

}
