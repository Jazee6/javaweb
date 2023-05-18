package cn.jmzzz.javaweb.pojo;

import lombok.Data;

@Data
public class Course {
    private int id;
    private String name;
    private int hours;
    private int school_id;
    private String img;
}
