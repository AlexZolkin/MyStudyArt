package ua.artcode.dao;

import org.springframework.stereotype.Component;
import ua.artcode.exception.CourseNotFoundException;
import ua.artcode.model.Course;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Алексей on 17.04.2017.
 */
@Component
public class CourseDBImpl implements CourseDB{

    private Map<Integer, Course> courses;

    public CourseDBImpl(){
        courses = new ConcurrentHashMap<>();
    }

    @Override
    public boolean add(Course course) {
        if(course.getId() == 0)
            course.setId(courses.size() + 1);
        if(courses.values().contains(course))
            return false;
        courses.put(course.getId(), course);
        return true;
    }

    @Override
    public boolean remove(Course course) {
        return courses.remove(course.getId()) != null;
    }

    @Override
    public Course getCourseById(int id) throws CourseNotFoundException {
        if(id <= 0)
            throw new CourseNotFoundException("No course found with such id: " + id);
        return courses.values()
                .stream()
                .filter(course -> course.getId() == id)
                .findFirst()
                .orElseThrow(() -> new CourseNotFoundException("No course found with such id: " + id));
    }

    @Override
    public String getCoursePath(Course course) throws CourseNotFoundException {
        return courses.values()
                .stream()
                .filter(c -> c.equals(course))
                .map(Course::getCourseLocalPath)
                .findFirst()
                .orElseThrow(() -> new CourseNotFoundException("No such course available"));
    }

    @Override
    public Collection<Course> getAllCourses() {
        return courses.values();
    }
}
