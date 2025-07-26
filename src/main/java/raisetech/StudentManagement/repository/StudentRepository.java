package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;

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
  @Select("SELECT * FROM students WHERE isDeleted = false")
  List<Student> search();

  /**
   * 受講生の検索を行います
   *
   * @return 受講生
   */
  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchStudentsCourses();


  /**
   * 受講生のコース情報の全件検索
   *
   * @result 受講生のコース情報（全件）
   */
  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> findAllStudentsCourses();

  @Insert(
      "INSERT INTO students(name, kanaName, nickname, email, region, age, gender, remark,isDeleted) "
          + "VALUES(#{name}, #{kanaName}, #{nickname}, #{email}, #{region}, #{age}, #{gender}, #{remark},false)")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertStudent(Student student);

  @Insert(
      "INSERT INTO students_courses(id, student_id, course_name, start_date, end_date) "
          + "VALUES(#{id}, #{studentId}, #{courseName}, #{startDate}, #{endDate}) ")
  void insertStudentsCourse(StudentsCourses studentsCourse);

  @Select("SELECT * FROM students WHERE id = #{id}")
  Student findStudentById(String id);

  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentsCourses> findCoursesByStudentId(String studentId);

  @Update("UPDATE students SET name = #{name}, kanaName = #{kanaName}, nickname = #{nickname}, "
      + "email = #{email}, region = #{region}, age = #{age}, gender = #{gender}, remark = #{remark}, isDeleted = #{isDeleted}  WHERE id = #{id} ")
  void updateStudent(Student student);

  @Update("UPDATE students_courses SET course_name = #{courseName}, start_date = #{startDate}, end_date = #{endDate} WHERE id = #{id}")
  void updateStudentsCourse(StudentsCourses studentsCourse);

}
