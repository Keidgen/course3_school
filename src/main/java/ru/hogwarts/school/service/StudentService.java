package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import java.util.Collection;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        return studentRepository.findById(id).orElseThrow();
    }

    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(long id){
        studentRepository.deleteById(id);
    }

    public Collection<Student> findByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findByAgeBetween(int ageFrom, int ageTo) {
        return studentRepository.findByAgeBetween(ageFrom, ageTo);
    }

    public Collection<Student> findAll() {
        return studentRepository.findAll();
    }

    public Integer getCountStudents() {
        return studentRepository.getCountStudents();
    }

    public Double getAverageAgeStudents() {
        return studentRepository.getAverageAgeStudents();
    }

    public Collection<Student> getLastStudents(Integer count) {
        return studentRepository.getLastStudents(count);
    }
}
