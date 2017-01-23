package um.feri.mihael.wi_finder;

import java.util.ArrayList;

public class BitArray
{
    class BitArrayResponse
    {
        private Byte[] bits;
        private int bitCount;

        public BitArrayResponse(ArrayList<Byte> listOfBits, int bitCount)
        {
            bits = new Byte[listOfBits.size()];
            bits = listOfBits.toArray(bits);

            this.bitCount = bitCount;
        }

        public byte[] getPrimitiveBits()
        {
            byte[] primitives = new byte[bits.length];
            for (int i=0; i<primitives.length; i++)
            {
                primitives[i] = bits[i];
            }

            return primitives;
        }

        public Byte[] getBits()
        {
            return bits;
        }

        public int getBitCount()
        {
            return bitCount;
        }
    }

    protected ArrayList<Byte> bitArray;
    protected int numOfBits;
    protected int bitIndex;

    public BitArray()
    {
        bitArray = new ArrayList<Byte>();
        bitArray.add((byte)0);

        numOfBits = 0;
        bitIndex = 0;
    }

    public void AddBits(Byte[] bitsToAdd, int bitCount, int startingBitIndex, int startingByteIndex)
    {
        int bitIndexSrc = startingBitIndex;
        int byteIndexSrc = startingByteIndex;

        while(bitCount != 0)
        {
            byte bit = (byte)(((bitsToAdd[byteIndexSrc]) & (1 << bitIndexSrc)) != 0 ? 1 : 0);
            bitArray.set((bitArray.size() - 1), (byte)(bitArray.get(bitArray.size() - 1) | (byte)(bit << bitIndex)));

            bitIndexSrc++;
            bitIndex++;
            numOfBits++;

            if (bitIndexSrc == 8)
            {
                bitIndexSrc = 0;
                byteIndexSrc++;
            }

            if(bitIndex == 8)
            {
                bitIndex = 0;
                bitArray.add((byte)0);
            }

            bitCount--;
        }
    }

    public void AddBit(boolean isOne)
    {
        byte bit = (byte)((isOne) ? 1 : 0);
        bitArray.set((bitArray.size() - 1), (byte)(bitArray.get(bitArray.size() - 1) | (byte)(bit << bitIndex)));
        bitIndex++;
        numOfBits++;

        if (bitIndex == 8)
        {
            bitIndex = 0;
            bitArray.add((byte)0);
        }
    }

    public BitArrayResponse getBits()
    {
        return new BitArrayResponse(bitArray, numOfBits);
    }
}
