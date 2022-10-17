package bag.spl.mics;

import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import bgu.spl.mics.example.services.ExampleBroadcastListenerService;
import bgu.spl.mics.example.services.ExampleEventHandlerService;
import bgu.spl.mics.example.services.ExampleMessageSenderService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class MessageBusTest {
    private MessageBus messageBus;
    private ExampleEvent exampleEvent;
    private ExampleBroadcast exampleBroadcast;
    private ExampleEventHandlerService eventHandlerService;
    private ExampleMessageSenderService exampleMessageSenderService;
    private Future<String> future;
    private ExampleMessageSenderService broadcastMessageSender;
    private ExampleBroadcastListenerService broadcastListenerService;

    @Before

    public void setUp()throws Exception {
        messageBus = MessageBusImpl.getInstance();
        eventHandlerService = new ExampleEventHandlerService("lior", new String[]{"1"});
        exampleMessageSenderService = new ExampleMessageSenderService("sender1", new String[]{"request"});
        exampleEvent = new ExampleEvent("sender1");//exE
        exampleBroadcast =new ExampleBroadcast("shaun");
        broadcastMessageSender = new ExampleMessageSenderService("sender1", new String[]{"broadcast"});
        broadcastListenerService = new ExampleBroadcastListenerService("BroadcastListenerService", new String[]{"1"});
    }

    @After
    public void tearDown()throws Exception{
    }

    @Test
    public void subscribeEvent() {
        messageBus.register(eventHandlerService);
        messageBus.subscribeEvent(exampleEvent.getClass(), eventHandlerService);
        messageBus.sendEvent(exampleEvent);
        try {
            Assert.assertEquals(messageBus.awaitMessage(eventHandlerService).getClass(), ExampleEvent.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void complete() {
        messageBus.register(eventHandlerService);
        messageBus.subscribeEvent(exampleEvent.getClass(), eventHandlerService);
        future = messageBus.sendEvent(exampleEvent);
        Assert.assertFalse(future.isDone());
        messageBus.complete(exampleEvent, "YES");
        try {
            Assert.assertTrue(future.isDone());
            Assert.assertEquals(messageBus.awaitMessage(eventHandlerService).getClass(), (exampleEvent.getClass()));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sendBroadcast() {
        messageBus.register(broadcastMessageSender);
        messageBus.register(broadcastListenerService);
        messageBus.subscribeBroadcast(exampleBroadcast.getClass(), broadcastListenerService);
        messageBus.sendBroadcast(exampleBroadcast);

        try {
            Assert.assertEquals(messageBus.awaitMessage(broadcastListenerService).getClass(), ExampleBroadcast.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        messageBus.unregister(broadcastMessageSender);
        messageBus.unregister(broadcastListenerService);
    }
    @Test

    public void subscribeBroadcast() {
        messageBus.register(broadcastMessageSender);
        messageBus.register(broadcastListenerService);
        messageBus.subscribeBroadcast(exampleBroadcast.getClass(), broadcastMessageSender);
        messageBus.sendBroadcast(exampleBroadcast);
        messageBus.unregister(broadcastMessageSender);
        messageBus.unregister(broadcastListenerService);
    }


    @Test

    public void sendEvent() {
        messageBus.register(exampleMessageSenderService);
        messageBus.register(eventHandlerService);
        messageBus.subscribeEvent(exampleEvent.getClass(), exampleMessageSenderService);
        future = messageBus.sendEvent(exampleEvent);
        Assert.assertNotEquals(future.getClass(), exampleEvent.getClass());
    }

    @Test

    public void register() {
        messageBus.register(exampleMessageSenderService);
        messageBus.register((eventHandlerService));
        messageBus.subscribeEvent(exampleEvent.getClass(), exampleMessageSenderService);

        future = messageBus.sendEvent(exampleEvent);
        Assert.assertNotEquals(future.getClass(), exampleEvent.getClass());
    }

    @Test

    public void unregister() {
        messageBus.register(eventHandlerService);
        try {
            messageBus.unregister(eventHandlerService);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test

    public void awaitMessage() {
        messageBus.register(exampleMessageSenderService);
        messageBus.register(eventHandlerService);
        messageBus.subscribeEvent(exampleEvent.getClass(), exampleMessageSenderService);
        messageBus.sendEvent(exampleEvent);
        messageBus.register(broadcastMessageSender);
        messageBus.register(broadcastListenerService);
        messageBus.subscribeBroadcast(exampleBroadcast.getClass(), broadcastListenerService);
        messageBus.sendBroadcast(exampleBroadcast);
        try {
            Assert.assertEquals(messageBus.awaitMessage(exampleMessageSenderService).getClass(), ExampleEvent.class);
            Assert.assertEquals(messageBus.awaitMessage(broadcastListenerService).getClass(), ExampleBroadcast.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}