package uz.urinov.school;
import lombok.*;

import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FullSchoolResponse {

    private String name;
    private String email;

    public List<Student> students;
}
