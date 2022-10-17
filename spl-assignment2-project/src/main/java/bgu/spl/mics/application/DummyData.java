package bgu.spl.mics.application;

import bag.spl.mics.objects.Data;

public class DummyData {
    private Data.Type type;
    private int size;

    public DummyData(Data data) {
        this.type = data.getType();
        this.size = data.getSize();
    }
}
