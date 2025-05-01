package raisetech.StudentManagement.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourseList;
import raisetech.StudentManagement.service.StudentService;

@RestController
public class StudentsController {

  private StudentService service;

  @Autowired
  public StudentsController(StudentService service) {
    this.service = service;
  }

  @GetMapping("/StudentList")
  public List<Student> getStudentList() {
    return service.searchStudentList();
  }

  @GetMapping("/StudentsCourseList")
  public List<StudentsCourseList> getStudentsCourseList() {
    return service.searchStudentsCourseList();
  }
}
