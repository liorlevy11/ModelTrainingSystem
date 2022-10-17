package bag.spl.mics.objects;

import bgu.spl.mics.PublishResultsEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Passive object representing information on a conference.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class ConfrenceInformation {

    private String name;
    private int date;
    private int counterTick = 0;
    private List<String> successfulModelName;//the name of successful models
    private List<Model> successfulModel;
    private List<String> notSuccessfulModelName = new ArrayList<>();
    private int i = 0;
    //List<PublishResultsEvent>

    public ConfrenceInformation(String name, int date) {
        this.name = name;
        this.date = date;
        this.successfulModelName = null;
        this.successfulModel = new ArrayList<>();
        this.notSuccessfulModelName.add("all the models failed");
    }

    public ConfrenceInformation(String name, int date, List<Model> successfulModel) {
        this.name = name;
        this.date = date;
        this.successfulModel = successfulModel;
        successfulModelName();
    }

    private void successfulModelName() {
        for (Model model : successfulModel) {
            addModel(model);
        }
    }

    public void addModel(Model model) {

        System.out.println("this  add modle");
        if(model==null||!(model.getResults()== Model.Results.Good|model.getResults()== Model.Results.Bad)){
            System.out.println("this  add modle null ");}
else {
            String name = "";
            name = model.getName();
            //  successfulModelName.add(name);
            System.out.println("this  add modle  "+model.getName());
            successfulModel.add(model);
            System.out.println("this  add modle succsessful " + name);
        }
    }

 /*   public String publish() {
        String output = "";
        for (String name : successfulModelName) {
            output = output + name + ", ";
        }
        if (output == "")
            output = "all the models failed";
        return output;
    }*/

    /*public List<String> Publish(){
        if(successfulModelName.isEmpty())
            return  notsuccessfulModelName;
        return successfulModelName;
    }
*/
    public List<Model> Publish() {
        System.out.println("ConfrenceInformation: publish-> ");
        //if (successfulModel.isEmpty())
          //  return null;
        return successfulModel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public boolean checkPublish() {
   //     System.out.println("the counter is "+counterTick+"the date is"+date+"ConfrenceInformation: checkPublish-> conf " + i++);
        this.counterTick++;
        if (this.counterTick == date) {
            System.out.println("ConfrenceInformation: checkPublish->  should publish");

            return true;
        }
        return false;

    }

    public List<Model> getSuccessfulModel() {
        return successfulModel;
    }
}

