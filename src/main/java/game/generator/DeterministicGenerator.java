package game.generator;
public final class DeterministicGenerator implements ValueGenerator {
    private final int[] seq;
    private int idx = 0;

    public DeterministicGenerator(int... seq) {
        this.seq = seq;
    }

    @Override
    public int roll(int min, int max) {
        return seq[idx++ % seq.length];
    }
}