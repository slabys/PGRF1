package fill;

public interface PatternFill {

    enum Patterns {
        Chess, Circle, NewMethod
    }

    int paint(int x, int y, PatterFill.Patterns patterns);

}
