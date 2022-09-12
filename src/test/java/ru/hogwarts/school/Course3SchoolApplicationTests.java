package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;

import java.net.URI;
import java.util.Collection;
import java.util.Set;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Course3SchoolApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateStudent() throws Exception {
        Student student = givenStudentWith("studentTest1", 30);
        ResponseEntity<Student> response = whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student);
        thenStudentHasBeenCreated(response);
    }

    private ResponseEntity<Student> whenSendingCreateStudentRequest(URI uri, Student student) {
        return restTemplate.postForEntity(uri, student, Student.class);
    }

    private UriComponentsBuilder getUriBuilder() {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path("/student");
    }

    private void thenStudentHasBeenCreated(ResponseEntity<Student> response) {
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isNotNull();
    }

    @Test
    public void testGetStudentById() throws Exception{
        Student student = givenStudentWith("studentTest1", 25);
        ResponseEntity<Student> createResponse = whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student);
        thenStudentHasBeenCreated(createResponse);

        Student createdStudent = createResponse.getBody();
        thenStudentWithIdHasBeenFound(createdStudent.getId(), createdStudent);
    }

    private void thenStudentWithIdHasBeenFound(Long studentId, Student student) {
        URI uri = getUriBuilder().path("/{id}").buildAndExpand(studentId).toUri();
        ResponseEntity<Student> response = restTemplate.getForEntity(uri, Student.class);

        Assertions.assertThat(response.getBody()).isEqualTo(student);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testFindByAge() {
        Student student_18 = givenStudentWith("studentTest2", 18);
        Student student_25 = givenStudentWith("studentTest3", 25);
        Student student_28 = givenStudentWith("studentTest4", 28);
        Student student_32 = givenStudentWith("studentTest5", 32);

        whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student_18);
        whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student_25);
        whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student_28);
        whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student_32);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("ageFrom", "25");
        thenStudentsAreFoundByCriteria(queryParams, student_25);
    }

    @Test
    public void testFindByAgeBetween() {
        Student student_35 = givenStudentWith("studentTest5", 35);
        Student student_40 = givenStudentWith("studentTest6", 40);
        Student student_45 = givenStudentWith("studentTest7", 45);

        whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student_35);
        whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student_40);
        whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student_45);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("ageFrom", "25");
        queryParams.add("ageTo", "40");
        thenStudentsAreFoundByCriteria(queryParams, student_35, student_40);
    }

    private void thenStudentsAreFoundByCriteria(MultiValueMap<String, String> queryParams, Student... students) {
        URI uri = getUriBuilder().queryParams(queryParams).build().toUri();

        ResponseEntity<Set<Student>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Set<Student>>() {}
        );

        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Set<Student> actualResult = response.getBody();
        resetIds(actualResult);
        Assertions.assertThat(actualResult).containsExactlyInAnyOrder(students);
    }

    @Test
    public void testUpdate() {
        Student student = givenStudentWith("studentTest8", 28);

        ResponseEntity<Student> responseEntity = whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student);
        thenStudentHasBeenCreated(responseEntity);
        Student createdStudent = responseEntity.getBody();

        whenUpdatingStudent(createdStudent, 32, "newName");
        thenStudentHasBeenUpdated(createdStudent, 32, "newName");
    }

    private void whenUpdatingStudent(Student createdStudent, int newAge, String newName) {
        createdStudent.setAge(newAge);
        createdStudent.setName(newName);

        restTemplate.put(getUriBuilder().build().toUri(), createdStudent);
    }

    private void thenStudentHasBeenUpdated(Student createdStudent, int newAge, String newName) {
        URI uri = getUriBuilder().cloneBuilder().path("/{id}").buildAndExpand(createdStudent.getId()).toUri();
        ResponseEntity<Student> updatedStudentRs = restTemplate.getForEntity(uri, Student.class);

        Assertions.assertThat(updatedStudentRs.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(updatedStudentRs.getBody()).isNotNull();
        Assertions.assertThat(updatedStudentRs.getBody().getAge()).isEqualTo(newAge);
        Assertions.assertThat(updatedStudentRs.getBody().getName()).isEqualTo(newName);
    }

    @Test
    public void testDelete() {
        Student student = givenStudentWith("studentTest9", 19);

        ResponseEntity<Student> responseEntity = whenSendingCreateStudentRequest(getUriBuilder().build().toUri(), student);
        thenStudentHasBeenCreated(responseEntity);
        Student createdStudent = responseEntity.getBody();

        whenDeletingStudent(createdStudent);
        thenStudentNotFound(createdStudent);
     }

    private void whenDeletingStudent(Student createdStudent) {
        restTemplate.delete(getUriBuilder().path("/{id}").buildAndExpand(createdStudent.getId()).toUri());
    }

    private void thenStudentNotFound(Student createdStudent) {
        URI getUri = getUriBuilder().path("/{id}").buildAndExpand(createdStudent.getId()).toUri();
        ResponseEntity<Student> emptyRs = restTemplate.getForEntity(getUri, Student.class);

        Assertions.assertThat(emptyRs.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void resetIds(Collection<Student> students) {
        students.forEach(it -> it.setId(null));
    }

    public Student givenStudentWith(String name, int age) {
        return new Student(name, age);
    }


    @Test
    void contextLoads() {
    }

}
