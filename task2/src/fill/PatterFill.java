package fill;

import java.awt.*;

public class PatterFill implements PatternFill {

    private int radius = 5;

    @Override
    public int paint(int x, int y, Patterns pattern) {
        switch (pattern) {
            case Chess -> {
                return chess(x, y);
            }
            case Circle -> {
                return circle(x, y);
            }
            case NewMethod -> {
                return newMethod(x, y);
            }
            default -> {
                return Color.BLACK.getRGB();
            }
        }
    }

    private int newMethod(int x, int y) {
        if ((x % 2 != 0) == (y % 2 == 0)) {
            return Color.GREEN.getRGB();
        }
        return Color.BLACK.getRGB();
    }

    private int chess(int x, int y) {
        if ((x % 2) == 0) {
            return 0xff;
        } else {
            return 0xFFFFFF;
        }
    }

    private int circle(int x, int y) {
        int sx = x % (2 * radius) - radius;
        int sy = y % (2 * radius) - radius;
        if (Math.sqrt(sx * sx + sy * sy) < radius) {
            return 0xffddcc;
        }
        return 0xffffff;
    }
}
