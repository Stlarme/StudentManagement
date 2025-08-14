package raisetech.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.Controller.converter.StudentsConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

/**
 * 受講生情報を取り扱うService 受講生の検索や登録、更新処理を行う
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentsConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentsConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生の一覧検索 全件検索 ＠return　受講生一覧（全件）
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
    List<StudentCourse> studentCourseList = repository.searchStudentsCoursesList();
    return converter.convertStudentDetails(studentList, studentCourseList);
  }

  /**
   * 受講生検索 IDに紐づく受講生情報を取得した後、その受講生に紐づくコース情報を取得
   *
   * @param id 　受講生ID
   * @return 受講生情報
   */
  public StudentDetail searchStudent(String id) {
    Student student = repository.searchStudent(id);
    List<StudentCourse> studentCourse = repository.searchStudentsCourses(id);
    return new StudentDetail(student, studentCourse);
  }

  /**
   * 受講生詳細の登録
   *
   * @param studentDetail
   * @return
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();

    repository.registerStudent(student);
    studentDetail.getStudentsCourseList().forEach(studentsCourses -> {
      initStudentsCourse(studentsCourses, student);
      repository.registerStudentsCourses(studentsCourses);
    });
    return studentDetail;
  }

  /**
   * 登録時の初期情報を設定する
   *
   * @param studentCourse 　受講生コース情報を
   * @param student       　受講生情報
   */
  void initStudentsCourse(StudentCourse studentCourse, Student student) {
    LocalDateTime now = LocalDateTime.now();

    studentCourse.setId("c-" + UUID.randomUUID().toString().substring(0, 8));
    studentCourse.setStudentId(student.getId());
    studentCourse.setCourseStartAt(now);
    studentCourse.setCourseEndAt(now.plusYears(1));
  }

  /**
   * 受講生詳細の更新を行う 受講開と受講コース情報をそれぞれ更新
   *
   * @param studentDetail 受講生詳細
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());

    List<StudentCourse> courseList = studentDetail.getStudentsCourseList();
    if (courseList != null) {
      courseList.forEach(studentCourse -> repository.updateStudentsCourse(studentCourse));
    }
  }
}
