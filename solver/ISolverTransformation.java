package solver;

public interface ISolverTransformation {

    TransformationOperation getOperation();

    void setOperation(TransformationOperation operation);

    String getAttribute();

    void setAttribute(String attribute);

    String getBeforeValue();

    void setBeforeValue(String beforeValue);

    String getAfterValue();

    void setAfterValue(String afterValue);
}
