/*******************************************************************************
 * AnthObfuscator v1.1 - (c) 2008-2009 AnthraX
 * File : UDword.java
 * Revision History:
 *      Created by AnthraX
 ******************************************************************************/
package anthobfuscator.datatypes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Java implementation of the unreal DWORD datatype. Stores the 4 bytes of the
 * DWORD in the order they were written (A,B,C,D). Also stores the value of the
 * DWORD in a long (L).
 */
public class UDword
{
    /** Store each byte in the DWORD seperately */
    private byte A,B,C,D;

    /** Also store the DWORD as a long */
    private long L;

    /**
     * Constructor
     * @param AA Value of the DWORD A byte
     * @param BB Value of the DWORD B byte
     * @param CC Value of the DWORD C byte
     * @param DD Value of the DWORD D byte
     */
    public UDword (byte AA, byte BB, byte CC, byte DD)
    {
        A = AA;
        B = BB;
        C = CC;
        D = DD;
        L = (((int)A&0xFF)+(((int)B&0xFF)<<8)+(((int)C&0xFF)<<16)+(((long)D&0xFF)<<24));
    }

    /**
     * Read a DWORD from an unreal package file and return the UDword object.
     * @param f RandomAccessFile object of the Package
     * @return UDword object
     */
    public static UDword readDWORD(RandomAccessFile f) throws IOException
    {
        byte aa,bb,cc,dd;

        aa = f.readByte();
        bb = f.readByte();
        cc = f.readByte();
        dd = f.readByte();

        return new UDword(aa,bb,cc,dd);
    }

    /**
     * Write a DWORD to an Unreal Package file
     * @param f RandomAccessFile object of the Package
     * @return true if successful
     */
    public boolean writeDWORD(RandomAccessFile f) throws IOException
    {
        byte[] bytes = {A,B,C,D};
        f.write(bytes, 0, 4);
        return true;
    }

    /**
     * Convert this DWORD to a hex string (endianness inverted!à
     * @return hex string value of the DWORD
     */
    @Override
    public String toString()
    {
        return "0x"+Integer.toHexString(0xFF&D).toUpperCase()
                +Integer.toHexString(0xFF&C).toUpperCase()
                +Integer.toHexString(0xFF&B).toUpperCase()
                +Integer.toHexString(0xFF&A).toUpperCase();
    }

    /**
     * Get the lower word value of this DWORD (bytes A and B)
     * @return lower word value of this DWORD as a UWord object
     */
    public UWord getLowerWord()
    {
        return new UWord(A,B);
    }

    /**
     * Get the higher word value of this DWORD (bytes C and D)
     * @return higher word value of this DWORD as a UWord object
     */
    public UWord getHigherWord()
    {
        return new UWord(C,D);
    }

    /**
     * Get the value of this DWORD as a long
     * @return long value of this DWORD
     */
    public long getLongValue()
    {
        return L;
    }

    /**
     * Set a new value for this DWORD and update the A,B,C,D bytes accordingly
     * @param LL desired value
     */
    public void setLongValue(long LL)
    {
        L = LL;
        A = (byte)(L&0xFF);
        B = (byte)((L&0xFF00)>>8);
        C = (byte)((L&0xFF0000)>>16);
        D = (byte)((L&0xFF000000)>>24);
    }

    /**
     * get the size of this DWORD in a file
     * @return size of the DWORD
     */
    public int getSize()
    {
        return 4;
    }
}
