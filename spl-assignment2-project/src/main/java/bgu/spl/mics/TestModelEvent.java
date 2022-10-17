package bgu.spl.mics;

import bag.spl.mics.objects.Model;
import bag.spl.mics.objects.Student;
import bgu.spl.mics.application.services.GPUService;
import bgu.spl.mics.application.services.StudentService;

import java.util.concurrent.atomic.AtomicInteger;

public class TestModelEvent implements Event<Model> {

    private AtomicInteger ithEventTestModel;

    public TestModelEvent() {

    }
//TODO delete sh!t

    private Model model;
    private MicroService sender;
    private Student.Degree senderDegree;
    private Future<Model> futureModel;

    public TestModelEvent(Model model, StudentService studentService) {
        this.model = model;
        this.sender = studentService;
        this.senderDegree = studentService.getStudent().getStatus();
        this.ithEventTestModel = new AtomicInteger(0);
    }
    public TestModelEvent(Model model, GPUService gpuService) {
        this.model = model;
        this.sender = gpuService;
        this.ithEventTestModel = new AtomicInteger(0);
    }

    public Student.Degree getSenderStatus() {
        return this.senderDegree;
    }

    public Model getModel() {
        return this.model;
    }

    public MicroService getSender() {
        return this.sender;
    }

    public AtomicInteger get_ithEvent() {
        return ithEventTestModel;
    }

    public void set_ithEvent() {
        this.ithEventTestModel.incrementAndGet();
    }

    public Future<Model> getFutureModel() {
        return futureModel;
    }
}
