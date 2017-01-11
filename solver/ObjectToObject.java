package solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ObjectToObject {

    public static ArrayList<ISolverTransformation> CalculateTransformations(ISolverObject before, ISolverObject after) {

        ArrayList<ISolverTransformation> transformations = new ArrayList<>();

        if(before instanceof NullSolverObject && after instanceof NullSolverObject) {
            transformations.add(new NullObjectTransformation());
        } else if (before instanceof NullSolverObject) {
            transformations.add(new AddObjectTransformation());
        } else if (after instanceof NullSolverObject) {
            transformations.add(new DeleteObjectTransformation());
        } else {
            HashMap<String, String> beforeAttribs = before.getAttributes();
            HashMap<String, String> afterAttribs = after.getAttributes();

            Set<String> deletedAttributes = SetExtensions.Difference(beforeAttribs.keySet(), afterAttribs.keySet());
            Set<String> addedAttributes = SetExtensions.Difference(afterAttribs.keySet(), beforeAttribs.keySet());
            Set<String> potentiallyChangedAttributes = SetExtensions.Intersection(beforeAttribs.keySet(), afterAttribs.keySet());

            for (String attrib : deletedAttributes) {
                transformations.add(new SolverTransformation()
                        .WithOperation(TransformationOperation.DELETE)
                        .ForAttribute(attrib));
            }

            for (String attrib : addedAttributes) {
                transformations.add(new SolverTransformation()
                        .WithOperation(TransformationOperation.ADD)
                        .ForAttribute(attrib)
                        .WithAfterValue(afterAttribs.get(attrib)));
            }

            for (String attrib : potentiallyChangedAttributes) {
                String beforeValue = beforeAttribs.get(attrib);
                String afterValue = afterAttribs.get(attrib);

                if (!beforeValue.equals(afterValue)) {
                    if(attrib.equals("inside") || attrib.equals("above")) {
                        if(beforeValue.split(",").length != afterValue.split(",").length){
                            transformations.add(new SolverTransformation()
                                    .WithOperation(TransformationOperation.CHANGE)
                                    .ForAttribute(attrib)
                                    .WithBeforeValue(beforeValue)
                                    .WithAfterValue(afterValue));
                        }
                    } else {
                        transformations.add(new SolverTransformation()
                                .WithOperation(TransformationOperation.CHANGE)
                                .ForAttribute(attrib)
                                .WithBeforeValue(beforeValue)
                                .WithAfterValue(afterValue));
                    }
                }
            }
        }

        return transformations;
    }

    public static int CalculateScore(ArrayList<ISolverTransformation> transformations, boolean hasAddition, boolean hasDeletion) {
        int score = 0;

        if (transformations.size() == 0) {
            return score;
        }

        for (ISolverTransformation t : transformations) {
            if(t instanceof AddObjectTransformation) {
                if(!hasAddition) {
                    score += 30;
                } else {
                    score += 5;
                }
            } else if(t instanceof DeleteObjectTransformation) {
                if(!hasDeletion) {
                    score += 30;
                } else {
                    score += 5;
                }
            } else if (t instanceof NullObjectTransformation) {
                if(!(hasAddition || hasDeletion)) {
                    score += 30;
                } else {
                    score += 5;
                }
            } else {
                String attr = t.getAttribute();
                if(attr != null && (attr.contains("inside") || attr.contains("above"))) {
                    score += 4;
                } else {
                    score += 5;
                }
            }
        }

        return score;
    }
}
