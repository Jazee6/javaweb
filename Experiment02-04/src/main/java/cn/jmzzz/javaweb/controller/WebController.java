package cn.jmzzz.javaweb.controller;

import cn.jmzzz.javaweb.MyBatisUtils;
import cn.jmzzz.javaweb.interceptor.Login;
import cn.jmzzz.javaweb.interceptor.map;
import cn.jmzzz.javaweb.pojo.Course;
import cn.jmzzz.javaweb.pojo.School;
import cn.jmzzz.javaweb.pojo.User;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
public class WebController {
    static SqlSession session;

    static {
        session = MyBatisUtils.getSession();
    }

    @PostMapping("/courses")
    public String createCourses(@RequestBody Course course) {
        Object o = session.selectOne("cn.jmzzz.mapper.course.selectCourseByName", course.getName());
        if (o != null) {
            return course.getName() + " is already exist!";
        }
        session.insert("cn.jmzzz.mapper.course.insertCourse", course);
        session.commit();
        return "success";
    }

    @GetMapping("/courses")
    public List<Course> getCourses() {
        return session.selectList("cn.jmzzz.mapper.course.selectAllCourse");
    }

    @PutMapping("/courses")
    public String updateCourses(@RequestBody Course course) {
        Object o = session.selectOne("cn.jmzzz.mapper.course.selectCourseByNameAndEx", course);
        if (o != null) {
            return course.getName() + " is already exist!";
        }
        session.update("cn.jmzzz.mapper.course.updateAllCourse", course);
        session.commit();
        return "success";
    }

    @DeleteMapping("/courses/{id}")
    public String deleteCourses(@PathVariable int id) {
        session.delete("cn.jmzzz.mapper.course.deleteCourse", id);
        session.commit();
        return "success";
    }

    @GetMapping("/schools")
    public List<School> getSchools() {
        return session.selectList("cn.jmzzz.mapper.course.selectAllSchool");
    }


    @RequestMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile f1) throws IOException {
        String originalFilename = f1.getOriginalFilename();
        String destFilePath = String.format("D:\\File\\Code\\Java\\Experiment\\Experiment02\\src\\main\\webapp\\uploads\\%s", originalFilename);
        File destFile = new File(destFilePath);
        f1.transferTo(destFile);
        return "http://localhost:8080/uploads/" + originalFilename;
    }

    @PostMapping("/login")
    public String login(@RequestBody User user, HttpServletResponse response) {
        Object o = session.selectOne("cn.jmzzz.mapper.course.selectUser", user.getUsername());
        if (o == null) {
            return "user not exist!";
        }
        User user1 = (User) o;
        if (user1.getPassword().equals(user.getPassword())) {
            String token = UUID.randomUUID().toString();
            map.map.put(token, user.getUsername());
            response.addCookie(new Cookie("token", token));
            return "success";
        } else {
            return "password error!";
        }
    }
}
