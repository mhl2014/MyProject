package um.feri.mihael.wi_finder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class DctCompressionActivity extends AppCompatActivity {

    private Button dctStartButton;
    private TextView dctResultDisplay;
    private ImageView dctExampleImage;
    private EditText dctCompressionFactorEditText;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dct_compression);

        context = this.getApplicationContext();

        dctExampleImage = (ImageView) findViewById(R.id.dctExampleImageView);
        dctResultDisplay = (TextView) findViewById(R.id.dctResultDisplay);
        dctStartButton = (Button) findViewById(R.id.dctBeginButton);
        dctCompressionFactorEditText = (EditText) findViewById(R.id.dctCompressionFactorInput);
        dctStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int compressionfactor;
                int originalSize;
                String compressionFactorString = dctCompressionFactorEditText.getText().toString();

                // get the compression Factor
                if(!(compressionFactorString).equals(""))
                    compressionfactor = Integer.parseInt(compressionFactorString);
                else
                    compressionfactor = 0;

                // get the image bitmap and its size
                Bitmap imageBitmap = ((BitmapDrawable)dctExampleImage.getDrawable()).getBitmap();
                originalSize = imageBitmap.getByteCount();

                // run the algorithm
//                try {
                    executeDctCompression(imageBitmap, compressionfactor, originalSize);
