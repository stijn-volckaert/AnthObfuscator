/*******************************************************************************
 * AnthObfuscator v1.1 - (c) 2008-2009 AnthraX
 * File : UName.java
 * Revision History:
 *      Created by AnthraX
 ******************************************************************************/
package anthobfuscator.datatypes;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Java implementation of the unreal NAME datatype. This is basically the same
 * as a standard, null-terminated string. The only difference is that the first
 * first char of the string is a byte representing the length of the string
 * (including the null char but not the length byte).
 *
 * Eg: the string "Unreal" would be written as
 *
 * 0x07 'U' 'n' 'r' 'e' 'a' 'l' '\0'
 */
public class UName
{
    /** Store the name as a String (ignore \0 and length bit) */
    private String NAME;

    /**
     * Constructor
     * @param n String form of the UName
     */
    public UName (String n)
    {
        NAME = n;
    }

    /**
     * Read a NAME from an unreal package file
     * @param f RandomAccessFile object of the Package
     * @return UName object
     */
    public static UName readNAME(RandomAccessFile f, int length) throws IOException
    {
        String result = "";
        
        /** Error ? */
        if (length < 0)
        {
            System.out.println(">>> WTF?! Length: "+length);
        }
        /** Empty name */
        else if (length == 1)
        {
            result = "";
            /** Skip one byte */
            f.skipBytes(1);
        }
        /** Non-empty name */
        else if (length > 0)
        {
            byte[] b = new byte[length-1];
            f.readFully(b, 0, length-1);
            for (int i = 0; i < length-1; ++i)
                result += (char)b[i];
            f.skipBytes(1);
        }
        else if (length == 0)
        {
            byte[] b = new byte[128];
            for (int i = 0; i < 128; ++i)
            {
                b[i] = f.readByte();
                if (b[i] == 0)
                    break;
                result += (char)b[i];
            }
        }
        return new UName(result);
    }

    /**
     * Write a NAME to an unreal package file
     * @param f RandomAccessFile object of the Package
     * @return true if successful
     */
    public boolean writeNAME(RandomAccessFile f) throws IOException
    {
        byte len = (byte)((0xFF&NAME.length())+1);
        f.writeByte(len);
        for (int i = 0; i < NAME.length(); ++i)
            f.writeByte(NAME.charAt(i));
        f.writeByte(0);
        return true;
    }

    @Override
    public String toString()
    {
        return NAME;
    }

    /**
     * get the size of this NAME in a file
     * @return size of the NAME
     */
    public int getSize()
    {
        return 2+NAME.length();
    }

    /**
     * Change the value of NAME
     * @param newName new NAME
     */
    public void setName(String newName)
    {
        NAME = newName;
    }
}
