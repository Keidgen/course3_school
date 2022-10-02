package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import java.util.Collection;

@Service
public class StudentService {

    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        logger.debug("We added student {}", student);
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        logger.info("Was invoked method for find student");
        return studentRepository.findById(id).orElseThrow();
    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit student");
        return studentRepository.save(student);
    }

    public void deleteStudent(long id){
        logger.info("Was invoked method for delete student");
        logger.debug("We deleting student with id = {}", id);
        studentRepository.deleteById(id);
    }

    public Collection<Student> findByAge(int age) {
        logger.info("Was invoked method for find student by age");
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findByAgeBetween(int ageFrom, int ageTo) {
        logger.info("Was invoked method for find students ages between {} and {}", ageFrom, ageTo);
        return studentRepository.findByAgeBetween(ageFrom, ageTo);
    }

    public Collection<Student> findAll() {
        logger.info("Was invoked method for find all students");
        return studentRepository.findAll();
    }

    public Integer getCountStudents() {
        logger.info("Was invoked method for get count students");
        return studentRepository.getCountStudents();
    }

    public Double getAverageAgeStudents() {
        logger.info("Was invoked method for get average age students");
        return studentRepository.getAverageAgeStudents();
    }

    public Collection<Student> getLastStudents(Integer count) {
        logger.info("Was invoked method for get {} last students", count);
        return studentRepository.getLastStudents(count);
    }
}
