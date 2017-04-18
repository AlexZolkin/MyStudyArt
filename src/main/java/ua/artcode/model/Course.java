package ua.artcode.model;

import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Created by Алексей on 17.04.2017.
 */
public class Course {
    private Integer id;

    @Pattern(regexp = "^https://github\\.com/\\w+/\\w+$", message = "Invalid URL")
    private String gitURL;
    private String courseLocalPath;
    @Pattern(regexp = "^\\w{2,}$", message = "Invalid author name")
    private String author;
    private List<Lesson> lessons;

    public Course(){

    }
    public Course(int id, String gitURL, String courseLocalPath, String author, List<Lesson> lessons){
        this.id = id;
        this.gitURL = gitURL;
        this.courseLocalPath = courseLocalPath;
        this.author = author;
        this.lessons = lessons;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String  getGitUrl() {
        return gitURL;
    }

    public void setGitURL(String gitURL) {
        this.gitURL = gitURL;
    }

    public String getCourseLocalPath() {
        return courseLocalPath;
    }

    public void setCourseLocalPath(String courseLocalPath) {
        this.courseLocalPath = courseLocalPath;
    }

    public String  getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(o == null || o.getClass() != this.getClass())
            return false;
        Course course = (Course) o;
        if(id != course.getId())
            return false;
        if(gitURL != null ? !gitURL.equals(course.getGitUrl()) : course.getGitUrl() != null)
            return false;
        if(courseLocalPath != null ? !courseLocalPath.equals(course.getCourseLocalPath()) :
                course.getCourseLocalPath() != null)
            return false;
        return author != null ? author.equals(course.getAuthor()) : course.getAuthor() == null;
    }

    @Override
    public int hashCode(){
        int result = id;
        result += 255 * result + (gitURL != null ? gitURL.hashCode() : 0);
        result += 255 * result + (courseLocalPath != null ? courseLocalPath.hashCode() : 0);
        result += 255 * result + (author != null ? author.hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        return "Course{" +
                "id=" + id +
                ", gitUrl=" + gitURL + "\\'" +
                ", courseLocalPath=" + courseLocalPath + "\\'" +
                ", author=" + author + "\\'" +
                ", lessons=" + lessons +
                "}";
    }

}
