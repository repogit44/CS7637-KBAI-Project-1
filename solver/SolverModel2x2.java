package solver;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SolverModel2x2 {
    private Map<String, SolverFigure> answerFigures;

    private SolverBlock[][][] solverModel;
    private int dimA;
    private int dimB;
    private int dimC;

    public SolverModel2x2(SolverFigure figureA,
                          SolverFigure figureB,
                          SolverFigure figureC,
                          Map<String, SolverFigure> answerFigures) {

        this.answerFigures = answerFigures;

        List<ISolverObject> nullObjectList = Arrays.asList(new NullSolverObject());

        List<ISolverObject> objectsA = Stream.concat(nullObjectList.stream(), figureA.getSolverObjects().values().stream()).collect(Collectors.toList());
        List<ISolverObject> objectsB = Stream.concat(nullObjectList.stream(), figureB.getSolverObjects().values().stream()).collect(Collectors.toList());
        List<ISolverObject> objectsC = Stream.concat(nullObjectList.stream(), figureC.getSolverObjects().values().stream()).collect(Collectors.toList());

        dimA = objectsA.size();
        dimB = objectsB.size();
        dimC = objectsC.size();

        solverModel = new SolverBlock[dimA][dimB][dimC];

        System.out.println(String.format("SolverBlock[%d][%d][%d]", dimA, dimB, dimC));

        int indexA = 0;
        for(ISolverObject objA : objectsA) {
            int indexB = 0;
            for(ISolverObject objB : objectsB) {
                int indexC = 0;
                for(ISolverObject objC : objectsC) {
                    if(!(indexA == 0 && indexB == 0 && indexC == 0)) {
                        SolverBlock solverBlock = new SolverBlock(objA, objB, objC, indexA, indexB, indexC, dimA, dimB, dimC);
                        solverModel[indexA][indexB][indexC] = solverBlock;
                    }
                    indexC += 1;
                }
                indexB += 1;
            }
            indexA += 1;
        }
    }

    public String Solve() {

        List<SolverBlock> solverBlocks = new ArrayList<>();

        // Flatten and sort
        for(int c = 0; c < dimC; c++) {
            for(int a = 0; a < dimA; a++) {
                for(int b = 0; b < dimB; b++) {
                    SolverBlock solverBlock = solverModel[a][b][c];
                    if(solverBlock != null && (a != 0 || b != 0 || c != 0)) {
                        solverBlocks.add(solverBlock);
                    }
                }
            }
        }

        Collections.sort(solverBlocks);

        // Build list of best matches

        List<SolverBlock> finalBlocks = new ArrayList<>();

        for(SolverBlock solverBlock : solverBlocks) {
            if(finalBlocks.size() == 0) {
                finalBlocks.add(solverBlock);
            } else {
                boolean foundMatch = false;
                for(SolverBlock finalBlock : finalBlocks) {
                    if((solverBlock.getIndexA() == finalBlock.getIndexA() && solverBlock.getIndexA() != 0) ||
                       (solverBlock.getIndexB() == finalBlock.getIndexB() && solverBlock.getIndexB() != 0) ||
                       (solverBlock.getIndexC() == finalBlock.getIndexC() && solverBlock.getIndexC() != 0)) {
                        foundMatch = true;
                        break;
                    }
                }
                if(!foundMatch) {
                    finalBlocks.add(solverBlock);
                }
            }
        }

        List<ISolverObject> finalObjects =
                finalBlocks.stream()
                .flatMap(x -> x.getGenerated().stream())
                .filter(b -> !(b instanceof NullSolverObject))
                .collect(Collectors.toList());

        // Test against answer set

        int finalObjectsSize = finalObjects.size();

        for(HashMap.Entry<String, SolverFigure> answerFigureMap : answerFigures.entrySet()) {
            if(answerFigureMap.getValue().getSolverObjects().size() == finalObjectsSize) {
                boolean allMatch = finalObjects.stream().allMatch(f -> DoesSolverObjectMatchAnswerFigureObject(f, answerFigureMap.getValue()));
                if (allMatch) {
                    return answerFigureMap.getKey();
                }
            }
        }

        return null;
    }

    public void PrintObjects(List<ISolverObject> solverObjects) {
        for(ISolverObject solverObject : solverObjects) {
            if(solverObject != null) {
                HashMap<String, String> attributes = solverObject.getAttributes();
                if(attributes != null) {
                    System.out.println("\t" + attributes.toString());
                }
            }
        }
    }

    public boolean DoesSolverObjectMatchAnswerFigureObject(ISolverObject solverObject, SolverFigure answerFigure) {

        for(ISolverObject answerObject : answerFigure.getSolverObjects().values()) {
            ArrayList<ISolverTransformation> solverTransformations = ObjectToObject.CalculateTransformations(solverObject, answerObject);
            int score = ObjectToObject.CalculateScore(solverTransformations, false, false);

            if(score == 0) {
                return true;
            }
        }

        return false;
    }
}
