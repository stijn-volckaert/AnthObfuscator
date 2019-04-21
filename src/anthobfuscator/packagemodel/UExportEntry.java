/*******************************************************************************
 * AnthObfuscator v1.1 - (c) 2008-2009 AnthraX
 * File : UExportEntry.java
 * Revision History:
 *      Created by AnthraX
 ******************************************************************************/
package anthobfuscator.packagemodel;

import anthobfuscator.datatypes.UDword;
import anthobfuscator.datatypes.UIndex;

/**
 * Export Table Entry Object
 */
public class UExportEntry
{
    /** Class of the Object. See "object references". */
    private UIndex CLASS;

    /** Parent of the Object (from which it inherits). See "object references". */
    private UIndex SUPER;

    /** Package this Object resides in. Could be an internal package (a group). See "object references". */
    private UDword PACKAGE;

    /** The Object name. It's an index into the Name Table. */
    private UIndex NAME;

    /** See “Object Flags */
    private UDword FLAGS;

    /** Size of the object inside the file. */
    private UIndex SIZE;

    /** Offset of the object inside the file. This field only exists if SIZE>0 */
    private UIndex OFFSET;

    /** Exported Data. Not part of the actual Export table. This is the data pointed to by OFFSET */
    private byte DATA[];

    /** Object Header **/
    public UIndex STATEFRAMENODE;
    public UIndex STATEFRAMESTATENODE;
    private UDword STATEFRAMEPROBEMASK1, STATEFRAMEPROBEMASK2;
    private UDword STATEFRAMELATENTACTION;
    private UIndex STATEFRAMEOFFSET;

    /**
     * Constructor          
     * @param eCLASS Class of the Object. See "object references".
     * @param eSUPER Parent of the Object (from which it inherits). See "object references".
     * @param ePACKAGE Package this Object resides in. Could be an internal package (a group). See "object references".
     * @param eNAME The Object name. It's an index into the Name Table.
     * @param eFLAGS See "Object Flags"
     * @param eSIZE Size of the object inside the file.
     * @param eOFFSET Offset of the object inside the file. This field only exists if SIZE>0
     * @param eDATA Exported Data. Not part of the actual Export table. This is the data pointed to by OFFSET
     */
    public UExportEntry (UIndex eCLASS, UIndex eSUPER, UDword ePACKAGE, UIndex eNAME, UDword eFLAGS, UIndex eSIZE, UIndex eOFFSET, byte eDATA[])
    {                
        CLASS = eCLASS;
        SUPER = eSUPER;
        PACKAGE = ePACKAGE;
        NAME = eNAME;
        FLAGS = eFLAGS;
        SIZE = eSIZE;
        OFFSET = eOFFSET;
        DATA = eDATA;
    }

    /**
     * @return the PACKAGE
     */
    public UDword getPACKAGE()
    {
        return PACKAGE;
    }

    /**
     * @param PACKAGE the PACKAGE to set
     */
    public void setPACKAGE(UDword PACKAGE)
    {
        this.PACKAGE = PACKAGE;
    }

    /**
     * @return the FLAGS
     */
    public UDword getFLAGS()
    {
        return FLAGS;
    }

    /**
     * @param FLAGS the FLAGS to set
     */
    public void setFLAGS(UDword FLAGS)
    {
        this.FLAGS = FLAGS;
    }

    /**
     * @return the CLASS
     */
    public UIndex getCLASS()
    {
        return CLASS;
    }

    /**
     * @param CLASS the CLASS to set
     */
    public void setCLASS(UIndex CLASS)
    {
        this.CLASS = CLASS;
    }

    /**
     * @return the SUPER
     */
    public UIndex getSUPER()
    {
        return SUPER;
    }

    /**
     * @param SUPER the SUPER to set
     */
    public void setSUPER(UIndex SUPER)
    {
        this.SUPER = SUPER;
    }

    /**
     * @return the NAME
     */
    public UIndex getNAME()
    {
        return NAME;
    }

    /**
     * @param NAME the NAME to set
     */
    public void setNAME(UIndex NAME)
    {
        this.NAME = NAME;
    }

    /**
     * @return the SIZE
     */
    public UIndex getSIZE()
    {
        return SIZE;
    }

    /**
     * @param SIZE the SIZE to set
     */
    public void setSIZE(UIndex SIZE)
    {
        this.SIZE = SIZE;
    }

    /**
     * @return the OFFSET
     */
    public UIndex getOFFSET()
    {
        return OFFSET;
    }

    /**
     * @param OFFSET the OFFSET to set
     */
    public void setOFFSET(UIndex OFFSET)
    {
        this.OFFSET = OFFSET;
    }

    /**
     * @return the DATA
     */
    public byte[] getDATA()
    {
        return DATA;
    }

    /**
     * @param DATA the DATA to set
     */
    public void setDATA(byte[] DATA)
    {
        this.DATA = DATA;
    }
}
