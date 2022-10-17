package bgu.spl.mics.example.messages;

import bgu.spl.mics.Event;

import java.util.concurrent.atomic.AtomicInteger;

public class ExampleEvent implements Event<String>{

    private String senderName;

    public ExampleEvent(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    @Override
    public AtomicInteger get_ithEvent() {
        return null;
    }

    @Override
    public void set_ithEvent() {

    }
}