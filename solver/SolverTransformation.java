package solver;

public class SolverTransformation implements ISolverTransformation {
    private TransformationOperation operation;
    private String attribute;
    private String beforeValue;
    private String afterValue;

    public SolverTransformation WithOperation(TransformationOperation operation) {
        this.setOperation(operation);
        return this;
    }

    public SolverTransformation ForAttribute(String attribute) {
        this.setAttribute(attribute);
        return this;
    }

    public SolverTransformation WithBeforeValue(String value) {
        this.setBeforeValue(value);
        return this;
    }

    public SolverTransformation WithAfterValue(String value) {
        this.setAfterValue(value);
        return this;
    }

    @Override
    public TransformationOperation getOperation() {
        return operation;
    }

    @Override
    public void setOperation(TransformationOperation operation) {
        this.operation = operation;
    }

    @Override
    public String getAttribute() {
        return attribute;
    }

    @Override
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getBeforeValue() {
        return beforeValue;
    }

    @Override
    public void setBeforeValue(String beforeValue) {
        this.beforeValue = beforeValue;
    }

    @Override
    public String getAfterValue() {
        return afterValue;
    }

    @Override
    public void setAfterValue(String afterValue) {
        this.afterValue = afterValue;
    }
}
