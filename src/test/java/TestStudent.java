import api.IAdmin;
import api.IInstructor;
import api.IStudent;
import api.core.impl.Admin;
import api.core.impl.Instructor;
import api.core.impl.Student;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by AlbertTMAC on 3/7/17.
 */
public class TestStudent {
    private IAdmin admin;
    private IInstructor instructor;
    private IStudent student;
    private IStudent student2;
    private IStudent student3;

    @Before
    public void setup() {
        this.admin = new Admin();
        this.instructor = new Instructor();
        this.student = new Student();
        this.student2 = new Student();
        this.student3 = new Student();
    }


    //Test for enrolling for a class that exists
    @Test
    public void testRegisterForClass() {
        this.admin.createClass("Class", 2017, "Instructor", 2);
        this.student.registerForClass("Student", "Class", 2017);
        assertTrue(this.student.isRegisteredFor("Student", "Class", 2017));
    }



    //Test for enrolling for a class that does not exist
    @Test
    public void testRegisterForClass2() {
        this.student.registerForClass("Student", "Class", 2017);
        assertFalse("Failure - a student is enrolled for a class that does not exist", this.student.isRegisteredFor("Student", "Class", 2017));
    }


    //Test for enrolling for a class that has not met its enrollment capacity
    @Test
    public void testRegisterForClass3() {
        this.admin.createClass("Class", 2017, "Instructor", 3);
        this.student.registerForClass("Student", "Class", 2017);
        this.student2.registerForClass("Student2", "Class", 2017);
        this.student3.registerForClass("Student3", "Class", 2017);
        assertTrue(this.student3.isRegisteredFor("Student3","Class", 2017));
    }


    //Test for enrolling for a class that has met its enrollment capacity
    @Test
    public void testRegisterForClass4() {
        this.admin.createClass("Class", 2017, "Instructor", 2);
        this.student.registerForClass("Student", "Class", 2017);
        this.student2.registerForClass("Student2", "Class", 2017);
        this.student3.registerForClass("Student3", "Class", 2017);
        assertFalse("Failure - student is enrolled for a class that is already at max capacity", this.student3.isRegisteredFor("Student3","Class", 2017));
    }


    //Test for dropping a class for a student that is enrolled
    @Test
    public void testDropClass(){
        this.admin.createClass("Class", 2017, "Instructor", 5);
        this.student.registerForClass("Student", "Class", 2017);
        this.student.dropClass("Student", "Class", 2017);
        assertTrue(!this.student.isRegisteredFor("Student", "Class", 2017));
    }


    //Test for dropping a class for a student that is not enrolled
    @Test
    public void testDropClass2(){
        this.admin.createClass("Class", 2017, "Instructor", 5);
        this.student.dropClass("Student", "Class", 2017);
        fail("A student dropped a class he is not enrolled in");
    }


    //Test for submitting when a homework exists
    //Test for submitting a homework for a class in the current year 2017
    //Test for submitting a homework when the student is registered for the class
    @Test
    public void testSubmitHomework() {
        this.admin.createClass("Class", 2017, "Instructor", 5);
        this.instructor.addHomework("Instructor", "Class", 2017, "Homework1", "TestHomework");
        this.student.registerForClass("Student", "Class", 2017);
        this.student.submitHomework("Student", "Homework1", "TestAnswer", "Class", 2017);
        assertTrue(this.student.hasSubmitted("Student", "Homework1", "Class", 2017));
    }


    //Test for submitting when a homework does NOT exists
    @Test
    public void testSubmitHomework2() {
        this.admin.createClass("Class", 2017, "Instructor", 5);
        this.student.registerForClass("Student", "Class", 2017);
        this.student.submitHomework("Student", "Homework1", "TestAnswer", "Class", 2017);
        assertFalse("Failure - a student submitted an assignment for a homework that does not exist", this.student.hasSubmitted("Student", "Homework1", "Class", 2017));
    }


    //Test for submitting a homework when the student is NOT registered for the class
    @Test
    public void testSubmitHomework3() {
        this.admin.createClass("Class", 2017, "Instructor", 5);
        this.instructor.addHomework("Instructor", "Class", 2017, "Homework1", "TestHomework");
        this.student.submitHomework("Student", "Homework1", "TestAnswer", "Class", 2017);
        assertFalse("Failure - a student not enrolled for a class submitted homework for that class", this.student.hasSubmitted("Student", "Homework1", "Class", 2017));
    }


    //Test for submitting a homework for a class in a future year 2018
    @Test
    public void testSubmitHomework4() {
        this.admin.createClass("Class", 2018, "Instructor", 5);
        this.instructor.addHomework("Instructor", "Class", 2018, "Homework1", "TestHomework");
        this.student.registerForClass("Student", "Class", 2018);
        this.student.submitHomework("Student", "Homework1", "TestAnswer", "Class", 2018);
        assertFalse("Failure - a student submitted a homework for a class that is not taught in the current year", this.student.hasSubmitted("Student", "Homework1", "Class", 2018));
    }


    //Test for a student submitting a homework with no solution
    @Test
    public void testSubmitHomework5() {
        this.admin.createClass("Class", 2017, "Instructor", 5);
        this.instructor.addHomework("Instructor", "Class", 2017, "Homework1", "TestHomework");
        this.student.registerForClass("Student", "Class", 2017);
        this.student.submitHomework("Student", "Homework1", null, "Class", 2017);
        assertFalse("Failure - student submitted a homework with no solution included", this.student.hasSubmitted("Student", "Homework1", "Class", 2017));
    }


}
