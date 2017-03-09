import api.IAdmin;
import api.IStudent;
import api.core.impl.Admin;
import api.core.impl.Student;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by Albert on 3/1/2017.
 */

public class TestAdmin {
    private IAdmin admin;
    private IStudent student1;
    private IStudent student2;
    private IStudent student3;

    @Before
    public void setup() {
        this.admin = new Admin();
        this.student1 = new Student();
        this.student2 = new Student();
        this.student3 = new Student();
    }


    //Tests making a class w/ valid year
    @Test
    public void testMakeClass() {
        this.admin.createClass("Test", 2017, "Instructor", 15);
        assertTrue(this.admin.classExists("Test", 2017));
    }


    //Tests making a class with invalid year
    @Test
    public void testMakeClass2() {
        this.admin.createClass("Test", 2016, "Instructor", 15);
        assertFalse("Failure - created a class before the current year",this.admin.classExists("Test", 2016));
    }


    //Tests making a class with valid capacity >0
    @Test
    public void testMakeClass3() {
        this.admin.createClass("Test", 2017, "Instructor", 1);
        assertTrue(this.admin.classExists("Test", 2017));
    }


    //Tests making a class with invalid capacity of zero
    @Test
    public void testMakeClass4() {
        this.admin.createClass("Test", 2017, "Instructor", 0);
        assertFalse("Failure - a class exists with a capacity of 0", this.admin.classExists("Test", 2017));
    }


    //Tests making a class with invalid capacity of negative
    @Test
    public void testMakeClass5() {
        this.admin.createClass("Test", 2017, "Instructor", -5);
        assertFalse("Failure - a class exists with a capacity of <0", this.admin.classExists("Test", 2017));
    }

    //Tests having 3 classes with the same instructor in the same year
    @Test
    public void testMakeClass6() {
        this.admin.createClass("TestA", 2017, "Instructor1", 10);
        this.admin.createClass("TestB", 2017, "Instructor1", 10);
        this.admin.createClass("TestC", 2017, "Instructor1", 10);
        assertTrue(this.admin.classExists("TestA", 2017));
        assertTrue(this.admin.classExists("TestB", 2017));
        assertFalse("Failure - a third class was created with an instructor already teaching two classes in the same year", this.admin.classExists("TestC", 2017));
    }


    //Tests having 3 classes with the same instructor in all different years
    @Test
    public void testMakeClass7() {
        this.admin.createClass("TestA", 2017, "Instructor1", 10);
        this.admin.createClass("TestB", 2018, "Instructor1", 10);
        this.admin.createClass("TestC", 2019, "Instructor1", 10);
        assertTrue(this.admin.classExists("TestA", 2017));
        assertTrue(this.admin.classExists("TestB", 2018));
        assertTrue(this.admin.classExists("TestC", 2019));
    }


    //Tests having 3 classes with the same instructor over two years
    @Test
    public void testMakeClass8() {
        this.admin.createClass("TestA", 2017, "Instructor1", 10);
        this.admin.createClass("TestB", 2018, "Instructor1", 10);
        this.admin.createClass("TestC", 2018, "Instructor1", 10);
        assertTrue(this.admin.classExists("TestA", 2017));
        assertTrue(this.admin.classExists("TestB", 2018));
        assertTrue(this.admin.classExists("TestC", 2018));
    }


    //Test for making 2 classes with identical name and year
    @Test
    public void testMakeClass9() {
        this.admin.createClass("Test", 2017, "InstructorA", 5);
        this.admin.createClass("Test", 2017, "InstructorB", 5);
        assertEquals("InstructorA", this.admin.getClassInstructor("Test", 2017));
        assertFalse("Failure - a second class with the exact name and year of an existing class was created", this.admin.getClassInstructor("Test", 2017) == "InstructorB");
    }


    //Tests for a change in class capacity when the change is higher than the number of students enrolled
    @Test
    public void testChangeCapacity() {
        this.admin.createClass("Test", 2017, "Instructor", 3);
        this.student1.registerForClass("Student1", "Test", 2017);
        this.student2.registerForClass("Student2", "Test", 2017);
        this.student3.registerForClass("Student3", "Test", 2017);
        this.admin.changeCapacity("Test", 2017, 5);
        assertTrue(this.admin.getClassCapacity("Test", 2017) >= 3);
    }


    //Tests for a change in class capacity when the change is lower than the number of students enrolled
    @Test
    public void testChangeCapacity2() {
        this.admin.createClass("Test", 2017, "Instructor", 3);
        this.student1.registerForClass("Student1", "Test", 2017);
        this.student2.registerForClass("Student2", "Test", 2017);
        this.student3.registerForClass("Student3", "Test", 2017);
        this.admin.changeCapacity("Test", 2017, 2);
        assertTrue(this.student1.isRegisteredFor("Student1", "Test", 2017));
        assertTrue(this.student2.isRegisteredFor("Student2", "Test", 2017));
        assertTrue(this.student3.isRegisteredFor("Student3", "Test", 2017));
        assertFalse("Failure - a class had its capacity changed to a value lower than the number of students enrolled", this.admin.getClassCapacity("Test", 2017) < 3);
    }


    //Test if class capacity can be lowered, but still higher than number of enrolled
    @Test
    public void testChangeCapacity3(){
        this.admin.createClass("Class", 2017, "Instructor", 5);
        this.student1.registerForClass("Student", "Class", 2017);
        this.student2.registerForClass("Student2", "Class", 2017);
        this.student3.registerForClass("Student3", "Class", 2017);
        this.admin.changeCapacity("Class", 2017, 4);
        assertTrue(this.admin.getClassCapacity("Class", 2017) >= 3);
    }


    //Test if class capacity can be lowered to the number of students enrolled
    @Test
    public void testChangeCapacity4(){
        this.admin.createClass("Class", 2017, "Instructor", 5);
        this.student1.registerForClass("Student", "Class", 2017);
        this.student2.registerForClass("Student2", "Class", 2017);
        this.student3.registerForClass("Student3", "Class", 2017);
        this.admin.changeCapacity("Class", 2017, 3);
        assertTrue(this.admin.getClassCapacity("Class", 2017) >= 3);
    }


    //Test if a class capacity can be changed to 0, no students enrolled
    @Test
    public void testChangeCapacity6(){
        this.admin.createClass("Class", 2017, "Instructor", 5);
        this.admin.changeCapacity("Class", 2017, 0);
        assertFalse("Failure - a class capacity has been changed to zero", this.admin.getClassCapacity("Class", 2017) == 0);
    }


    //Test if a class capacity can be changed to a negative number, no students enrolled
    @Test
    public void testChangeCapacity7(){
        this.admin.createClass("Class", 2017, "Instructor", 5);
        this.admin.changeCapacity("Class", 2017, -1);
        assertFalse("Failure - a class capacity has been changed to a negative number", this.admin.getClassCapacity("Class", 2017) < 0);
    }
}
