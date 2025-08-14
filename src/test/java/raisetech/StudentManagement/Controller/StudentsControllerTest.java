package raisetech.StudentManagement.Controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.service.StudentService;

@WebMvcTest(StudentsController.class)
class studentsControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private StudentService service;

  private Validator validator;

  @BeforeEach
  void setUp() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること() throws Exception {
    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 受講生検索で該当IDの受講生情報が取得できること() throws Exception {
    String id = "1000";
    mockMvc.perform(get("/Student/{id}", id))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudent(id);
  }

  @Test
  void 受講生検索でIDに数値以外を渡した場合に入力チェックエラーとなること() throws Exception {
    String invalidId = "test-001";
    mockMvc.perform(get("/student/{id}", invalidId))
        .andExpect(status().isBadRequest());

    verify(service, times(0)).searchStudent(anyString());
  }

  @Test
  void 受講生登録が実行できて空で返ってくること() throws Exception {
    mockMvc.perform(post("/registerStudent").contentType(MediaType.APPLICATION_JSON).content(
            """
                {
                        "student" : {
                            "id" : "1",
                            "name" : "山田　太郎",
                            "kanaName" : "ヤマダ　タロウ",
                            "nickname" : "やま",
                            "email" : "taro.yamada@example.com",
                            "region" : "神奈川",
                            "age" : 21,
                            "gender" : "男性",
                            "remark" : "",
                            "isDeleted" : false
                    },
                    "studentCourseList":[
                        {
                            "courseName" : "Javaスタンダード"
                        }
                    ]
                 }
                """
        ))
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudent(any());
  }

  @Test
  void 受講生更新が成功すること() throws Exception {
    mockMvc.perform(put("/updateStudent").contentType(MediaType.APPLICATION_JSON).content(
            """
                {
                        "student" : {
                            "id" : "1",
                            "name" : "山田　太郎",
                            "kanaName" : "ヤマダ　タロウ",
                            "nickname" : "やま",
                            "email" : "taro.yamada@example.com",
                            "region" : "神奈川",
                            "age" : 21,
                            "gender" : "男性",
                            "remark" : "",
                            "isDeleted" : false
                    },
                    "studentCourseList":[
                        {
                            "id" : "c-1"
                            "studentId" : "1"
                            "courseName" : "Javaスタンダード"
                            "courseStartAt" : "2025-01-01"
                            "courseEndAt" : "2026-01-01"
                        }
                    ]
                 }
                """
        ))
        .andExpect(status().isOk());

    verify(service, times(1)).updateStudent(any());
  }

  @Test
  void 受講生詳細の例外APIが実行できてステータスが400で返ってくること() throws Exception {
    mockMvc.perform(get("/test"))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string("例外が発生しました。"));

  }

  @Test
  void 受講生詳細の受講生で適切な値を入力したときに入力チェックで異常が発生しないこと() {
    Student student = new Student("100", "テスト太郎", "テストタロウ", "タロウ", "taro@example.com",
        "東京", 20, "男性", "備考", false);

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations).isEmpty();
  }

  @Test
  void 受講生詳細の受講生IDに数字以外を用いた際に入力チェックがかかること() {
    Student student = new Student("test", "テスト太郎", "テストタロウ", "タロウ",
        "taro@example.com",
        "東京", 20, "男性", "備考", false);

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations).hasSize(1);
    assertThat(violations).extracting("message")
        .containsOnly("数字のみ入力するようにしてください。");
  }

  @Test
  void 受講生詳細の氏名が空白の際に入力チェックがかかること() {
    Student student = new Student("100", "", "テストタロウ", "タロウ", "taro@example.com",
        "東京", 20, "男性", "備考", false);

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations).hasSize(1);
  }

  @Test
  void 受講生詳細のカナ氏名が空白の際に入力チェックがかかること() {
    Student student = new Student("100", "テスト太郎", "", "タロウ", "taro@example.com",
        "東京", 20, "男性", "備考", false);

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations).hasSize(1);
  }

  @Test
  void 受講生詳細のニックネームが空白の際に入力チェックがかかること() {
    Student student = new Student("100", "テスト太郎", "テストタロウ", "", "taro@example.com",
        "東京", 20, "男性", "備考", false);

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations).hasSize(1);
  }

  @Test
  void 受講生詳細のメールアドレスの形式が不正な際に入力チェックがかかること() {
    Student student = new Student("100", "テスト太郎", "テストタロウ", "タロウ", "test-example",
        "東京", 20, "男性", "備考", false);

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations).hasSize(1);
  }

  @Test
  void 受講生詳細の地域が空白の際に入力チェックがかかること() {
    Student student = new Student("100", "テスト太郎", "テストタロウ", "タロウ", "taro@example.com",
        "", 20, "男性", "備考", false);

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations).hasSize(1);
  }

  @Test
  void 受講生詳細の性別が空白の際に入力チェックがかかること() {
    Student student = new Student("100", "テスト太郎", "テストタロウ", "タロウ", "taro@example.com",
        "東京", 20, "", "備考", false);

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations).hasSize(1);
  }

}