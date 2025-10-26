package com.simplex.service;

import com.simplex.model.SimplexIteration;
import com.simplex.model.SimplexRequest;
import com.simplex.model.SimplexResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SimplexService {

    private static final double EPSILON = 1e-10;

    public SimplexResponse solve(SimplexRequest request) {
        SimplexResponse response = new SimplexResponse();
        List<SimplexIteration> iterations = new ArrayList<>();

        try {
            int numVars = request.getObjectiveFunction().length;
            int numConstraints = request.getConstraints().length;

            // Construir tableau inicial
            double[][] tableau = buildInitialTableau(request, numVars, numConstraints);
            String[] basicVars = new String[numConstraints];

            // Variables de holgura/exceso
            for (int i = 0; i < numConstraints; i++) {
                basicVars[i] = "s" + (i + 1);
            }

            // Guardar iteración inicial
            SimplexIteration initialIter = new SimplexIteration();
            initialIter.setIterationNumber(0);
            initialIter.setTableau(copyTableau(tableau));
            initialIter.setBasicVariables(basicVars.clone());
            initialIter.setDescription("Tableau inicial");
            iterations.add(initialIter);

            int iterCount = 0;
            int maxIterations = 100;

            while (iterCount < maxIterations) {
                // Encontrar columna pivote (variable entrante)
                int pivotCol = findPivotColumn(tableau);

                if (pivotCol == -1) {
                    // Solución óptima encontrada
                    break;
                }

                // Encontrar fila pivote (variable saliente)
                int pivotRow = findPivotRow(tableau, pivotCol);

                if (pivotRow == -1) {
                    response.setFeasible(false);
                    response.setUnbounded(true);
                    response.setMessage("El problema es no acotado");
                    response.setIterations(iterations);
                    return response;
                }

                // Actualizar variable básica
                basicVars[pivotRow] = "x" + (pivotCol + 1);

                // Realizar operación pivote
                performPivotOperation(tableau, pivotRow, pivotCol);

                // Guardar iteración
                iterCount++;
                SimplexIteration iter = new SimplexIteration();
                iter.setIterationNumber(iterCount);
                iter.setTableau(copyTableau(tableau));
                iter.setBasicVariables(basicVars.clone());
                iter.setPivotRow(pivotRow);
                iter.setPivotColumn(pivotCol);
                iter.setDescription("Iteración " + iterCount + ": x" + (pivotCol + 1) +
                        " entra, " + basicVars[pivotRow] + " sale");
                iterations.add(iter);
            }

            // Extraer solución
            double[] solution = extractSolution(tableau, basicVars, numVars);
            double optimalValue = -tableau[tableau.length - 1][tableau[0].length - 1];

            if (!request.isMaximization()) {
                optimalValue = -optimalValue;
            }

            response.setFeasible(true);
            response.setUnbounded(false);
            response.setOptimalValue(optimalValue);
            response.setSolution(solution);
            response.setIterations(iterations);
            response.setMessage("Solución óptima encontrada");

        } catch (Exception e) {
            response.setFeasible(false);
            response.setMessage("Error: " + e.getMessage());
        }

        return response;
    }

    private double[][] buildInitialTableau(SimplexRequest request, int numVars, int numConstraints) {
        int totalCols = numVars + numConstraints + 1; // vars + slack + RHS
        int totalRows = numConstraints + 1; // constraints + objective

        double[][] tableau = new double[totalRows][totalCols];

        // Agregar restricciones
        for (int i = 0; i < numConstraints; i++) {
            // Coeficientes de variables
            for (int j = 0; j < numVars; j++) {
                tableau[i][j] = request.getConstraints()[i][j];
            }
            // Variable de holgura
            tableau[i][numVars + i] = 1.0;
            // RHS
            tableau[i][totalCols - 1] = request.getRhsValues()[i];
        }

        // Función objetivo (última fila)
        for (int j = 0; j < numVars; j++) {
            double coef = request.getObjectiveFunction()[j];
            tableau[totalRows - 1][j] = request.isMaximization() ? -coef : coef;
        }

        return tableau;
    }

    private int findPivotColumn(double[][] tableau) {
        int lastRow = tableau.length - 1;
        int lastCol = tableau[0].length - 1;
        int pivotCol = -1;
        double minValue = 0;

        for (int j = 0; j < lastCol; j++) {
            if (tableau[lastRow][j] < minValue - EPSILON) {
                minValue = tableau[lastRow][j];
                pivotCol = j;
            }
        }

        return pivotCol;
    }

    private int findPivotRow(double[][] tableau, int pivotCol) {
        int lastCol = tableau[0].length - 1;
        int pivotRow = -1;
        double minRatio = Double.POSITIVE_INFINITY;

        for (int i = 0; i < tableau.length - 1; i++) {
            if (tableau[i][pivotCol] > EPSILON) {
                double ratio = tableau[i][lastCol] / tableau[i][pivotCol];
                if (ratio < minRatio) {
                    minRatio = ratio;
                    pivotRow = i;
                }
            }
        }

        return pivotRow;
    }

    private void performPivotOperation(double[][] tableau, int pivotRow, int pivotCol) {
        double pivotElement = tableau[pivotRow][pivotCol];

        // Dividir fila pivote por elemento pivote
        for (int j = 0; j < tableau[0].length; j++) {
            tableau[pivotRow][j] /= pivotElement;
        }

        // Eliminar columna pivote en otras filas
        for (int i = 0; i < tableau.length; i++) {
            if (i != pivotRow) {
                double factor = tableau[i][pivotCol];
                for (int j = 0; j < tableau[0].length; j++) {
                    tableau[i][j] -= factor * tableau[pivotRow][j];
                }
            }
        }
    }

    private double[] extractSolution(double[][] tableau, String[] basicVars, int numVars) {
        double[] solution = new double[numVars];
        int lastCol = tableau[0].length - 1;

        for (int i = 0; i < basicVars.length; i++) {
            if (basicVars[i].startsWith("x")) {
                int varIndex = Integer.parseInt(basicVars[i].substring(1)) - 1;
                if (varIndex < numVars) {
                    solution[varIndex] = tableau[i][lastCol];
                }
            }
        }

        return solution;
    }

    private double[][] copyTableau(double[][] tableau) {
        double[][] copy = new double[tableau.length][tableau[0].length];
        for (int i = 0; i < tableau.length; i++) {
            copy[i] = Arrays.copyOf(tableau[i], tableau[i].length);
        }
        return copy;
    }
}