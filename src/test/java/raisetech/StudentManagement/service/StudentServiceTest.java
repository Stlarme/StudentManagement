package raisetech.StudentManagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement.Controller.converter.StudentsConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentsConverter converter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_全件検索が動作すること() {
    Student student = new Student();
    student.setId("test-001");
    student.setName("テスト太郎");

    List<Student> studentList = List.of(student);

    StudentCourse course = new StudentCourse();
    course.setId("c-001");
    course.setStudentId("Test-001");
    course.setCourseName("Javaスタンダード");

    List<StudentCourse> studentCourseList = List.of(course);

    StudentDetail studentDetail = new StudentDetail(student, List.of(course));
    List<StudentDetail> expectedDetailList = List.of(studentDetail);

    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentsCoursesList()).thenReturn(studentCourseList);
    when(converter.convertStudentDetails(studentList, studentCourseList)).thenReturn(
        expectedDetailList);

    List<StudentDetail> actual = sut.searchStudentList();

    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentsCoursesList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);

    assertEquals(expectedDetailList, actual);
  }


  @Test
  void 受講生詳細の検索_IDで検索が動作すること() {
    String id = "test-001";

    Student student = new Student();
    student.setId(id);
    student.setName("テスト太郎");
    student.setKanaName("テストタロウ");
    student.setNickname("タロウ");
    student.setEmail("taro@example.com");
    student.setRegion("東京");
    student.setAge(20);
    student.setGender("男性");
    student.setRemark("備考");
    student.setIsDeleted(false);

    List<StudentCourse> studentCourses = new ArrayList<>();

    StudentDetail expected = new StudentDetail(student, studentCourses);

    when(repository.searchStudent(id)).thenReturn(student);
    when(repository.searchStudentsCourses(id)).thenReturn(studentCourses);

    StudentDetail actual = sut.searchStudent(id);

    verify(repository, times(1)).searchStudent(id);
    verify(repository, times(1)).searchStudentsCourses(id);

    assertEquals(expected, actual);
  }


  @Test
  void 受講生詳細の登録_登録処理が正常に動作すること() {
    Student student = new Student();
    student.setId("test-001");
    student.setName("テスト太郎");

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId("c-001");
    studentCourse.setStudentId("test-001");
    studentCourse.setCourseName("Javaスタンダード");
    studentCourse.setCourseStartAt(LocalDateTime.now());
    studentCourse.setCourseEndAt(LocalDateTime.now().plusYears(1));

    List<StudentCourse> studentCourseList = List.of(studentCourse);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    sut.registerStudent(studentDetail);

    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(1)).registerStudentsCourses((StudentCourse) studentCourseList);
  }


  @Test
  void 受講生詳細の更新_受講生とコース情報の更新が正常に動作すること() {
    Student student = new Student();
    student.setId("test-001");

    StudentCourse course1 = new StudentCourse();
    course1.setId("c-001");
    course1.setStudentId("test-001");
    course1.setCourseName("Javaスタンダード");

    StudentCourse course2 = new StudentCourse();
    course2.setId("c-002");
    course2.setStudentId("test-001");
    course2.setCourseName("Javaベーシック");

    List<StudentCourse> courseList = List.of(course1, course2);
    StudentDetail studentDetail = new StudentDetail(student, courseList);

    sut.updateStudent(studentDetail);

    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(1)).updateStudentsCourse(course1);
    verify(repository, times(1)).updateStudentsCourse(course2);
  }


  @Test
  void 学生コース初期化処理が正しく動作すること() {
    Student student = new Student();
    student.setId("test-001");

    StudentCourse studentCourse = new StudentCourse();

    sut.initStudentsCourse(studentCourse, student);

    assertNotNull(studentCourse.getId());
    assertTrue(studentCourse.getId().startsWith("c-"));
    assertEquals("test-001", studentCourse.getStudentId());
    assertNotNull(studentCourse.getCourseStartAt());
    assertNotNull(studentCourse.getCourseEndAt());
    assertTrue(studentCourse.getCourseEndAt().isAfter(studentCourse.getCourseStartAt()));
  }

}
