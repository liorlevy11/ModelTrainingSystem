package bgu.spl.mics.application;


import bag.spl.mics.objects.Model;
import bag.spl.mics.objects.Student;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class OutputFile {

    private DummyJson dummyJson;

    public void trainStudents() {
        dummyJson.trainStudents();
    }

    private static class OutputFileHolder {
        private static OutputFile outputFile = new OutputFile();
    }

    public static synchronized OutputFile getInstance() {

        return OutputFileHolder.outputFile;

    }
    private OutputFile(){
        initDummyObject();
    }
    private void initDummyObject() {
        dummyJson = new DummyJson(CRMSRunner.studentsList, null);
    }


    public void createJson(String path) throws IOException {
        dummyJson.setStatistics();
        dummyJson.setDataWeNeed(CRMSRunner.studentsList);
        Writer writer = new FileWriter(path);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(dummyJson.toString() + "CCCCCCCCCCCCCCCC");
        gson.toJson(dummyJson, writer);
        writer.flush();
        writer.close();
    }

    public void addToStudentModel(Model currentModel) {
        System.out.println("in addToStudentModel"+currentModel.getName());
        dummyJson.addModelToStudent(currentModel);
    }
}