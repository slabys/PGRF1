package fill;

public interface PatternFill {

    enum Patterns {
        FullColor, Chess, Circle, NewMethod
    }

    int paint(int x, int y, PatterFill.Patterns patterns);

}
