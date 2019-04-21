/*******************************************************************************
 * AnthObfuscator v1.1 - (c) 2008-2009 AnthraX
 * File : UIndex.java
 * Revision History:
 *      Created by AnthraX
 ******************************************************************************/
package anthobfuscator.datatypes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Java implementation of the unreal INDEX datatype. This is also known as the
 * "Compact Index" type. It's used to store (preferably small) numbers without
 * the overhead of a DWORD.
 *
 * The bytes of the index are stored in the order they were read/written. We
 * also keep the size (S) and the value as a long (L) up to date.
 */
public class UIndex
{
    /** Store each byte seperately (we shouldn't need all 5!) */
    public byte A,B,C,D,E;

    /** Store long value as well */
    protected long L;

    /** Store size of the INDEX */
    protected int S;

    /**
     * Constructor
     * @param AA Value of the INDEX A byte
     * @param BB Value of the INDEX B byte
     * @param CC Value of the INDEX C byte
     * @param DD Value of the INDEX D byte
     * @param EE Value of the INDEX E byte
     * @param LL Long value of the INDEX. Calculated during read rather than in the constructor
     * @param SS Size of the index
     */
    public UIndex (byte AA, byte BB, byte CC, byte DD, byte EE, long LL, int SS)
    {
        A = AA;
        B = BB;
        C = CC;
        D = DD;
        E = EE;
        L = LL;
        S = SS;
        //System.out.println("Index create: "+AA+" "+BB+" "+CC+" "+DD+" "+LL+" "+SS);
    }

    /**
     * Read an INDEX from an unreal package file.
     * Fix the endianness and return the UIndex object.
     * @param f RandomAccessFile object of the Package
     * @return UIndex object
     */
    public static UIndex readINDEX(RandomAccessFile f) throws IOException
    {
        byte aa = (byte)0,bb = (byte)0,cc = (byte)0,dd = (byte)0, ee= (byte)0;
        boolean negative;
        long val = 0;
        int ss = 0;

        aa = f.readByte();
        negative = ((aa&0x80) != 0);
        /** check if there's more */
        if ((aa&0x40) != 0)
        {
            bb = f.readByte();
            /** check if there's more */
            if ((bb&0x80) != 0)
            {
                cc = f.readByte();
                if ((cc&0x80) != 0)
                {
                    dd = f.readByte();
                    if ((dd&0x80) != 0)
                    {
                        ee = f.readByte();
                        val = ((int)aa&0x3F) + (((int)bb&0x7F)<<6) + (((int)cc&0x7F)<<(6+7)) + (((int)dd&0x7F)<<(6+7+7)) + (((int)ee&0x7F)<<(6+7+7+7));
                        ss = 5;
                    }
                    else
                    {
                        val = ((int)aa&0x3F) + (((int)bb&0x7F)<<6) + (((int)cc&0x7F)<<(6+7)) + (((int)dd&0x7F)<<(6+7+7));
                        ss = 4;
                    }
                }
                else
                {
                    val = ((int)aa&0x3F) + (((int)bb&0x7F)<<6) + (((int)cc&0x7F)<<(6+7));
                    ss = 3;
                }
            }
            else
            {
                val = ((int)aa&0x3F)+(((int)bb&0x7F)<<6);
                ss = 2;
            }
        }
        else
        {
            val = ((int)aa&0x3F);
            ss = 1;
        }

        if (negative)
            val *= -1;

        return new UIndex(aa,bb,cc,dd,ee,val,ss);
    }

    /**
     * Write an INDEX to an unreal package file.
     * @param f RandomAccessFile object of the Package
     * @return true if successful
     */
    public boolean writeINDEX(RandomAccessFile f) throws IOException
    {
        byte[] bytes = {A,B,C,D,E};
        f.write(bytes, 0, S);
        return true;
    }

    @Override
    public String toString()
    {
        return "#"+L;
    }

    /**
     * Get the value of this INDEX as a long
     * @return long value of this INDEX
     */
    public long getLongValue()
    {
        return L;
    }

    /**
     * Set a new value for this INDEX and update the A,B,C,D bytes accordingly
     * @param LL desired value
     */
    public void setLongValue(long LL)
    {
        L = LL;

        /** MSB -> 6 bits, other bytes -> 7 bits
         * 0011 1111 = 3F
         * 0111 1111 = 7F
         *
         * ORIGINAL DWORD:
         * <---OD--->   <---OC--->   <---OB--->   <---OA--->
         * xxxx  xxxx   xxxx  xxxx   xxxx  xxxx   xxxx  xxxx
         *                                          <-  --->
         *                                             AA
         *                              <  ----   ->
         *                                   BB
         *                    <---   -->
         *                        CC
         *        <--   --->
         *            DD
         * INDEX:
         * 6 LOWEST  bits of AA = 6 LOWEST  bits of OA
         *
         * 2 LOWEST  bits of BB = 2 HIGHEST bits of OA
         * 5 HIGHEST bits of BB = 5 LOWEST  bits of OB
         *
         * 3 LOWEST  bits of CC = 3 HIGHEST bits of OB
         * 4 HIGHEST bits of CC = 4 LOWEST  bits of OC
         *
         * 4 LOWEST  bits of DD = 4 HIGHEST bits of OC
         * 3 HIGHEST bits of DD = 3 LOWEST  bits of OD
         */
        long OA,OB,OC,OD;
        long AA,BB,CC,DD,EE;
        boolean negative = (LL<0);
        if (negative)
            LL *= -1;

        /** Calculate original bytes first */
        OA = (LL&0xFF);
        OB = (LL&0xFF00)>>8;
        OC = (LL&0xFF0000)>>16;
        OD = (LL&0xFF000000)>>24;

        /** Calculate AA */
        AA = (OA&0x3F);

        /** Calculate BB */
        BB = (((OA&0xC0)>>6) /** 2 higest bits of OA */
                + ((OB&0x1F)<<2)); /** 5 lowest bits of OB */

        /** Calculate CC */
        CC = (((OB&0xE0)>>5) /** 3 highest bits of OB */
                + ((OC&0x0F)<<3)); /** 4 lowest bits of OC */

        /** Calculate DD */
        DD = (((OC&0xF0)>>4) /** 4 highest bits of OC */
                + ((OD&0x07)<<4)); /** 3 lowest bits of OD */

        /** Calculate EE */
        EE = (OD&0xF8)>>3;

        /** Set sign bit in AA */
        if (negative)
            AA = (AA|0x80);

        /** Set followup bits */
        if (EE != 0x0)
            DD = (DD|0x80);

        if (DD != 0x0)
            CC = (CC|0x80);

        if (CC != 0x0)
            BB = (BB|0x80);

        if (BB != 0x0)        
            AA = (AA|0x40);                    

        /** Calculate size */
        S = 1 + ((BB != 0x0)?1:0) + ((CC != 0x0)?1:0) + ((DD != 0x0)?1:0) + ((EE != 0x0)?1:0);

        A = (byte)AA;
        B = (byte)BB;
        C = (byte)CC;
        D = (byte)DD;
        E = (byte)EE;
    }   

    /**
     * get the size of this INDEX in a file
     * @return size of the INDEX
     */
    public int getSize()
    {
        return S;
    }
}
