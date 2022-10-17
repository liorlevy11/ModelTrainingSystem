package bgu.spl.mics.application;

import bag.spl.mics.objects.*;

import java.util.List;

public class DummyJson {
    private DummyStudent[] students;
    //private DummyConference[] conferences;
    private DummyStatistics statistics;

    public DummyJson(List<Student> students, List<ConfrenceInformation> confrenceInformations){
        this.students = new DummyStudent[students.size()];
        for (int i=0;i<this.students.length;i++){
            this.students[i] = new DummyStudent(students.get(i));
        }

    }
    public void setStatistics(){
        this.statistics = new DummyStatistics(Cluster.getInstance().getStatistics());
    }
    public void addModelToStudent(Model currentModel) {
        Student student = currentModel.getStudent();
        System.out.println("in addToStudentModel DM"+currentModel.getName());
        for(DummyStudent dummyStudent : students){
            if(DummyEqualReal(dummyStudent,student)){

                System.out.println("in addToStudentModel Demy is good?"+currentModel.getName());
                dummyStudent.addTrained(currentModel);
            }
        }
    }
    private boolean DummyEqualReal(DummyStudent dummyStudent,Student student){
        return dummyStudent.getName().equals(student.getName()) &&
                dummyStudent.getDepartment().equals(student.getDepartment())&&
                dummyStudent.getPapersRead()  == student.getPapersRead().intValue()&&
                dummyStudent.getStatus() == (student.getStatus())&&
                dummyStudent.getPublications() == student.getPublications().intValue();
    }
    public void setDataWeNeed(List<Student> students){
        for(int i=0;i< students.size();i++){
            this.students[i].setPapersRead(students.get(i));
            this.students[i].setPublications(students.get(i));

        }
    }

    public void trainStudents() {
        /*for(DummyStudent student : this.students){
            student.setTrained();
        }*/
    }
}