package uz.urinov.school;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.urinov.school.client.StudentClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private  final StudentClient client;

    public void save(School school) {
        schoolRepository.save(school);
    }

    public List<School> findAll() {
        return schoolRepository.findAll();
    }


    public FullSchoolResponse findSchoolWithStudents(Integer schoolId) {
        var school = schoolRepository.findById(schoolId)
                .orElse(
                        School.builder()
                                .name("NOT_FOUND")
                                .email("NOT_FOUND")
                                .build()
                );
        var students=client.findAllStudentBySchool(schoolId);

        return FullSchoolResponse.builder()
                .name(school.getName())
                .email(school.getEmail())
                .students(students)
                .build();
    }
}
