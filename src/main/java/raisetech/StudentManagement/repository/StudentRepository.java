package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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
  @Select("SELECT * FROM students WHERE id = #{id}")
  Student searchStudent(String id);

  @Select("SELECT * FROM students WHERE isDeleted = false")
  List<Student> search();

  /**
   * 受講生のコース情報の全件検索
   *
   * @return 受講生のコース情報（全件）
   */
  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchStudentsCoursesList();

  /**
   * 受講生IDに基づく受講生コース情報を検索
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentCourse> searchStudentsCourses(String studentId);

  /**
   * 受講生の新規登録　IDは自動採番
   *
   * @param student 受講生
   */
  @Insert(
      "INSERT INTO students(name, kanaName, nickname, email, region, age, gender, remark,isDeleted) "
          + "VALUES(#{name}, #{kanaName}, #{nickname}, #{email}, #{region}, #{age}, #{gender}, #{remark},false)")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudent(Student student);

  /**
   * 受講生コース情報の新規登録　IDは自動採番
   *
   * @param studentsCourse 受講生コース情報
   */
  @Insert(
      "INSERT INTO students_courses(id, student_id, course_name, start_date, end_date) "
          + "VALUES(#{id}, #{studentId}, #{courseName}, #{startDate}, #{endDate}) ")
  void registerStudentsCourses(StudentCourse studentsCourse);

  /**
   * 受講生を更新
   *
   * @param student 受講生
   */
  @Update("UPDATE students SET name = #{name}, kanaName = #{kanaName}, nickname = #{nickname}, "
      + "email = #{email}, region = #{region}, age = #{age}, gender = #{gender}, remark = #{remark}, isDeleted = #{isDeleted}  WHERE id = #{id} ")
  void updateStudent(Student student);

  /**
   * 受講生コース情報のコース名を更新
   *
   * @param studentCourse 受講生コース情報
   */
  @Update("UPDATE students_courses SET course_name = #{courseName} WHERE id = #{id}")
  void updateStudentsCourse(StudentCourse studentCourse);


}
