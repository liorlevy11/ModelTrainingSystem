package bag.spl.mics.objects;

import bgu.spl.mics.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing single student.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Student {




    public void increasePublications() {
        this.publications.incrementAndGet();
        System.out.println("we in increasePublications");
    }

    public void increaseNumberOfReadPaper() {
        this.papersRead.incrementAndGet();
        System.out.println("we in increaseNumberOfReadPaper");
    }

    public int getI() {
        return this.i.intValue();
    }

    public int getProcessed() {
        return this.processed.intValue();
    }

    /**
     * Enum representing the Degree the student is studying for.
     */
    public enum Degree {MSc, PhD}

    private String name;
    private String department;
    private Degree status;
    private AtomicInteger publications;
    private AtomicInteger papersRead;
    private List<Model> studentsModels;
    //private Future<Model> modelFuture=null;
    private AtomicInteger numberOfTestedModels = new AtomicInteger(0);
    private AtomicInteger i =new AtomicInteger(0) ;
    private AtomicInteger processed;

    public void increaseI() {
        if(i.intValue()<studentsModels.size()){
        this.i.incrementAndGet();}
    }

    public Student(String name, String department, Degree status) {
        this.name = name;
        this.department = department;
        this.status = status;
        this.publications = new AtomicInteger(0);
        this.papersRead = new AtomicInteger(0);
        this.studentsModels = new ArrayList<>();


    }

    public void setNumberOfTestedModels() {
        this.numberOfTestedModels.incrementAndGet();
        System.out.println("we in incrementTestedModelIndex" + numberOfTestedModels);
    }
   /* public Future<Model> getFutureModel() {
        return modelFuture;
    }*/

    public AtomicInteger getNumberOfTestedModels() {
        return this.numberOfTestedModels;
    }

    /* public void setFuture(Future<Model> model) {

         System.out.println("------------------------getnum");
         System.out.println("------------------------------------------getnumlior"+model.get().getName()+model.get().getResults()+model.get().getStatus());
         this.modelFuture = model;
     }
 */
    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(Degree status) {
        this.status = status;
    }

    public Degree getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public AtomicInteger getPapersRead() {
        return papersRead;
    }

    public AtomicInteger getPublications() {
        return publications;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setPapersRead(AtomicInteger papersRead) {
        this.papersRead = papersRead;
    }

    public void setPublications(AtomicInteger publications) {
        this.publications = publications;
    }

    public void setStudentModels(List<Model> studentsModels) {
        this.studentsModels = studentsModels;
    }

}

