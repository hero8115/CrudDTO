package com.example.cruddto.controller;

import com.example.cruddto.dto.StudentDTO;
import com.example.cruddto.entity.Address;
import com.example.cruddto.entity.Groups;
import com.example.cruddto.entity.Student;
import com.example.cruddto.repository.GroupRepository;
import com.example.cruddto.repository.StudentRepository;
import com.example.cruddto.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class StudentController {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UniversityRepository universityRepository;

    @RequestMapping(value = "/student{university_id}", method = RequestMethod.GET)
    public List<Student> studentList(@PathVariable Integer university_id){
        List<Student> studentList = new ArrayList<>();
        List<Student> all = studentRepository.findAll();
        for (Student s: all){
            if (s.getGroups().getFaculty().getUniversity().getId()==university_id){
                studentList.add(s);
            }
        }
        return studentList;
    }

    @RequestMapping(value = "/student/{groups_id}", method = RequestMethod.GET)
    public List<Student> studentListId(@PathVariable Integer groups_id){
        List<Student> byGroups_id = studentRepository.findByGroups_Id(groups_id);
        return byGroups_id;
    }

    @RequestMapping(value = "/student", method = RequestMethod.POST)
    public String addStudent(@RequestBody StudentDTO studentDTO){
        Student student = new Student();
        if (!studentRepository.existsByGroups_Id(studentDTO.getGroup_id())){
            student.setFirstName(studentDTO.getFirstName());
            student.setLastName(studentDTO.getLastName());
            student.setBirthday(studentDTO.getBirthday());
            Optional<Groups> byId = groupRepository.findById(studentDTO.getGroup_id());
            student.setGroups(byId.get());
            studentRepository.save(student);
            return "Student added";
        }
        else return "Group not found!!!";

    }

    @RequestMapping(value = "/student", method = RequestMethod.GET)
    public List<Student> studentListAll(){
        return studentRepository.findAll();
    }

}