//                }
//                catch (Exception ex)
//                {
//                    (Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG)).show();
//                }
            }
        });
    }

    private void executeDctCompression(Bitmap bmp, int compressionFactor, long originalLen)
    {
        if (compressionFactor < 0 || compressionFactor > 15)
            throw new RuntimeException("Compression factor must be between 0 and 15.");

        int heightAdaptation = bmp.getHeight();
        int widthAdaptation = bmp.getWidth();

        while (widthAdaptation % 8 != 0)
            widthAdaptation++;

        while (heightAdaptation % 8 != 0)
            heightAdaptation++;

        Bitmap toCompress = null;
        try {
            toCompress = Bitmap.createScaledBitmap(bmp, widthAdaptation, heightAdaptation, false);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return;
        }
        // 2. in 3. korak
        int numOfBlocks = (toCompress.getWidth() * toCompress.getHeight()) / 64; // Izracunamo Stevilo potrebnih blokov oz. matrik

        // Ustvarimo matrike za stiskanje
        DCTMatrix[] matricesRed = new DCTMatrix[numOfBlocks];
        DCTMatrix[] matricesGreen = new DCTMatrix[numOfBlocks];
        DCTMatrix[] matricesBlue = new DCTMatrix[numOfBlocks];

        for(int i=0; i<numOfBlocks; i++)
        {
            matricesRed[i] = new DCTMatrix();
            matricesGreen[i] = new DCTMatrix();
            matricesBlue[i] = new DCTMatrix();
        }

        int matrixX = 0;
        int matrixY = 0;
        int matriceInd = 0;

        for (int y = 0; y < toCompress.getHeight(); y++)
        {
            for (int x = 0; x < toCompress.getWidth(); x++)
            {
                int pixelColor = toCompress.getPixel(x, y);
                int red = Color.red(pixelColor);
                int green = Color.green(pixelColor);
                int blue = Color.blue(pixelColor);
                matricesRed[matriceInd].setElement(matrixX, matrixY, (byte)(red - 128));
                matricesGreen[matriceInd].setElement(matrixX, matrixY, (byte)(green - 128));
                matricesBlue[matriceInd].setElement(matrixX, matrixY, (byte)(blue - 128));

                // Povecamo stevce
                matrixX++;

                if (matrixX == 8)
                {
                    matrixX = 0;
                    matrixY++;
                }

                if (matrixY == 8)
                {
                    matrixY = 0;
                    matriceInd++;
                }
            }
        }

        // Vse matrike dodamo v en samo polje
        DCTMatrix[][] matricesRGB = new DCTMatrix[3][];
        matricesRGB[0] = matricesRed;
        matricesRGB[1] = matricesGreen;
        matricesRGB[2] = matricesBlue;

        // 4. korak izvedemo DCT
        ArrayList<int[][]> exitMatrices = new ArrayList<int[][]>();

        // Vnaprej izracunamo mozne vrednosti za kosinuse
        double[][] cosineValues = new double[8][8];

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                cosineValues[j][i] = Math.cos(((2 * i + 1) * j * Math.PI) / 16);
            }
        }

        // Izvdemo DCT za vsako barvo
        for (int colorInd = 0; colorInd < matricesRGB.length; colorInd++)
        {
            // ... za vsako matriko barvnega kanala
            DCTMatrix[] colorChannel = matricesRGB[colorInd];
            for (int matInd = 0; matInd < colorChannel.length; matInd++)
            {
                DCTMatrix matrix = colorChannel[matInd];

                int[][] fdctMatrix = new int[8][8];
                for (int v = 0; v < 8; v++)
                {
                    for (int u = 0; u < 8; u++)
                    {
                        double c1 = (u == 0) ? (1.0 / Math.sqrt(2)) : 1.0;
                        double c2 = (v == 0) ? (1.0 / Math.sqrt(2)) : 1.0;

                        double sum = 0;
                        for (int y = 0; y < 8; y++)
                        {
                            for (int x = 0; x < 8; x++)
                            {
                                sum += matrix.getElementValue(x, y) * cosineValues[x][u] * cosineValues[y][v];
                            }
                        }

                        fdctMatrix[u][v] = (int)(0.25 * c1 * c2 * sum);
                    }
                }

                exitMatrices.add(fdctMatrix);
            }
        }

        // 5. korak (Faktor stiskanja)
        // Nad vsako matriko nastavimo potrebne elemente na nič
        for (int i = 0; i < exitMatrices.size(); i++)
        {
            // Ponastavimo cf
            int cf = compressionFactor;
            while (cf > 0)
            {
                int yInd, xInd;

                if (cf >= 8)
                {
                    yInd = 0;
                    xInd = 15 - cf;
                }
                else
                {
                    xInd = 7;
                    yInd = 8 - cf;
                }

                while ((yInd <= 7) && (xInd >= 0))
                {
                    exitMatrices.get(i)[xInd][yInd] = 0;
                    yInd++;
                    xInd--;
                }

                cf--;
            }
        }

        // Korak 6 CikCak
        ArrayList<int[]> redistributedMatrices = new ArrayList<int[]>();
        for (int i = 0; i < exitMatrices.size(); i++)
        {
            redistributedMatrices.add(redistributeMatrix(exitMatrices.get(i)));
        }

        // 7. Korak zapis v daoteko
        BitArray bitArray = new BitArray();

        for (int i = 0; i < redistributedMatrices.size(); i++)
        {
            int coded = 1;
            int[] f = redistributedMatrices.get(i);
            int mask;

            // Prvega vedno zakodiramo z 11 biti
            if (f[coded] > 0)
                bitArray.AddBit(false);
            else
                bitArray.AddBit(true);

            mask = 1;
            for (int u = 0; u < 10; u++)
            {
                if ((f[coded] & (mask << u)) == 0)
                    bitArray.AddBit(false);
                else
                    bitArray.AddBit(true);
            }

            while (coded < f.length)
            {
                if (f[coded] != 0) // Kodiramo c
                {
                    bitArray.AddBit(true); // Oznacuje tip kodiranja
                    codeAC(f[coded], bitArray); // V celoti zakodira dolzino in vrednost AC

                    coded++;
                }
                else
                {
                    int numOfZeroes = 0; // Stevilo nicelnih elementov
                    boolean stop = false;

                    while (!stop && (coded + numOfZeroes != 64)) // Prestejemo stevilo nicel
                    {
                        if ((f[coded + numOfZeroes] == 0))
                            numOfZeroes++;
                        else
                            stop = true;
                    }

                    if (coded + numOfZeroes == 64)
                    {
                        bitArray.AddBit(false); // Kodiramo b

                        // Kodiramo tekoco dolzino
                        mask = 1;
                        for (int u = 0; u < 6; u++)
                            bitArray.AddBit((numOfZeroes & (mask << u)) != 0);

                        coded += numOfZeroes;
                    }
                    else
                    {
                        bitArray.AddBit(false); // Kodiramo a

                        // Kodiramo tekoco dolzino
                        mask = 1;
                        for (int u = 0; u < 6; u++)
                            bitArray.AddBit((numOfZeroes & (mask << u)) != 0);

                        // Zakodiramo dolzino in vrednost elementa
                        codeAC(f[coded + numOfZeroes], bitArray);

                        coded += numOfZeroes + 1;
                    }
                }
            }
        }

        // Pridobimo stisnjen zapis
        BitArray.BitArrayResponse response = bitArray.getBits();
        byte[] Width = ByteBuffer.allocate(2).putShort((short)toCompress.getWidth()).array();
        byte[] Height = ByteBuffer.allocate(2).putShort((short)toCompress.getHeight()).array();
        byte[] ImageBytes = response.getPrimitiveBits();

        // Ustvarimo "paket" kamor se doda velisost datoteke v sirini in dolzini
        byte[] packageToWrite = new byte[4 + ImageBytes.length];
        System.arraycopy(Width, 0, packageToWrite, 0, 2);
        System.arraycopy(Height, 0, packageToWrite, 2, 2);
        System.arraycopy(ImageBytes, 0, packageToWrite, 4, ImageBytes.length);

        double compressionRatio = ((double)(originalLen) / (double)ImageBytes.length);
        dctResultDisplay.setText("Compression Ratio: " + compressionRatio);
    }

    // CikCak metoda
    protected int[] redistributeMatrix(int[][] matrix)
    {
        final int overflow = 2033;
        final int bound = 7;
        boolean done = false;
        int x = 0;
        int y = 0;
        int index = 0;

        int[] redistributed = new int[(bound + 1) * (bound + 1)];

        do
        {
            redistributed[index] = matrix[x][y];
            matrix[x][y] = overflow;

            if ((x > 0) && (y < bound) && matrix[x - 1][y + 1] < overflow)
            {
                x--;
                y++;
            }
            else
            {
                if ((x < bound) && (y > 0) && matrix[x + 1][y - 1] < overflow)
                {
                    x++;
                    y--;
                }
                else if ((x > 0) && (x < bound))
                x++;
            else if ((y > 0) && (y < bound))
                y++;
            else if (x < bound)
                x++;
            else
                done = true;
            }

            index++;

        } while (!done);

        return redistributed;
    }

    protected void codeAC(int num, BitArray buffer)
    {
        int numAbs = Math.abs(num);
        int numObBitsUSed = 0;
        boolean isPostive = num > 0;

        // Dolocimo stevilo bitov potrebnih za zapis
        if (isPostive)
            while ((Math.pow(2, numObBitsUSed) - 1) < numAbs)
                numObBitsUSed++;
        else
            while ((Math.pow(2, numObBitsUSed)) < numAbs)
                numObBitsUSed++;

        // Dodamo bite, ki dolocajo dolzino
        int mask = 1;
        for (int i = 0; i < 4; i++)
            buffer.AddBit((numObBitsUSed & (mask << i)) != 0);

        // Dodamo bite, ki kodirajo AC vrednost
        for (int i = 0; i < numObBitsUSed; i++)
            buffer.AddBit((num & (mask << i)) != 0);
    }
}
