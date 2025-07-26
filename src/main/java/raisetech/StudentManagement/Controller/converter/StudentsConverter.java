package raisetech.StudentManagement.Controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;

/**
 * nServiceから取得してオブジェクトをControllerにとって必要な形に変換するコンバーター
 */
@Component
public class StudentsConverter {

  /**
   * 受講生に紐づく受講生コース情報をマッピングする　受講生コース情報は受講生に対して複数存在するのでループを回して受講生詳細情報を組み立てる
   *
   * @param students        　受講生一覧
   * @param studentsCourses 　受講生コース情報のリスト
   * @return　受講生詳細情報のリスト
   */
  public List<StudentDetail> convertStudentDetails(List<Student> students,
      List<StudentsCourses> studentsCourses) {
    List<StudentDetail> studentsDetails = new ArrayList<>();
    students.forEach(student -> {
      StudentDetail studentsDetail = new StudentDetail();
      studentsDetail.setStudent(student);

      List<StudentsCourses> convertStudentCourses = studentsCourses.stream()
          .filter(studentCourse -> student.getId().equals(studentCourse.getStudentId()))
          .collect(Collectors.toList());

      studentsDetail.setStudentsCourses(convertStudentCourses);
      studentsDetails.add(studentsDetail);
    });
    return studentsDetails;
  }
}
