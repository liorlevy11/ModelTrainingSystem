package bgu.spl.mics.application;

import bag.spl.mics.objects.Model;
import bag.spl.mics.objects.Student;

import java.util.ArrayList;
import java.util.List;

public class DummyStudent {
    private String name;
    private String department;
    private Student.Degree status;
    private int publications;
    private int papersRead;
    //private DummyModel[] trained;
    private List<DummyModel> trained = new ArrayList<>();


    public DummyStudent(String name, String department, Student.Degree status, int publications, int papersRead, int processed) {
        this.name = name;
        this.department = department;
        this.status = status;
        this.publications = publications;
        this.papersRead = papersRead;

        //this.trained = trained;
    }

    public DummyStudent(Student student) {
        this.name = student.getName();
        this.department = student.getDepartment();
        this.status = student.getStatus();
        this.publications = student.getPublications().intValue();
        this.papersRead = student.getPapersRead().intValue();


    }

    /*public void setTrained() {
        this.trained = new DummyModel[dummyModelList.size()];
        for (int i = 0; i < this.trained.length; i++) {
            this.trained[i] = dummyModelList.get(i);
        }
    }*/

    public void addTrained(Model model) {
        System.out.println("in Grate" + model.getName());
        boolean flag = false;
        DummyModel dummyModel = new DummyModel(model.getName(), model.getData(), model.getStatus(), model.getResults());
        for (DummyModel dummyModel1 : trained) {
            if (dummyModel1.getName().equals(dummyModel.getName())) {
                flag = true;
                break;
            }
        }
        if (!flag)
            this.trained.add(dummyModel);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public Student.Degree getStatus() {
        return status;
    }

    public int getPublications() {
        return publications;
    }

    public int getPapersRead() {
        return papersRead;
    }

    public void setPublications(Student student) {
        this.publications = student.getPublications().intValue();
    }

    public void setPapersRead(Student student) {
        this.papersRead = student.getPapersRead().intValue();
    }


}