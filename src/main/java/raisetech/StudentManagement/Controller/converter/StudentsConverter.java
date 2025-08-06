package raisetech.StudentManagement.Controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

/**
 * Serviceから取得してオブジェクトをControllerにとって必要な形に変換するコンバーター
 */
@Component
public class StudentsConverter {

  /**
   * 受講生に紐づく受講生コース情報をマッピングする　受講生コース情報は受講生に対して複数存在するのでループを回して受講生詳細情報を組み立てる
   *
   * @param studentList       　受講生一覧
   * @param studentCourseList 　受講生コース情報のリスト
   * @return 受講生詳細情報のリスト
   */
  public List<StudentDetail> convertStudentDetails(List<Student> studentList,
      List<StudentCourse> studentCourseList) {
    List<StudentDetail> studentsDetails = new ArrayList<>();
    studentList.forEach(student -> {
      StudentDetail studentsDetail = new StudentDetail();
      studentsDetail.setStudent(student);

      List<StudentCourse> convertStudentCourseList = studentCourseList.stream()
          .filter(studentCourse -> student.getId().equals(studentCourse.getStudentId()))
          .collect(Collectors.toList());

      studentsDetail.setStudentsCourseList(convertStudentCourseList);
      studentsDetails.add(studentsDetail);
    });
    return studentsDetails;
  }
}
