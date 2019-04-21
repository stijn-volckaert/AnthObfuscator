/*******************************************************************************
 * AnthObfuscator v1.1 - (c) 2008-2009 AnthraX
 * File : UGenerationEntry.java
 * Revision History:
 *      Created by AnthraX
 ******************************************************************************/
package anthobfuscator.packagemodel;

import anthobfuscator.datatypes.UDword;

/** Generation entry in the package header */
public class UGenerationEntry
{    
    private UDword EXPORTS, NAMES;

    /**
     * Constructor
     * @param gEXPORTS EXPORTS
     * @param gNAMES NAMES
     */
    public UGenerationEntry (UDword gEXPORTS, UDword gNAMES)
    {
        EXPORTS = gEXPORTS;
        NAMES = gNAMES;
    }

    /**
     * @return the EXPORTS
     */
    public UDword getEXPORTS()
    {
        return EXPORTS;
    }

    /**
     * @param EXPORTS the EXPORTS to set
     */
    public void setEXPORTS(UDword EXPORTS)
    {
        this.EXPORTS = EXPORTS;
    }

    /**
     * @return the NAMES
     */
    public UDword getNAMES()
    {
        return NAMES;
    }

    /**
     * @param NAMES the NAMES to set
     */
    public void setNAMES(UDword NAMES)
    {
        this.NAMES = NAMES;
    }
    
    @Override
    public String toString()
    {
        return EXPORTS+" - "+NAMES;
    }
}
