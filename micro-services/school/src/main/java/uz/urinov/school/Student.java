package uz.urinov.school;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {

    private String firstName;
    private String lastName;
    private String email;

}
