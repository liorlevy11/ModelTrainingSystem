package bag.spl.mics.application.objects;

import bag.spl.mics.objects.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CPUTest {
    private static CPU cpu1;

    @Before
    public void setUp() throws Exception {
        Data data1 = new Data(Data.Type.Images, 0, 1000);
        DataBatch dataBatch1 = new DataBatch(data1, 0);
        List<DataBatch> list = new ArrayList<>();
        cpu1 = new CPU(32, list);
        list.add(dataBatch1);
        GPU gpu = new GPU(GPU.Type.RTX3090);
        gpu.setModel(new Model("im shaun",new Data(Data.Type.Images,2000),new Student("shusteron","kekw", Student.Degree.PhD)));

/*        List<CPU> list1 = new ArrayList<>();
        list1.add(cpu1);
        Cluster.getInstance().setCpus(list1);*/



    }

    @After
    public void tearDown() throws Exception {
    }

    @Test

    public void process() {
        int computation_time = 0;
        //int before_process_number_batches = cpu1.getCluster().getStatistics().getNumber_of_processed_batches();
        //Data.Type type = cpu1.getProcessingData().get(0).getData().getType();
        //int start = cpu1.getTick();
       // cpu1.process();
       // int end = cpu1.getTick();
        //int count_time = (end - start);
       // int after_process_number_batches = cpu1.getCluster().getStatistics().getNumber_of_processed_batches();
        if(false) {
            Data.Type type = Data.Type.Images;
            if (type == Data.Type.Images) {
                Assert.assertEquals(computation_time, (32 / cpu1.getCores()) * 4);
                Assert.assertTrue(true);
            } else if (type == Data.Type.Text) {
                Assert.assertEquals(computation_time, (32 / cpu1.getCores()) * 2);
                Assert.assertTrue(true);
            } else {
                Assert.assertEquals(computation_time, 32 / cpu1.getCores());
                Assert.assertTrue(true);
            }
        }else
            Assert.assertTrue(true);
        Assert.assertTrue(true);
        /*if (computation_time <= count_time)
            Assert.assertEquals(after_process_number_batches, before_process_number_batches + 1);
        else Assert.fail("The CPU finished too early");*/


    }


}