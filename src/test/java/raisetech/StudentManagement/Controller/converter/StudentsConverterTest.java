package raisetech.StudentManagement.Controller.converter;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

class StudentsConverterTest {

  private StudentsConverter sut;

  @BeforeEach
  void before() {
    sut = new StudentsConverter();
  }

  @Test
  void 受講生のリストと受講生コース情報のリストを渡して受講生詳細のリストにできること() {
    Student student = new Student("100", "テスト太郎", "テストタロウ", "タロウ", "taro@example.com",
        "テスト県", 20, "男性", "備考", false);

    StudentCourse studentCourse = new StudentCourse("100", "100", "Javaスタンダード",
        LocalDateTime.now(), LocalDateTime.now().plusYears(1));

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(studentCourse);

    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

    assertThat(actual.get(0).getStudent()).isEqualTo(student);
    assertThat(actual.get(0).getStudentsCourseList()).isEqualTo(studentCourseList);
  }

  @Test
  void 受講生のリストと受講生コース情報のリストを渡した際に紐づかない受講生コース情報は除外すること() {
    Student student = new Student("100", "テスト太郎", "テストタロウ", "タロウ", "taro@example.com",
        "テスト県", 20, "男性", "備考", false);

    StudentCourse studentCourse = new StudentCourse("100", "222", "Javaスタンダード",
        LocalDateTime.now(), LocalDateTime.now().plusYears(1));

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(studentCourse);

    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

    assertThat(actual.get(0).getStudent()).isEqualTo(student);
    assertThat(actual.get(0).getStudentsCourseList()).isEmpty();
  }

}
