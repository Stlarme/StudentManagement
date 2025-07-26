package raisetech.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

/**
 * 受講生情報を取り扱うService 受講生の検索や登録、更新処理を行う
 */
@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  /**
   * 受講生の一覧検索 全件検索 ＠return　受講生一覧（全件）
   */
  public List<Student> searchStudentList() {
    return repository.search();
  }

  /**
   * 受講生検索 IDに紐づく受講生情報を取得した後、その受講生に紐づくコース情報を取得
   *
   * @param studentId 　受講生ID
   * @return 受講生情報
   */
  public StudentDetail getStudentDetailById(String studentId) {
    Student student = repository.findStudentById(studentId);
    if (student == null) {
      return null;
    }
    List<StudentsCourses> courses = repository.findCoursesByStudentId(studentId);
    StudentDetail detail = new StudentDetail();
    detail.setStudent(student);
    detail.setStudentsCourses(courses);
    return detail;
  }

  public List<StudentsCourses> searchStudentsCourseList() {
    return repository.searchStudentsCourses();
  }

  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    repository.insertStudent(studentDetail.getStudent());
    List<StudentsCourses> courses = studentDetail.getStudentsCourses();
    if (courses == null) {
      courses = List.of();
    }
    for (StudentsCourses course : courses) {
      course.setId("c-" + UUID.randomUUID().toString().substring(0, 8));
      course.setStudentId(studentDetail.getStudent().getId());
      course.setCourseStartAt(LocalDateTime.now());
      course.setCourseEndAt(LocalDateTime.now().plusYears(1));
      repository.insertStudentsCourse(course);
    }
    return studentDetail;
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    if (studentDetail.getStudentsCourses() != null) {
      for (StudentsCourses course : studentDetail.getStudentsCourses()) {
        repository.updateStudentsCourse(course);
      }
    }
  }

}
