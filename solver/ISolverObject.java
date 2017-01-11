package solver;

import java.util.HashMap;
import java.util.List;

public interface ISolverObject {
    String getName();
    HashMap<String, String> getAttributes();
    List<ISolverObject> Transform(List<ISolverTransformation> transformations, HashMap<String, String> attributesForAdd);
}
