package uz.urinov.student;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
@Transactional
public interface StudentRepository extends JpaRepository<Student, Integer> {
}
