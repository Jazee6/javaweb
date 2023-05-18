package cn.jmzzz.javaweb;

import cn.jmzzz.javaweb.pojo.Course;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class TestA {
    private static SqlSession sqlSession = null;

    @Before
    public void init() {
        String resource = "mybatis.xml";
        try {
            Reader reader = Resources.getResourceAsReader(resource);
            SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
            sqlSession = sessionFactory.openSession();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    //查询 id=2 的课程信息
    public void test() {
        Course course = sqlSession.selectOne("cn.jmzzz.mapper.course.selectCourseById", 2);
        System.out.println(course);
    }

    @Test
    // 查询出所有计算机学院开设的课程信息
    public void test2() {
        List<Course> courses = sqlSession.selectList("cn.jmzzz.mapper.course.selectCourseBySid", 1);
        for (Course course : courses) {
            System.out.println(course);
        }
    }

    @Test
    // 将 id=4 这⻔课程的课时数修改为 32+8=40
    public void test3() {
        Course course = new Course();
        course.setId(4);
        course.setHours(40);
        sqlSession.update("cn.jmzzz.mapper.course.updateCourse", course);
        sqlSession.commit();
    }

    @Test
    // 插⼊⼀条新的课程记录： names=”⼤数据存储“，hours=32，schools =1
    public void test4() {
        Course course = new Course();
        course.setName("⼤数据存储");
        course.setHours(32);
        course.setSchool_id(1);
        sqlSession.insert("cn.jmzzz.mapper.course.insertCourse", course);
        sqlSession.commit();
    }

    @Test
    // 输出所有的学院开设的课程信息
    public void test5() {
        List<Course> courses = sqlSession.selectList("cn.jmzzz.mapper.course.selectAllCourse");
        for (Course course : courses) {
            System.out.println(course);
        }
    }

    @After
    public void close() {
        sqlSession.commit();
        sqlSession.close();
    }
}
