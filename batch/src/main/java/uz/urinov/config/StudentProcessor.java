package uz.urinov.config;

import org.springframework.batch.item.ItemProcessor;
import uz.urinov.student.Student;

public class StudentProcessor implements ItemProcessor<Student, Student> {

    @Override
    public Student process(Student student) throws Exception {
//        System.out.println("Processing student: " + student);
        return student;
    }
}
