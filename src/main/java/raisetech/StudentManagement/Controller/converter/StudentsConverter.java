package raisetech.StudentManagement.Controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentsDetail;

@Component
public class StudentsConverter {

  public List<StudentsDetail> convertStudentDetails(List<Student> students,
      List<StudentsCourses> studentsCourses) {
    List<StudentsDetail> studentsDetails = new ArrayList<>();
    students.forEach(student -> {
      StudentsDetail studentsDetail = new StudentsDetail();
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
