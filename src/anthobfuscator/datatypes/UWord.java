/*******************************************************************************
 * AnthObfuscator v1.1 - (c) 2008-2009 AnthraX
 * File : UWord.java
 * Revision History:
 *      Created by AnthraX
 ******************************************************************************/
package anthobfuscator.datatypes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Java implementation of the unreal WORD datatype. Stores the 2 bytes of the
 * WORD in the order they were written (A,B). Also stores the value of the
 * WORD in an int (I).
 *
 * This is seldom used
 */
public class UWord
{
    /** Store each byte seperately */
    private byte A,B;

    /** Also store as int */
    private int I;

    /**
     * Constructor
     * @param AA Value of the WORD A byte
     * @param BB Value of the WORD B byte
     */
    public UWord(byte AA, byte BB)
    {
        A = AA;
        B = BB;

        I = (AA&0xFF)+((BB&0xFF)<<8);
    }

    /**
     * Write a WORD to an Unreal Package file
     * @param f RandomAccessFile object of the Package
     * @return true if successful
     */
    public boolean writeWORD(RandomAccessFile f) throws IOException
    {
        byte[] bytes = {A,B};
        f.write(bytes, 0, 2);
        return true;
    }

    /**
     * Convert this WORD to a hex string (endianness inverted!)
     * @return hex string value of the WORD
     */
    @Override
    public String toString()
    {
        return "0x"+Integer.toHexString(0xFF&B).toUpperCase()
                +Integer.toHexString(0xFF&A).toUpperCase();
    }

    /**
     * Get the value of this WORD as an int
     * @return int value of this WORD
     */
    public int getIntValue()
    {
        return I;
    }

    /**
     * Set a new value for this WORD and update the A,B bytes accordingly
     * @param II desired value
     */
    public void setIntValue(int II)
    {
        I = II;
        A = (byte)(I&0xFF);
        B = (byte)((I&0xFF00)>>8);
    }

    /**
     * get the size of this WORD in a file
     * @return size of the WORD
     */
    public int getSize()
    {
        return 2;
    }
}
