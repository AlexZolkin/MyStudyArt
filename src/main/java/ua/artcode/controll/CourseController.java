package ua.artcode.controll;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.artcode.exception.CourseDirectoryCreatingException;
import ua.artcode.exception.CourseNotFoundException;
import ua.artcode.exception.NoSuchDirectoryException;
import ua.artcode.model.CheckResult;
import ua.artcode.model.Course;
import ua.artcode.model.GeneralResponse;
import ua.artcode.model.SolutionModel;
import ua.artcode.service.CourseService;

import javax.validation.Valid;
import java.util.Collection;

/**
 * Created by Алексей on 17.04.2017.
 */
@RestController
public class CourseController {
    @Autowired
    private CourseService courseService;

    @RequestMapping("courses/get")
    public Course getCourse(@RequestParam int id) throws CourseNotFoundException{
        return courseService.getCourse(id);
    }

    @RequestMapping("courses/getAll")
    public Collection<Course> getAllCourses(){
        return courseService.getAllCourses();
    }

    @RequestMapping(path = {"/courses/add"}, method = RequestMethod.POST)
    public GeneralResponse addCourse(@RequestBody @Valid Course course)
            throws CourseDirectoryCreatingException, GitAPIException {

        return courseService.addCourseFromGit(course) ? new GeneralResponse("DONE") : new GeneralResponse("FAILED");
    }

    @RequestMapping(path = {"/run-task"})
    public CheckResult runTask(@RequestParam String packageName,
                               @RequestParam String mainClass,
                               @RequestParam String methodName,
                               @RequestParam int id)
    throws NoSuchDirectoryException, CourseNotFoundException, ClassNotFoundException{
        return courseService.runClass(packageName, mainClass, methodName, id);
    }

    @RequestMapping(value = {"/send-solution"}, method = RequestMethod.POST)
    public CheckResult sendSolution(@RequestBody SolutionModel solutionModel,
                                    @RequestParam String packageName,
                                    @RequestParam String mainClass,
                                    @RequestParam String methodName,
                                    @RequestParam int id)
    throws NoSuchDirectoryException, CourseNotFoundException, ClassNotFoundException{
        return courseService.sendSolution(packageName, mainClass, methodName, id, solutionModel);
    }
}
