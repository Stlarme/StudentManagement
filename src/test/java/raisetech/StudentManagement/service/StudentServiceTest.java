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
    Student student = new Student(
        "100", "テスト太郎", "テストタロウ", "タロウ",
        "taro@example.com", "東京", 20, "男性", "備考", false
    );

    List<Student> studentList = List.of(student);

    StudentCourse course = new StudentCourse(
        "100", "100", "Javaスタンダード",
        null, null
    );

    List<StudentCourse> studentCourseList = List.of(course);

    StudentDetail studentDetail = new StudentDetail(student, List.of(course));
    List<StudentDetail> expectedDetailList = List.of(studentDetail);

    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentsCoursesList()).thenReturn(studentCourseList);
    when(converter.convertStudentDetails(studentList, studentCourseList))
        .thenReturn(expectedDetailList);

    List<StudentDetail> actual = sut.searchStudentList();

    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentsCoursesList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);

    assertEquals(expectedDetailList, actual);
  }

  @Test
  void 受講生詳細の検索_IDで検索が動作すること() {
    String id = "100";

    Student student = new Student(
        id,
        "テスト太郎", "テストタロウ", "タロウ",
        "taro@example.com", "東京", 20, "男性", "備考", false
    );

    List<StudentCourse> studentCourses = new ArrayList<>();

    StudentDetail expected = new StudentDetail(student, studentCourses);

    // int を String に変換して渡す必要がある場合
    when(repository.searchStudent(String.valueOf(id))).thenReturn(student);
    when(repository.searchStudentsCourses(String.valueOf(id))).thenReturn(studentCourses);

    StudentDetail actual = sut.searchStudent(String.valueOf(id));

    verify(repository, times(1)).searchStudent(String.valueOf(id));
    verify(repository, times(1)).searchStudentsCourses(String.valueOf(id));

    assertEquals(expected, actual);
  }


  @Test
  void 受講生詳細の登録_登録処理が正常に動作すること() {
    Student student = new Student(
        "100", "テスト太郎", "テストタロウ", "タロウ",
        "taro@example.com", "東京", 20, "男性", "備考", false
    );

    StudentCourse studentCourse = new StudentCourse(
        "100", "100", "Javaスタンダード",
        LocalDateTime.now(), LocalDateTime.now().plusYears(1)
    );

    List<StudentCourse> studentCourseList = List.of(studentCourse);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    sut.registerStudent(studentDetail);

    verify(repository, times(1)).registerStudent(student);
    for (StudentCourse course : studentCourseList) {
      verify(repository, times(1)).registerStudentsCourses(course);
    }

  }

  @Test
  void 受講生詳細の更新_受講生とコース情報の更新が正常に動作すること() {
    Student student = new Student(
        "100", "テスト太郎", "テストタロウ", "タロウ",
        "taro@example.com", "東京", 20, "男性", "備考", false
    );

    StudentCourse course1 = new StudentCourse(
        "100", "100", "Javaスタンダード", null, null
    );
    StudentCourse course2 = new StudentCourse(
        "100", "100", "Javaベーシック", null, null
    );

    List<StudentCourse> courseList = List.of(course1, course2);
    StudentDetail studentDetail = new StudentDetail(student, courseList);

    sut.updateStudent(studentDetail);

    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(1)).updateStudentsCourse(course1);
    verify(repository, times(1)).updateStudentsCourse(course2);
  }

  @Test
  void 学生コース初期化処理が正しく動作すること() {
    Student student = new Student("100", "テスト太郎", "テストタロウ", "タロウ",
        "taro@example.com", "東京", 20, "男性", "備考", false
    );

    StudentCourse studentCourse = new StudentCourse(
        "100",
        "100",
        "Javaスタンダード",
        LocalDateTime.now(),
        LocalDateTime.now().plusYears(1)
    );

    sut.initStudentsCourse(studentCourse, student);

    assertNotNull(studentCourse.getId());
    assertEquals("100", studentCourse.getStudentId());
    assertNotNull(studentCourse.getCourseStartAt());
    assertNotNull(studentCourse.getCourseEndAt());
    assertTrue(studentCourse.getCourseEndAt().isAfter(studentCourse.getCourseStartAt()));
  }


}