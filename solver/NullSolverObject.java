package solver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class NullSolverObject implements ISolverObject {

    @Override
    public String getName() {
        return "Null";
    }

    @Override
    public HashMap<String, String> getAttributes() {
        return null;
    }

    @Override
    public List<ISolverObject> Transform(List<ISolverTransformation> transformations, HashMap<String, String> attributesForAdd) {
        for(ISolverTransformation transformation : transformations) {
            if(transformation instanceof AddObjectTransformation) {
                return Arrays.asList(new SolverObject(UUID.randomUUID().toString(), attributesForAdd));
            } else if(transformation instanceof DeleteObjectTransformation || transformation instanceof NullObjectTransformation) {
                return Arrays.asList(new NullSolverObject());
            } else {
                throw new IllegalArgumentException("NullSolverObject - Illegal change transformation");
            }
        }

        return Arrays.asList(new NullSolverObject());
    }
}
