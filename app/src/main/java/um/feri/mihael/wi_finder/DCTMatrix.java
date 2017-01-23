package um.feri.mihael.wi_finder;

public class DCTMatrix {
    private Byte[][] matrix;

    public final int Width = 8;
    public final int Height = 8;

    public DCTMatrix() {
        matrix = new Byte[Width][Height];
    }

    public void setElement(int x, int y, Byte value)//, ColorChannel channel )
    {
        if (value > Byte.MAX_VALUE || value < Byte.MIN_VALUE)
            throw new RuntimeException("Invalid value passed: " + value);

        matrix[x][y] = value;
    }

    public Byte getElementValue(int x, int y) {
        return matrix[x][y];
    }
}
