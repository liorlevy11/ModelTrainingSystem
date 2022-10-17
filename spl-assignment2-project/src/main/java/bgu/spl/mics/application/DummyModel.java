package bgu.spl.mics.application;

import bag.spl.mics.objects.Data;
import bag.spl.mics.objects.Model;

public class DummyModel {
    private String name;
    private DummyData data;
    private Model.Status status;
    private Model.Results results;


    public DummyModel(String name, Data data, Model.Status status, Model.Results results) {
        this.name = name;
        this.data = new DummyData(data);
        this.status = status;
        this.results = results;
    }

    public String getName() {
        return name;
    }
}