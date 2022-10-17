package bgu.spl.mics;

import bag.spl.mics.objects.Model;

import java.util.LinkedList;
import java.util.List;

public class PublishConferenceBroadcast implements Broadcast {
    private List<Model> models = new LinkedList<>();
    public PublishConferenceBroadcast(List<Model> publish) {
        this.models = publish;
    }

    public PublishConferenceBroadcast() {
    }

    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        for (Model model : models) {
            this.models.add(model);
        }
    }
}
