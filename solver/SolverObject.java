package solver;

import ravensproject.RavensObject;

import java.util.*;

public class SolverObject implements ISolverObject {
    private String name;
    private HashMap<String, String> attributes;

    public SolverObject(RavensObject ravensObject) {
        name = ravensObject.getName();
        attributes = ravensObject.getAttributes();
    }

    public SolverObject(String name, HashMap<String, String> attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public HashMap<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public List<ISolverObject> Transform(List<ISolverTransformation> transformations, HashMap<String, String> attributesForAdd) {
        HashMap<String, String> newAttributes = new HashMap<>(attributes);
        for(ISolverTransformation transformation : transformations) {
            if(transformation instanceof AddObjectTransformation) {
                SolverObject obj1 = new SolverObject(UUID.randomUUID().toString(), newAttributes);
                SolverObject obj2 = new SolverObject(UUID.randomUUID().toString(), attributesForAdd);
                return Arrays.asList(obj1, obj2);
            } else if(transformation instanceof DeleteObjectTransformation || transformation instanceof NullObjectTransformation) {
                return Arrays.asList(new NullSolverObject());
            } else {
                String attribute = transformation.getAttribute();
                String attributeValue = transformation.getAfterValue();
                switch (transformation.getOperation()) {
                    case ADD:
                        newAttributes.put(attribute, attributeValue);
                        break;
                    case DELETE:
                        newAttributes.remove(attribute);
                        break;
                    case CHANGE:
                        if(attribute.equals("angle")) {
                            int beforeAngle = Integer.parseInt(transformation.getBeforeValue());
                            int afterAngle = Integer.parseInt(transformation.getAfterValue());
                            int currentAngle = Integer.parseInt(newAttributes.get(attribute));

                            int horizontalReflect = ReflectHorizontalAngle(beforeAngle);
                            int verticalReflect = ReflectVerticalAngle(beforeAngle);

                            int resultAngle;
                            if(horizontalReflect == afterAngle) {
                                resultAngle = ReflectHorizontalAngle(currentAngle);
                            } else if(verticalReflect == afterAngle) {
                                resultAngle = ReflectVerticalAngle(currentAngle);
                            } else {
                                // not a reflection
                                int change = afterAngle - beforeAngle;
                                resultAngle = currentAngle + change;
                            }
                            newAttributes.replace(attribute, Integer.toString(resultAngle));
                        } else if(attribute.equals("alignment")) {

                            String beforeAlignment = transformation.getBeforeValue();
                            String afterAlignment = transformation.getAfterValue();
                            String currentAlignment = newAttributes.get(attribute);

                            String horizontalReflect = ReflectHorizontalAlignment(beforeAlignment);
                            String verticalReflect = ReflectVerticalAlignment(beforeAlignment);
                            String horizontalAndVerticalReflect = ReflectVerticalAlignment(horizontalReflect);

                            String resultAlignment;
                            if(afterAlignment.equals(horizontalReflect)) {
                                resultAlignment = ReflectHorizontalAlignment(currentAlignment);
                            } else if(afterAlignment.equals(verticalReflect)) {
                                resultAlignment = ReflectVerticalAlignment(currentAlignment);
                            } else if(afterAlignment.equals(horizontalAndVerticalReflect)) {
                                resultAlignment = ReflectHorizontalAlignment(ReflectVerticalAlignment(currentAlignment));
                            } else {
                                // not a reflection
                                resultAlignment = currentAlignment;
                            }
                            newAttributes.replace(attribute, resultAlignment);
                        } else {
                            newAttributes.replace(attribute, attributeValue);
                        }
                        break;
                }
            }
        }

        return Arrays.asList(new SolverObject(UUID.randomUUID().toString(), newAttributes));
    }

    private int ReflectVerticalAngle(int angle) {
        int vertical = angle * -1;
        if(vertical < 0) {
            // normalize
            return vertical + 360;
        }
        return vertical;
    }

    private int ReflectHorizontalAngle(int angle) {
        int horizontal = 180 - angle;
        if (horizontal < 0) {
            return horizontal + 360;
        }
        return horizontal;
    }

    private String ReflectHorizontalAlignment(String alignment) {
        if(alignment.contains("left")) {
            return alignment.replace("left", "right");
        }

        if(alignment.contains("right")) {
            return alignment.replace("right", "left");
        }

        return alignment;
    }

    private  String ReflectVerticalAlignment(String alignment) {
        if(alignment.contains("top")) {
            return alignment.replace("top", "bottom");
        }

        if(alignment.contains("bottom")) {
            return alignment.replace("bottom", "top");
        }

        return alignment;
    }
}
