package ua.artcode.utils;

import ua.artcode.model.SolutionModel;

/**
 * Created by Алексей on 17.04.2017.
 */
public class StringUtils {
    public static String addSolution(String source, SolutionModel solution) {
        return source.substring(0, source.lastIndexOf("}"))
                + solution.getSolution() + "}";
    }
}
