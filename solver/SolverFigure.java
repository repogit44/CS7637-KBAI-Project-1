package solver;

import ravensproject.RavensFigure;
import ravensproject.RavensObject;

import java.util.HashMap;

public class SolverFigure {

    private String name;
    private HashMap<String, ISolverObject> solverObjects;

    public SolverFigure(RavensFigure ravensFigure) {
        name = ravensFigure.getName();
        solverObjects = new HashMap<>();

        for (HashMap.Entry<String, RavensObject> ravensObject : ravensFigure.getObjects().entrySet()) {
            solverObjects.put(ravensObject.getKey(), new SolverObject(ravensObject.getValue()));
        }
    }

    public HashMap<String, ISolverObject> getSolverObjects() {
        return solverObjects;
    }

    public String getName() {
        return name;
    }
}
