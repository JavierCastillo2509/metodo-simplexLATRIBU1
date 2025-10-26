package com.simplex.model;

public class SimplexRequest {
    private double[] objectiveFunction;
    private double[][] constraints;
    private double[] rhsValues;
    private String[] inequalityTypes; // "<=", ">=", "="
    private boolean isMaximization;

    public SimplexRequest() {}

    public double[] getObjectiveFunction() {
        return objectiveFunction;
    }

    public void setObjectiveFunction(double[] objectiveFunction) {
        this.objectiveFunction = objectiveFunction;
    }

    public double[][] getConstraints() {
        return constraints;
    }

    public void setConstraints(double[][] constraints) {
        this.constraints = constraints;
    }

    public double[] getRhsValues() {
        return rhsValues;
    }

    public void setRhsValues(double[] rhsValues) {
        this.rhsValues = rhsValues;
    }

    public String[] getInequalityTypes() {
        return inequalityTypes;
    }

    public void setInequalityTypes(String[] inequalityTypes) {
        this.inequalityTypes = inequalityTypes;
    }

    public boolean isMaximization() {
        return isMaximization;
    }

    public void setMaximization(boolean maximization) {
        isMaximization = maximization;
    }
}