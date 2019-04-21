/*******************************************************************************
 * AnthObfuscator v1.1 - (c) 2008-2009 AnthraX
 * File : UNameEntry.java
 * Revision History:
 *      Created by AnthraX
 ******************************************************************************/
package anthobfuscator.packagemodel;

import anthobfuscator.datatypes.UDword;
import anthobfuscator.datatypes.UName;

/**
 * Name Table Entry Object
 */
public class UNameEntry
{
    /** Name of the Object */
    private UName NAME;

    /** See “Object Flags” */
    private UDword FLAGS;

    /**
     * Constructor
     * @param nNAME Name of the Object
     * @param nFLAGS See “Object Flags”
     */
    public UNameEntry (UName nNAME, UDword nFLAGS)
    {
        NAME = nNAME;
        FLAGS = nFLAGS;
    }

    /**
     * @return the NAME
     */
    public UName getNAME()
    {
        return NAME;
    }

    /**
     * @param NAME the NAME to set
     */
    public void setNAME(UName NAME)
    {
        this.NAME = NAME;
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
}
