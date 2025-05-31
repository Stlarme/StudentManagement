package raisetech.StudentManagement.Controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;

@Component
public class StudentsConverter {

  public List<StudentDetail> convertStudentDetails(List<Student> students,
      List<StudentsCourses> studentsCourses) {
    List<StudentDetail> studentsDetails = new ArrayList<>();
    students.forEach(student -> {
      StudentDetail studentsDetail = new StudentDetail();
      studentsDetail.setStudent(student);

      List<StudentsCourses> convertStudentCourses = studentsCourses.stream()
          .filter(studentCourse -> student.getId().equals(studentCourse.getStudentId()))
          .collect(Collectors.toList());

      studentsDetail.setStudentsCourses(convertStudentCourses);
      studentsDetails.add(studentsDetail);
    });
    return studentsDetails;
  }
}
