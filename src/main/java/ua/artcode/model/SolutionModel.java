package ua.artcode.model;

/**
 * Created by Алексей on 17.04.2017.
 */
public class SolutionModel {
    private String solution;

    public SolutionModel() {
    }

    public SolutionModel(String solution) {
        this.solution = solution;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
}
