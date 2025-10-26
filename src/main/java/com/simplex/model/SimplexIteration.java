package com.simplex.model;

public class SimplexIteration {
    private int iterationNumber;
    private double[][] tableau;
    private String[] basicVariables;
    private int pivotRow;
    private int pivotColumn;
    private String description;

    public SimplexIteration() {}

    public int getIterationNumber() {
        return iterationNumber;
    }

    public void setIterationNumber(int iterationNumber) {
        this.iterationNumber = iterationNumber;
    }

    public double[][] getTableau() {
        return tableau;
    }

    public void setTableau(double[][] tableau) {
        this.tableau = tableau;
    }

    public String[] getBasicVariables() {
        return basicVariables;
    }

    public void setBasicVariables(String[] basicVariables) {
        this.basicVariables = basicVariables;
    }

    public int getPivotRow() {
        return pivotRow;
    }

    public void setPivotRow(int pivotRow) {
        this.pivotRow = pivotRow;
    }

    public int getPivotColumn() {
        return pivotColumn;
    }

    public void setPivotColumn(int pivotColumn) {
        this.pivotColumn = pivotColumn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}