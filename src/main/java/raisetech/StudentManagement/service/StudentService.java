package raisetech.StudentManagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;
import java.util.UUID;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    return repository.search();
  }

  public List<StudentsCourses> searchStudentsCourseList() {
    return repository.searchStudentsCourses();

  }

  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    repository.insertStudent(studentDetail.getStudent());

    if (studentDetail.getStudentsCourses() != null) {
      for (StudentsCourses course : studentDetail.getStudentsCourses()) {
        // UUIDを文字列化してIDにセット
        course.setId("c-" + UUID.randomUUID().toString().substring(0, 8)); // 例: c-1a2b3c4d
        course.setStudentId(studentDetail.getStudent().getId());
        repository.insertStudentsCourse(course);
      }
    }
  }
}
