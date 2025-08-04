package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

/**
 * 受講生テーブルと受講生コース情報テーブルと紐づくリポジトリ
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索
   *
   * @return 受講生一覧（全件）
   */
  List<Student> search();

  /**
   * 受講生の検索
   *
   * @param id 受講生ID
   * @return 受講生
   */
  Student searchStudent(String id);

  /**
   * 受講生コース情報の全件検索
   *
   * @return 受講生のコース情報（全件）
   */
  List<StudentCourse> searchStudentsCoursesList();

  /**
   * 受講生IDに基づく受講生コース情報を検索
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  List<StudentCourse> searchStudentsCourses(String studentId);

  /**
   * 受講生の新規登録　IDは自動採番
   *
   * @param student 受講生
   */
  void registerStudent(Student student);

  /**
   * 受講生コース情報の新規登録　IDは自動採番
   *
   * @param studentsCourse 受講生コース情報
   */
  void registerStudentsCourses(StudentCourse studentsCourse);

  /**
   * 受講生を更新
   *
   * @param student 受講生
   */
  void updateStudent(Student student);

  /**
   * 受講生コース情報のコース名を更新
   *
   * @param studentCourse 受講生コース情報
   */
  void updateStudentsCourse(StudentCourse studentCourse);

}
