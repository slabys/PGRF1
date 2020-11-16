package fill;

public class PatterFillChess implements PatternFill {

    @Override
    public int paint(int x, int y) {
        if (x % 2 == 1 && y % 2 != 1) {
            return 0x000000;
        }
        return 0xFFFFFF;
    }
}
