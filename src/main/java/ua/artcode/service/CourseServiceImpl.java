package ua.artcode.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.artcode.dao.CourseDB;
import ua.artcode.exception.CourseDirectoryCreatingException;
import ua.artcode.exception.CourseNotFoundException;
import ua.artcode.exception.NoSuchDirectoryException;
import ua.artcode.model.CheckResult;
import ua.artcode.model.Course;
import ua.artcode.model.GeneralResponse;
import ua.artcode.model.SolutionModel;
import ua.artcode.utils.CheckUtils;
import ua.artcode.utils.IOUtils;
import ua.artcode.utils.StringUtils;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by Алексей on 17.04.2017.
 */
@Service
public class CourseServiceImpl implements CourseService {

    private static final JavaCompiler COMPILER = ToolProvider.getSystemJavaCompiler();

    @Autowired
    private CourseDB courseDB;

    @Override
    public Course getCourse(int id) {
        Course course = null;
        try {
            course = courseDB.getCourseById(id);
        }catch (CourseNotFoundException e){
            e.printStackTrace();
        }
        return course;
    }

    @Override
    public Collection<Course> getAllCourses() {
        return courseDB.getAllCourses();
    }

    @Override
    public boolean addCourseFromGit(Course course) throws CourseDirectoryCreatingException, GitAPIException {
        try {
            File courseDir = IOUtils.createCourseDirectory(course);
            course.setCourseLocalPath(courseDir.getAbsolutePath());

            Git.cloneRepository()
                    .setURI(course.getGitUrl())
                    .setDirectory(courseDir)
                    .call();
            if(!IOUtils.checkDirectoryIsEmpty(courseDir))
                courseDB.add(course);
            else
                throw new CourseDirectoryCreatingException("Can't create a directory for course");
            return true;
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public CheckResult runClass(String packageName, String mainClass, String methodName, int id)
            throws NoSuchDirectoryException, ClassNotFoundException, CourseNotFoundException {
        String projectPath;
        CheckResult checkResult = new CheckResult(new GeneralResponse("FAILED"));
        try{
            projectPath = courseDB.getCoursePath(courseDB.getCourseById(id));
            String[] sourceJavaFilesPaths = IOUtils.getSourceJavaFilesPaths(projectPath);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            COMPILER.run(null, null, baos, sourceJavaFilesPaths);

            String compilationResult = new String(baos.toByteArray(), StandardCharsets.UTF_8);
            if(compilationResult.trim().length() > 0)
                return new CheckResult(new GeneralResponse(compilationResult));
            File rootDirectoryPath = IOUtils.getRootDirectory(projectPath, packageName);
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{rootDirectoryPath.toURI().toURL()});
            checkResult = CheckUtils.runCheckMethod(mainClass, classLoader, methodName);
        }catch (IOException e){
            e.printStackTrace();
        }
        return checkResult;
    }

    @Override
    public CheckResult sendSolution(String packageName, String className, String methodName, int id, SolutionModel solution) throws NoSuchDirectoryException, ClassNotFoundException, CourseNotFoundException {
        String projectPath;
        String sourceClassContentOriginal = null;
        Path sourceClassPath = null;
        CheckResult checkResult = new CheckResult(new GeneralResponse("FAILED"));
        try {
            projectPath = courseDB.getCoursePath(courseDB.getCourseById(id));
            // path for source class with tests (which we have to run)
            sourceClassPath = Files.walk(Paths.get(projectPath))
                    .filter(path -> path.toString().contains(className + ".java"))
                    .findFirst()
                    .orElseThrow(() -> new ClassNotFoundException("No class with name: " + className));

            // save original content of class
            sourceClassContentOriginal = Files.readAllLines(sourceClassPath)
                    .stream()
                    .collect(Collectors.joining());

            // append our solution at the end of sourceClassContentOriginal string
            String sourceClassContentWithSolution = StringUtils.addSolution(sourceClassContentOriginal, solution);

            // write sourceClassContentWithSolution to source class
            Files.write(sourceClassPath, sourceClassContentWithSolution.getBytes(), StandardOpenOption.CREATE);

            // run task
            checkResult = runClass(packageName, className, methodName, id);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sourceClassContentOriginal != null) {
                try (PrintWriter pw = new PrintWriter(new File(sourceClassPath.toString()))) {
                    // reset changes - to original state
                    IOUtils.writeToFile(pw, sourceClassPath, sourceClassContentOriginal);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return checkResult;
    }
}
