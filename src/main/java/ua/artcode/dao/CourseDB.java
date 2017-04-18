package ua.artcode.dao;

import ua.artcode.exception.CourseNotFoundException;
import ua.artcode.model.Course;

import java.util.Collection;

/**
 * Created by Алексей on 17.04.2017.
 */
public interface CourseDB {

    boolean add(Course course);

    boolean remove(Course course);

    Course getCourseById(int id) throws CourseNotFoundException;

    String getCoursePath(Course course) throws CourseNotFoundException;

    Collection<Course> getAllCourses();

}
