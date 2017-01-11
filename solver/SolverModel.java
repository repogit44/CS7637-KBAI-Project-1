package solver;

import ravensproject.RavensFigure;
import ravensproject.RavensProblem;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SolverModel {
    private String name;
    private String problemType;
    private boolean hasVerbal = false;
    private boolean hasVisual = false;

    private Map<String,SolverFigure> solverFigures;

    public SolverModel(RavensProblem ravensProblem) {
        name = ravensProblem.getName();

        solverFigures = new HashMap<>();
        for (Map.Entry<String, RavensFigure> figureMap: ravensProblem.getFigures().entrySet()) {
            solverFigures.put(figureMap.getKey(), new SolverFigure(figureMap.getValue()));
        }

        hasVerbal = ravensProblem.hasVerbal();
        hasVisual = ravensProblem.hasVisual();
        problemType = ravensProblem.getProblemType();
    }

    public int Solve() {
        if(hasVerbal && problemType.equals("2x2")) {
            return SolveVerbal();
        } else {
            return -1;
        }
    }

    private int SolveVerbal() {

        SolverFigure figureA = solverFigures.get("A");
        SolverFigure figureB = solverFigures.get("B");
        SolverFigure figureC = solverFigures.get("C");

        System.out.println(name);

        Map<String, SolverFigure> answerFigures =
                solverFigures.entrySet().stream()
                .filter(s -> s.getKey().matches("^\\d+$"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        SolverModel2x2 solver = new SolverModel2x2(figureA, figureB, figureC, answerFigures);
        String result = solver.Solve();

        if(result != null) {
            return Integer.parseInt(result);
        }

        Map<String, SolverFigure> singleAnswer = new HashMap<>();
        singleAnswer.put("A", figureA);

        for(Map.Entry<String, SolverFigure> figure : solverFigures.entrySet()) {
            SolverModel2x2 reverseSolve = new SolverModel2x2(figure.getValue(), figureC, figureB, singleAnswer);
            result = reverseSolve.Solve();
            if(result != null) {
                return Integer.parseInt(result);
            }
        }

        return -1;
    }
}
