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
 * Created by Albert on 3/1/2017.
 */
public class TestInstructor {
    private IInstructor instructor;
    private IAdmin admin;
    private IStudent student;

    @Before
    public void setup() {
        this.instructor = new Instructor();
        this.admin = new Admin();
        this.student = new Student();
    }


    //Test that an instructor is assigned to the class
    @Test
    public void testAddHomework() {
        this.admin.createClass("Test", 2017, "Instructor", 5);
        this.instructor.addHomework("Instructor", "Test", 2017, "Homework1", "TestHomework");
        assertTrue(this.instructor.homeworkExists("Test", 2017, "Homework1"));
    }


    //Test for an instructor assigning homework to a class he isn't assigned to
    @Test
    public void testAddHomework2() {
        this.admin.createClass("Test", 2017, "InstructorA", 5);
        this.instructor.addHomework("InstructorB", "Test", 2017, "Homework1", "TestHomework");
        assertFalse("Failure - an instructor not assigned to the class added homework", this.instructor.homeworkExists("Test", 2017, "Homework1"));
    }


    //Test for an instructor assigning a grade to a student in a class he is assigned to
    //Test for a homework assignment that has correctly been assigned
    //Test that the student has submitted the homework correctly
    @Test
    public void testAssignGrade() {
        this.admin.createClass("Test", 2017, "Instructor", 5);
        this.student.registerForClass("Student", "Test", 2017);
        this.instructor.addHomework("Instructor", "Test", 2017, "Homework1", "TestHomework");
        this.student.submitHomework("Student", "Homework1", "TestAnswer", "Test", 2017);
        this.instructor.assignGrade("Instructor", "Test", 2017, "Homework1", "Student", 75);
        assertNotNull(this.instructor.getGrade("Test", 2017, "Homework1", "Student"));
    }


    //Test for an instructor assigning a grade to a student for a class he is not assigned to
    @Test
    public void testAssignGrade2() {
        this.admin.createClass("Test", 2017, "InstructorA", 5);
        this.instructor.addHomework("InstructorA", "Test", 2017, "Homework1", "TestHomework");
        this.student.registerForClass("Student", "Test", 2017);
        this.student.submitHomework("Student", "Homework1", "TestAnswer", "Test", 2017);
        this.instructor.assignGrade("InstructorB", "Test", 2017, "Homework1", "Student", 75);
        assertNull("Failure - an instructor not assigned to the class gave a homework grade", this.instructor.getGrade("Test", 2017, "Homework1", "Student"));
    }


    //Test for a homework assignment that hasn't been assigned
    @Test
    public void testAssignGrade3() {
        this.admin.createClass("Test", 2017, "Instructor", 5);
        this.student.registerForClass("Student", "Test", 2017);
        this.student.submitHomework("Student", "Homework1", "TestAnswer", "Test", 2017);
        this.instructor.assignGrade("Instructor", "Test", 2017, "Homework1", "Student", 75);
        assertNull("Failure - a grade has been given for a homework that has not been assigned", this.instructor.getGrade("Test", 2017, "Homework1", "Student"));
    }


    //Test that the student has not submitted the homework
    @Test
    public void testAssignGrade4() {
        this.admin.createClass("Test", 2017, "Instructor", 5);
        this.instructor.addHomework("InstructorA", "Test", 2017, "Homework1", "TestHomework");
        this.student.registerForClass("Student", "Test", 2017);
        this.instructor.assignGrade("Instructor", "Test", 2017, "Homework1", "Student", 75);
        assertNull("Failure - a grade has been given for a homework that a student has not submitted yet", this.instructor.getGrade("Test", 2017, "Homework1", "Student"));
    }


    //Test that a valid homework grade is assigned (>= 0)
    @Test
    public void testAssignGrade5() {
        this.admin.createClass("Test", 2017, "Instructor", 5);
        this.instructor.addHomework("Instructor", "Test", 2017, "Homework1", "TestHomework");
        this.student.registerForClass("Student", "Test", 2017);
        this.student.submitHomework("Student", "Homework1", "TestAnswer", "Test", 2017);
        this.instructor.assignGrade("Instructor", "Test", 2017, "Homework1", "Student", 75);
        assertTrue(this.instructor.getGrade("Test", 2017, "Homework1", "Student") >= 0);
    }


    //Test that an invalid homework grade is assigned (<0)
    @Test
    public void testAssignGrade6() {
        this.admin.createClass("Test", 2017, "Instructor", 5);
        this.instructor.addHomework("Instructor", "Test", 2017, "Homework1", "TestHomework");
        this.student.registerForClass("Student", "Test", 2017);
        this.student.submitHomework("Student", "Homework1", "TestAnswer", "Test", 2017);
        this.instructor.assignGrade("Instructor", "Test", 2017, "Homework1", "Student", -5);
        assertFalse("Failure - a negative grade was given for a homework", this.instructor.getGrade("Test", 2017, "Homework1", "Student") < 0);
    }

    //Test that a valid homework grade is assigned (>100)
    @Test
    public void testAssignGrade7() {
        this.admin.createClass("Test", 2017, "Instructor", 5);
        this.instructor.addHomework("Instructor", "Test", 2017, "Homework1", "TestHomework");
        this.student.registerForClass("Student", "Test", 2017);
        this.student.submitHomework("Student", "Homework1", "TestAnswer", "Test", 2017);
        this.instructor.assignGrade("Instructor", "Test", 2017, "Homework1", "Student", 150);
        assertTrue(this.instructor.getGrade("Test", 2017, "Homework1", "Student") >= 100);
    }

    //Test for assigning a grade to a class that does not exist
    @Test
    public void testAssignGrade8() {
        this.student.registerForClass("Student", "Test", 2017);
        this.instructor.addHomework("Instructor", "Test", 2017, "Homework1", "TestHomework");
        this.student.submitHomework("Student", "Homework1", "TestAnswer", "Test", 2017);
        this.instructor.assignGrade("Instructor", "Test", 2017, "Homework1", "Student", 75);
        assertNull(this.instructor.getGrade("Test", 2017, "Homework1", "Student"));
    }

}

