/*******************************************************************************
 * AnthObfuscator v1.1 - (c) 2008-2009 AnthraX
 * File : UImportEntry.java
 * Revision History:
 *      Created by AnthraX
 ******************************************************************************/
package anthobfuscator.packagemodel;

import anthobfuscator.datatypes.UDword;
import anthobfuscator.datatypes.UIndex;

/**
 * Import Table Entry Object
 */
public class UImportEntry
{
    /** Package of the Class. It’s an index into the Name Table. */
    private UIndex CLASSPACKAGE;

    /** The Class of the Object. It’s an index into the Name Table. */
    private UIndex CLASSNAME;

    /** The Package this object resides in. See “object references”. */
    private UDword PACKAGE;

    /** The Object name. It’s an index into the Name Table. */
    private UIndex OBJECTNAME;

    /**
     * Constructor
     * @param iCLASSPACKAGE Package of the Class. It’s an index into the Name Table.
     * @param iCLASSNAME The Class of the Object. It’s an index into the Name Table.
     * @param iPACKAGE The Package this object resides in. See “object references”.
     * @param iOBJECTNAME The Object name. It’s an index into the Name Table.
     */
    public UImportEntry(UIndex iCLASSPACKAGE, UIndex iCLASSNAME, UDword iPACKAGE, UIndex iOBJECTNAME)
    {
        CLASSPACKAGE = iCLASSPACKAGE;
        CLASSNAME = iCLASSNAME;
        PACKAGE = iPACKAGE;
        OBJECTNAME = iOBJECTNAME;
    }

    /**
     * @return the CLASSPACKAGE
     */
    public UIndex getCLASSPACKAGE()
    {
        return CLASSPACKAGE;
    }

    /**
     * @param CLASSPACKAGE the CLASSPACKAGE to set
     */
    public void setCLASSPACKAGE(UIndex CLASSPACKAGE)
    {
        this.CLASSPACKAGE = CLASSPACKAGE;
    }

    /**
     * @return the CLASSNAME
     */
    public UIndex getCLASSNAME()
    {
        return CLASSNAME;
    }

    /**
     * @param CLASSNAME the CLASSNAME to set
     */
    public void setCLASSNAME(UIndex CLASSNAME)
    {
        this.CLASSNAME = CLASSNAME;
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
     * @return the OBJECTNAME
     */
    public UIndex getOBJECTNAME()
    {
        return OBJECTNAME;
    }

    /**
     * @param OBJECTNAME the OBJECTNAME to set
     */
    public void setOBJECTNAME(UIndex OBJECTNAME)
    {
        this.OBJECTNAME = OBJECTNAME;
    }
}
