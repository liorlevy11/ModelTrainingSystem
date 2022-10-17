package bgu.spl.mics.application.services;

import bag.spl.mics.objects.Cluster;
import bag.spl.mics.objects.ConfrenceInformation;
import bag.spl.mics.objects.Model;
import bgu.spl.mics.*;

import java.util.List;

/**
 * Conference service is in charge of
 * aggregating good results and publishing them via the ,
 * after publishing results the conference will unregister from the system.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ConferenceService extends MicroService {
    private MessageBus messageBus;
    private ConfrenceInformation conference;
    private Cluster cluster = Cluster.getInstance();

    //private List<Model> modelList=null;
    //private  List<String> nameOfModelList= null;

    public ConferenceService(String name,ConfrenceInformation conference) {
        super(name);
        messageBus = MessageBusImpl.getInstance();
        this.conference = conference;
    }

    @Override
    protected void initialize() {
        messageBus.register(this);
        subscribeBroadcast(TerminateBroadcast.class, lastCall -> this.terminate());

        subscribeBroadcast(TickBroadcast.class, tickIncoming -> {
            if (conference.checkPublish()) {
            //    System.out.println("ConfrenceService: Tick->checkPublish==true-> new PublishConferenceBroadcast ");
                sendBroadcast(new PublishConferenceBroadcast(conference.getSuccessfulModel()));
                messageBus.unregister(this);
                System.out.println("ConfrenceService: Tick->checkPublish==true-> new PublishConferenceBroadcast----->succesful ");

            }
        });

/*
        subscribeBroadcast(PublishConferenceBroadcast.class, broadcast -> {
            System.out.println("ConfrenceService: PublishConferenceBroadcast->setModels and unregisterst ");
            broadcast.setModels(conference.Publish());

            messageBus.unregister(this);
            System.out.println("ConfrenceService: PublishConferenceBroadcast->setModels and unregisterst ------>succesful");
*/



        subscribeEvent(PublishResultsEvent.class, event -> {
            System.out.println("ConfrenceService: PublishResultsEvent->need to add Model");
            Model successfulModel = event.getModel();
            System.out.println("ConfrenceService: PublishResultsEvent->need to add Model----> almost");
            conference.addModel(successfulModel);
            System.out.println("ConfrenceService: PublishResultsEvent->need to add Model------->succeful");

        });


    }
}
