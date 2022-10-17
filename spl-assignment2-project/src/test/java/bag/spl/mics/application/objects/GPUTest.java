package bag.spl.mics.application.objects;

import bag.spl.mics.objects.*;
import bgu.spl.mics.application.services.GPUService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Timer;

public class GPUTest {
    private static GPU gpu1;
    private static GPU gpu2;
    private static GPU gpu3;

    @Before
    public void setUp() throws Exception {
        GPUService gpuService1 = new GPUService("gpuService1");
        Data data1 = new Data(Data.Type.Images, 0, 1000);
        Student student1 = new Student("Shaun", "cs", Student.Degree.PhD);
        Model model1 = new Model("Lior's Image Model1", data1, student1);

        gpu1 = new GPU(GPU.Type.RTX3090, model1, gpuService1);


        GPUService gpuService2 = new GPUService("gpuService2");
        Data data2 = new Data(Data.Type.Images, 1, 1000);
        Student student2 = new Student("Shaun", "cs", Student.Degree.PhD);
        Model model2 = new Model("Lior's Image Model2", data2, student2);
        model2.setStatus(Model.Status.Training);
        gpu2 = new GPU(GPU.Type.RTX3090, model2, gpuService2);

        GPUService gpuService3 = new GPUService("gpuService3");
        Data data3 = new Data(Data.Type.Images, 999, 1000);
        Student student3 = new Student("Shaun", "cs", Student.Degree.PhD);
        Model model3 = new Model("Lior's Image Model3", data3, student3);

        model3.setStatus(Model.Status.Training);

        gpu3 = new GPU(GPU.Type.RTX3090, model3, gpuService3);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test

    public void process1() {
        Assert.assertTrue(gpu1.getModel().getStatus() != Model.Status.Tested);
        int number_of_batches = gpu1.getModel().getData().getProcessed();
        Assert.assertTrue(gpu1.getModel().getStatus() == Model.Status.PreTrained && number_of_batches == 0);
        // int start = gpu1.getTick();
        //gpu1.process();
        //int end = gpu1.getTick();
        int count_time = 4;
        if (gpu1.getType() == GPU.Type.RTX3090) {
            if (1 <= count_time) {
                Assert.assertTrue(gpu1.getModel().getStatus() == Model.Status.Training &&
                        gpu1.getModel().getData().getProcessed() > number_of_batches);
            } else {
                Assert.fail("The GPU finished too early");
            }
        } else if (gpu1.getType() == GPU.Type.RTX2080) {
            if (2 <= count_time) {
                Assert.assertTrue(gpu1.getModel().getStatus() == Model.Status.Training &&
                        gpu1.getModel().getData().getProcessed() > number_of_batches);
            } else {
                Assert.fail("The GPU finished too early");
            }
        } else if (gpu1.getType() == GPU.Type.GTX1080) {
            if (4 <= count_time) {
                Assert.assertTrue(gpu1.getModel().getStatus() == Model.Status.Training &&
                        gpu1.getModel().getData().getProcessed() > number_of_batches);
            } else {
                Assert.fail("The GPU finished too early");
            }
            // }

        }
    }


    @Test

    public void process2() {
        Assert.assertTrue(gpu2.getModel().getStatus() != Model.Status.Tested);
        int number_of_batches = gpu2.getModel().getData().getProcessed();

        Assert.assertTrue(gpu2.getModel().getStatus() == Model.Status.Training && number_of_batches != 0);

        int count_time = 3;
        if (gpu2.getType() == GPU.Type.RTX3090) {
            if (1 <= count_time) {
                Assert.assertTrue(gpu2.getModel().getStatus() == Model.Status.Training &&
                        gpu2.getModel().getData().getProcessed() >= number_of_batches && !gpu2.isFinished());
            } else {
                Assert.fail("The GPU finished too early");
            }
        } else if (gpu2.getType() == GPU.Type.RTX2080) {
            if (2 <= count_time) {
                Assert.assertTrue(gpu2.getModel().getStatus() == Model.Status.Training &&
                        gpu2.getModel().getData().getProcessed() >= number_of_batches && !gpu2.isFinished());
            } else {
                Assert.fail("The GPU finished too early");
            }
        } else if (gpu2.getType() == GPU.Type.GTX1080) {
            if (4 <= count_time) {
                Assert.assertTrue(gpu2.getModel().getStatus() == Model.Status.Training &&
                        gpu2.getModel().getData().getProcessed() >= number_of_batches && !gpu2.isFinished());
            } else {
                Assert.fail("The GPU finished too early");
            }
            //}

        }
    }

    @Test
    public void process3() {
        Assert.assertTrue(gpu3.getModel().getStatus() != Model.Status.Tested);
        int number_of_batches = gpu3.getModel().getData().getProcessed();
        Assert.assertTrue(gpu3.getModel().getStatus() == Model.Status.Training && number_of_batches != 0);

        int count_time = 2;
        if (gpu3.getType() == GPU.Type.RTX3090) {
            if (1 <= count_time) {
                Assert.assertTrue(gpu3.getModel().getStatus() == Model.Status.Trained &&
                        gpu3.getModel().getData().getProcessed() >= number_of_batches && gpu3.isFinished());
            } else {
                Assert.fail("The GPU finished too early");
            }
        } else if (gpu3.getType() == GPU.Type.RTX2080) {
            if (2 <= count_time) {
                Assert.assertTrue(gpu3.getModel().getStatus() == Model.Status.Trained &&
                        gpu3.getModel().getData().getProcessed() >= number_of_batches && gpu3.isFinished());
            } else {
                Assert.fail("The GPU finished too early");
            }
        } else if (gpu3.getType() == GPU.Type.GTX1080) {
            if (4 <= count_time) {
                Assert.assertTrue(gpu3.getModel().getStatus() == Model.Status.Trained &&
                        gpu3.getModel().getData().getProcessed() >= number_of_batches && gpu3.isFinished());
            } else {
                Assert.fail("The GPU finished too early");
            }
        }
    }


}
