package ua.artcode.service;

import org.eclipse.jgit.api.errors.GitAPIException;
import ua.artcode.exception.CourseDirectoryCreatingException;
import ua.artcode.exception.CourseNotFoundException;
import ua.artcode.exception.NoSuchDirectoryException;
import ua.artcode.model.CheckResult;
import ua.artcode.model.Course;
import ua.artcode.model.SolutionModel;

import java.util.Collection;

/**
 * Created by Алексей on 17.04.2017.
 */
public interface CourseService {
    Course getCourse(int id);

    Collection<Course> getAllCourses();

    boolean addCourseFromGit(Course course) throws CourseDirectoryCreatingException, GitAPIException;

    CheckResult runClass(String packageName, String mainClass, String methodName, int id)
            throws NoSuchDirectoryException, ClassNotFoundException, CourseNotFoundException;

    CheckResult sendSolution(String packageName, String mainClass, String methodName, int id, SolutionModel solutionModel)
            throws NoSuchDirectoryException, ClassNotFoundException, CourseNotFoundException;
}
