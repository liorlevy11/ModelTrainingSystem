package bgu.spl.mics.application;

import bag.spl.mics.objects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    private static List<List<Model>> models = new ArrayList<>();
    public static List<Student> studentsList = new ArrayList<>();
    private static List<GPU> gpuArrayList = new ArrayList<>();
    private static List<CPU> cpuArrayList = new ArrayList<>();
    private static List<ConfrenceInformation> ConferencesArrayList = new ArrayList<>();
    private static int tickTime;
    private static int duration;
    private static List<Thread> threadsList = new ArrayList<>();
    private static Thread thread1;

    public static String test;


    public static void main(String[] args) {
        try {
            objectsInitialization(args);
            initializeServices();

            /*joinThreads();
            thread1.join();*/
            //OutputFile outputFile = OutputFile.getInstance();
            System.out.println("++++++___");
            test = args[1];
//            outputFile.createJson(args[1]);
        } catch ( IOException | JsonIOException | JsonSyntaxException e) {
            e.printStackTrace();
        }


    }

    private static void joinThreads() throws InterruptedException {
        for (Thread thread : threadsList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("The thread was interrupted. " + thread.toString());
            }
        }
    }

    private static void initializeServices() {

        for (int i = 0; i < cpuArrayList.size(); i++) {
            Runnable runnable = new CPUService("CPUService: " + i,cpuArrayList.get(i));
            threadsList.add(new Thread(runnable));
        }

        //GPU SERVICE
        for (int i = 0; i < gpuArrayList.size(); i++) {
            Runnable runnable = new GPUService("GPUService: " + i,gpuArrayList.get(i));
            threadsList.add(new Thread(runnable));
        }
        //CPU SERVICE

        //STUDENT SERVICE
        for (int i = 0; i < studentsList.size(); i++) {
            Runnable runnable = new StudentService("StudentService: " + i, models.get(i),
                    studentsList.get(i));
            threadsList.add(new Thread(runnable));
        }
        //CONFERENCE SERVICE
        for (int i = 0; i < ConferencesArrayList.size(); i++) {
            Runnable runnable = new ConferenceService("ConferenceService: " + i,ConferencesArrayList.get(i));
            threadsList.add(new Thread(runnable));
        }
        //TIME SERVICE
        for(Thread thread : threadsList){
            thread.start();
        }
        try {
            Thread.sleep(50);
        }catch(Exception e){

        }
        Runnable runnableTime = new TimeService(tickTime, duration);
        thread1 = new Thread(runnableTime);
        thread1.start();
    }

    /*private static Thread startTask(Runnable runnableTask) {
        Thread thread = new Thread(runnableTask);
        thread.start();
        return thread;
    }*/

    private static void objectsInitialization(String[] args) throws IOException, JsonIOException, JsonSyntaxException {
        try {
            //create the gson
            Gson gson = new Gson();
            //read the file
            Reader reader = Files.newBufferedReader(Paths.get(args[0]));
            // convert JSON file to map
            Map<?, ?> map = gson.fromJson(reader, Map.class);
            readingObjects(map);

            Cluster cluster = Cluster.getInstance();
            cluster.setCpus(cpuArrayList);
            cluster.setGpus(gpuArrayList);

            // close reader
            reader.close();

        } catch (IOException | JsonIOException | JsonSyntaxException ex) {
            ex.printStackTrace();
        }
    }

    private static void readingObjects(Map<?, ?> map) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {

            if (entry.getKey().equals("Students")) {
                studentInit(entry);
            } else if (entry.getKey().equals("GPUS")) {
                gpusInit(entry);
            } else if (entry.getKey().equals("CPUS")) {
                cpusInit(entry);
            } else if (entry.getKey().equals("Conferences")) {
                conferencesInit(entry);
            } else if (entry.getKey().equals("TickTime")) {
                tickTimeInit(entry);

            } else if (entry.getKey().equals("Duration")) {
                durationInit(entry);
            }
        }
    }

    private static void durationInit(Map.Entry<?, ?> entry) {
        duration = (int)(Double.parseDouble(String.valueOf(entry.getValue())));
    }

    private static void tickTimeInit(Map.Entry<?, ?> entry) {
	tickTime = (int)(Double.parseDouble(String.valueOf(entry.getValue())));
    }

    private static void conferencesInit(Map.Entry<?, ?> entry) {
        ArrayList<LinkedTreeMap> Conferences = (ArrayList<LinkedTreeMap>) entry.getValue();
        for (LinkedTreeMap conference : Conferences) {
            ConferencesArrayList.add(new ConfrenceInformation((String) conference.get("name"), (int) (double) conference.get("date")));
        }
    }

    private static void cpusInit(Map.Entry<?, ?> entry) {
        ArrayList<Double> cpus = (ArrayList<Double>) entry.getValue();
        for (Double cpu : cpus) {
            cpuArrayList.add(new CPU((int) (double) cpu));
        }
    }

    private static void gpusInit(Map.Entry<?, ?> entry) {
        ArrayList<String> gpus = (ArrayList<String>) entry.getValue();
        for (String gpu : gpus) {
            GPU.Type type = convertStringToGpuType(gpu);
            gpuArrayList.add(new GPU(type));
        }
    }

    private static void studentInit(Map.Entry<?, ?> entry) {
        ArrayList<LinkedTreeMap> students = (ArrayList) entry.getValue();
        for (LinkedTreeMap student : students) {
            ArrayList<LinkedTreeMap> models1 = (ArrayList<LinkedTreeMap>) student.get("models");

            Student student1 = new Student((String) student.get("name"), (String) student.get("department"),
                    convertStringToStatus((String) student.get("status")));
            models.add(getModel(models1, student1));
            studentsList.add(student1);

        }
        int j = 0;
        for (Student student : studentsList) {
            student.setStudentModels(models.get(j++));
        }
    }

    private static GPU.Type convertStringToGpuType(String gpuType) {
        switch (gpuType) {
            case ("RTX3090"):
                return GPU.Type.RTX3090;
            case ("RTX2080"):
                return GPU.Type.RTX2080;
            case ("GTX1080"):
                return GPU.Type.GTX1080;
            default:
                return null;
        }
    }

    private static Student.Degree convertStringToStatus(String status) {
        switch (status) {
            case ("MSc"):
                return Student.Degree.MSc;
            case ("PhD"):
                return Student.Degree.PhD;
            default:
                return null;
        }
    }

    private static List<Model> getModel(ArrayList<LinkedTreeMap> models, Student student) {
        List<Model> models_to_return = new ArrayList<>();
        for (LinkedTreeMap model : models) {

            Model model1 = new Model((String) model.get("name"), convertStringToData((String) model.get("type"), ((int) (double) model.get("size"))), student);
            models_to_return.add(model1);
        }
        return models_to_return;
    }

    private static Data convertStringToData(String type, int size) {
        return new Data(convertStringToType(type), size);
    }

    private static Data.Type convertStringToType(String type) {
        switch (type) {
            case ("Images"):
                return Data.Type.Images;
            case ("Tabular"):
                return Data.Type.Tabular;
            case ("Text"):
                return Data.Type.Text;
            default:
                return null;
        }
    }
}
