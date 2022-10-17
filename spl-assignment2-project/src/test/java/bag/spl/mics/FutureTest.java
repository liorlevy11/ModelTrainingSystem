package bag.spl.mics;

import bgu.spl.mics.Future;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class FutureTest {

    private Future<Integer> future;
    private Integer result = 2;

    @Before
    public void setUp() throws Exception {
        future = new Future<>();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void get() throws InterruptedException {
        future.resolve(result);
        Assert.assertEquals(result, future.get());
    }

    @Test
    public void resolve() throws InterruptedException {
        future.resolve(result);
        Assert.assertEquals(result, future.get());
    }

    @Test
    public void isDone() {
        assertFalse(future.isDone());
        future.resolve(result);
        Assert.assertTrue(future.isDone());
    }

    @Test
    public void testGet() {
        long timeout = 1000;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        assertNull(future.get(timeout, unit));
        future.resolve(result);
        Assert.assertEquals(result, future.get(timeout, unit));
    }
}