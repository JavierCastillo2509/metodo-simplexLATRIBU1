package com.simplex.model;

import java.util.List;

public class SimplexResponse {
    private boolean feasible;
    private boolean unbounded;
    private double optimalValue;
    private double[] solution;
    private List<SimplexIteration> iterations;
    private String message;

    public SimplexResponse() {}

    public boolean isFeasible() {
        return feasible;
    }

    public void setFeasible(boolean feasible) {
        this.feasible = feasible;
    }

    public boolean isUnbounded() {
        return unbounded;
    }

    public void setUnbounded(boolean unbounded) {
        this.unbounded = unbounded;
    }

    public double getOptimalValue() {
        return optimalValue;
    }

    public void setOptimalValue(double optimalValue) {
        this.optimalValue = optimalValue;
    }

    public double[] getSolution() {
        return solution;
    }

    public void setSolution(double[] solution) {
        this.solution = solution;
    }

    public List<SimplexIteration> getIterations() {
        return iterations;
    }

    public void setIterations(List<SimplexIteration> iterations) {
        this.iterations = iterations;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}