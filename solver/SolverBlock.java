package solver;

import java.util.ArrayList;
import java.util.List;

public class SolverBlock implements Comparable<SolverBlock> {

    private ISolverObject base;
    private ISolverObject right;
    private ISolverObject down;

    private int indexA;
    private int indexB;
    private int indexC;

    private int dimA;
    private int dimB;
    private int dimC;

    private List<ISolverObject> generated;

    public ArrayList<ISolverTransformation> baseToRightTransformations;
    public int baseToRightScore;

    public ArrayList<ISolverTransformation> baseToDownTransformations;
    public int baseToDownScore;

    public SolverBlock(ISolverObject base, ISolverObject right, ISolverObject down,
                       int indexA, int indexB, int indexC,
                       int dimA, int dimB, int dimC) {
        this.base = base;
        this.right = right;
        this.down = down;

        this.indexA = indexA;
        this.indexB = indexB;
        this.indexC = indexC;

        this.dimA = dimA;
        this.dimB = dimB;
        this.dimC = dimC;

        this.baseToRightTransformations = ObjectToObject.CalculateTransformations(this.base, this.right);
        this.baseToRightScore = ObjectToObject.CalculateScore(this.baseToRightTransformations, this.dimB > this.dimA, this.dimB < this.dimA);

        this.baseToDownTransformations = ObjectToObject.CalculateTransformations(this.base, this.down);
        this.baseToDownScore = ObjectToObject.CalculateScore(this.baseToDownTransformations, this.dimC > this.dimA, this.dimC < this.dimA);

        try {
            this.generated = this.right.Transform(this.baseToDownTransformations, this.down.getAttributes());
        } catch(IllegalArgumentException e) { }

        if(this.generated == null) {
            try {
                this.generated = this.down.Transform(this.baseToRightTransformations, this.right.getAttributes());
            } catch(IllegalArgumentException e) {
                this.generated = new ArrayList<>();
            }
        }
    }

    public List<ISolverObject> getGenerated() {
        return generated;
    }

    public int getTotalScore() {
        return this.baseToRightScore + this.baseToDownScore;
    }

    @Override
    public String toString() {
        String line1 = String.format("[%d][%d][%d] - %d", indexA, indexB, indexC, this.getTotalScore());
        String line2 = String.format("\tbase:  %s", base.getAttributes());
        String line3 = String.format("\tright: %s", right.getAttributes());
        String line4 = String.format("\tdown:  %s", down.getAttributes());
        return String.format("%s\n%s\n%s\n%s", line1, line2, line3, line4);

    }

    public int getIndexA() {
        return indexA;
    }

    public int getIndexB() {
        return indexB;
    }

    public int getIndexC() {
        return indexC;
    }


    @Override
    public int compareTo(SolverBlock o) {

        if(this.getTotalScore() < o.getTotalScore()) return -1;
        if(this.getTotalScore() > o.getTotalScore()) return 1;

        if(this.baseToDownScore < o.baseToDownScore && this.baseToDownScore < o.baseToRightScore) return -1;
        if(this.baseToRightScore < o.baseToDownScore && this.baseToRightScore < o.baseToRightScore) return -1;

        if(this.baseToDownScore > o.baseToDownScore && this.baseToDownScore > o.baseToRightScore) return 1;
        if(this.baseToRightScore > o.baseToDownScore && this.baseToRightScore > o.baseToRightScore) return 1;

        return 0;
    }
}
