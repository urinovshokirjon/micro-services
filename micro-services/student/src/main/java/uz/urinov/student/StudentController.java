package uz.urinov.student;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createStudent(@RequestBody Student student) {
        studentService.save(student);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.findAll();
    }

    @GetMapping("/school/{school-id}")
    public ResponseEntity<List<Student>> findAllStudents(@PathVariable("school-id") Integer schoolId) {
        return ResponseEntity.ok(studentService.findAllStudentsBySchool(schoolId));
    }

}
