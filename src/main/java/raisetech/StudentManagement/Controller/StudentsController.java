package raisetech.StudentManagement.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.Controller.converter.StudentsConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentsDetail;
import raisetech.StudentManagement.service.StudentService;

@RestController
public class StudentsController {

  private StudentService service;
  private StudentsConverter converter;

  @Autowired
  public StudentsController(StudentService service, StudentsConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/StudentList")
  public List<StudentsDetail> getStudentList() {
    List<Student> students = service.searchStudentList();
    List<StudentsCourses> studentsCourses = service.searchStudentsCourseList();

    return converter.convertStudentDetails(students, studentsCourses);
  }

  @GetMapping("/StudentsCourseList")
    public List<StudentsCourses> getStudentsCourseList () {
      return service.searchStudentsCourseList();
    }
  }
