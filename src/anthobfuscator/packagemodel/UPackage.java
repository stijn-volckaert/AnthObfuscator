/*******************************************************************************
 * AnthObfuscator v1.1 - (c) 2008-2009 AnthraX
 * File : UPackage.java
 * Revision History:
 *      Created by AnthraX
 ******************************************************************************/
package anthobfuscator.packagemodel;

import anthobfuscator.datatypes.UDword;
import anthobfuscator.datatypes.UWord;
import java.util.ArrayList;

/**
 * Unreal Package Model
 * This is where all the parsed data is stored. 
 */
public class UPackage
{
    /** Header info */
    public UDword SIGNATURE;
	public UWord VERSION, LICENSE_MODE;
	public UDword PACKAGE_FLAGS;
	public UDword NAMES_CNT, NAME_OFFSET, EXPORTS_CNT, EXPORT_OFFSET, IMPORTS_CNT, IMPORT_OFFSET, GENERATIONS_CNT;
        public UDword HERITAGE_COUNT, HERITAGE_OFFSET;
	public byte FILE_GUID[] = new byte[16];
    
    /** Generations info */
    public ArrayList<UGenerationEntry> GENERATIONS;

    /** Import info */
    public ArrayList<UImportEntry> IMPORTS;

    /** Export info */
    public ArrayList<UExportEntry> EXPORTS;

    /** Name info */
    public ArrayList<UNameEntry> NAMES;

    // <editor-fold desc="Package Flags">
    /** Allow downloading package */
    public long PKG_AllowDownload = 0x0001;

    /** Purely optional for clients */
    public long PKG_ClientOptional = 0x0002;

    /** Only needed on the server side */
    public long PKG_ServerSideOnly = 0x0004;

    /** Loaded from linker with broken import links */
    public long PKG_BrokenLinks = 0x0008;

    /** Not trusted */
    public long PKG_Unsecure = 0x0010;

    /** Client needs to download this package */
    public long PKG_Need = 0x8000;
    // </editor-fold>
    // <editor-fold desc="Package Flags List">
    public long lngPackageFlags[] = {PKG_AllowDownload,PKG_ClientOptional,PKG_ServerSideOnly,PKG_BrokenLinks,PKG_Unsecure,PKG_Need};
    public String strPackageFlags[] = {"PKG_AllowDownload","PKG_ClientOptional","PKG_ServerSideOnly","PKG_BrokenLinks","PKG_Unsecure","PKG_Need"};
    // </editor-fold>

    // <editor-fold desc="Object Flags">
    /** Object is transactional. */
    public long RF_Transactional = 0x00000001;

    /** Object is not reachable on the object graph. */
    public long RF_Unreachable = 0x00000002;

    /** Object is visible outside its package. */
    public long RF_Public = 0x00000004;

    /** Temporary import tag in load/save. */
    public long RF_TagImp = 0x00000008;

    /** Temporary export tag in load/save. */
    public long RF_TagExp = 0x00000010;

    /** Modified relative to source files. */
    public long RF_SourceModified = 0x00000020;

    /** Check during garbage collection. */
    public long RF_TagGarbage = 0x00000040;

    /** During load, indicates object needs loading. */
    public long RF_NeedLoad = 0x00000200;

    /**  A hardcoded name which should be syntaxhighlighted. */
    public long RF_HighlightedName = 0x00000400;

    /** In a singular function. */
    public long RF_InSingularFunc = 0x00000800;

    /** Suppressed log name. */
    public long RF_Suppress = 0x00001000;

    /** Within an EndState call. */
    public long RF_InEndState = 0x00002000;

    /** Don't save object. */
    public long RF_Transient = 0x00004000;

    /** Data is being preloaded from file. */
    public long RF_PreLoading = 0x00008000;

    /** In-file load for client. */
    public long RF_LoadForClient = 0x00010000;

    /** In-file load for client. */
    public long RF_LoadForServer = 0x00020000;

    /** In-file load for client. */
    public long RF_LoadForEdit = 0x00040000;

    /** Keep object around for editing even if unreferenced. */
    public long RF_Standalone = 0x00080000;

    /** Don't load this object for the game client. */
    public long RF_NotForClient = 0x00100000;

    /** Don't load this object for the game server. */
    public long RF_NotForServer = 0x00200000;

    /** Don't load this object for the editor. */
    public long RF_NotForEdit = 0x00400000;

    /** Object Destroy has already been called. */
    public long RF_Destroyed = 0x00800000;

    /** Object needs to be postloaded. */
    public long RF_NeedPostLoad = 0x01000000;

    /** Has execution stack. */
    public long RF_HasStack = 0x02000000;

    /** Native (UClass only). */
    public long RF_Native = 0x04000000;

    /** Marked (for debugging). */
    public long RF_Marked = 0x08000000;

    /** ShutdownAfterError called. */
    public long RF_ErrorShutdown = 0x10000000;

    /** For debugging Serialize calls. */
    public long RF_DebugPostLoad = 0x20000000;

    /** For debugging Serialize calls. */
    public long RF_DebugSerialize = 0x40000000;

    /** For debugging Destroy calls. */
    public long RF_DebugDestroy = 0x80000000;
    // </editor-fold>
    // <editor-fold desc="Object Flags List">
    public long lngObjectFlags[] = {RF_Transactional, RF_Unreachable, RF_Public, RF_TagImp, RF_TagExp, RF_SourceModified, RF_TagGarbage,
    RF_NeedLoad, RF_HighlightedName, RF_InSingularFunc, RF_Suppress, RF_InEndState, RF_Transient, RF_PreLoading, RF_LoadForClient,
    RF_LoadForServer, RF_LoadForEdit, RF_Standalone, RF_NotForClient, RF_NotForServer, RF_NotForEdit, RF_Destroyed, RF_NeedPostLoad,
    RF_HasStack, RF_Native, RF_Marked, RF_ErrorShutdown, RF_DebugPostLoad, RF_DebugSerialize, RF_DebugDestroy};

    public String strObjectFlags[] = {"RF_Transactional", "RF_Unreachable", "RF_Public", "RF_TagImp", "RF_TagExp", "RF_SourceModified", "RF_TagGarbage",
    "RF_NeedLoad", "RF_HighlightedName", "RF_InSingularFunc", "RF_Suppress", "RF_InEndState", "RF_Transient", "RF_PreLoading", "RF_LoadForClient",
    "RF_LoadForServer", "RF_LoadForEdit", "RF_Standalone", "RF_NotForClient", "RF_NotForServer", "RF_NotForEdit", "RF_Destroyed", "RF_NeedPostLoad",
    "RF_HasStack", "RF_Native", "RF_Marked", "RF_ErrorShutdown", "RF_DebugPostLoad", "RF_DebugSerialize", "RF_DebugDestroy"};
    // </editor-fold>

    /**
     * Add a UGenerationEntry to the Generations table
     * @param e UGenerationEntry object of the Generation
     * @return true if successful
     */
    public boolean addGeneration(UGenerationEntry e)
    {
        if (GENERATIONS == null)
            GENERATIONS = new ArrayList<UGenerationEntry>();
        return GENERATIONS.add(e);
    }

    /**
     * Add a UNameEntry to the Name table
     * @param e UNameEntry object of the name
     * @return true if successful
     */
    public boolean addName(UNameEntry e)
    {
        if (NAMES == null)
            NAMES = new ArrayList<UNameEntry>();
        return NAMES.add(e);
    }
    
    /**
     * Add a UExportEntry to the Export table
     * @param e UExportEntry object of the export
     * @return true if successful
     */
    public boolean addExport(UExportEntry e)
    {
        if (EXPORTS == null)
            EXPORTS = new ArrayList<UExportEntry>();
        return EXPORTS.add(e);
    }

    /**
     * Add a UImportEntry to the Import table
     * @param e UImportEntry object of the import
     * @return true if successful
     */
    public boolean addImport(UImportEntry e)
    {
        if (IMPORTS == null)
            IMPORTS = new ArrayList<UImportEntry>();
        return IMPORTS.add(e);
    }   

    /**     
     * @return Package Flags in String form ("FLAG1|FLAG2|..." format)
     */
    public String getPackageFlagsString()
    {
        long l = PACKAGE_FLAGS.getLongValue();
        String result = "";
        for (int i = 0; i < lngPackageFlags.length; ++i)
        {
            if ((l & lngPackageFlags[i]) != 0x0)
            {
                if (!result.equals(""))
                    result+="|";
                result+=strPackageFlags[i];
            }
        }
        return result;
    }

    /**
     * Generate an Object Flags string for the specified object
     * @param objectFlags Object Flags DWORD
     * @return Object Flags in String form ("FLAG1|FLAG2|..." format)
     */
    public String getObjectFlagsString(UDword objectFlags)
    {
        long l = objectFlags.getLongValue();
        String result = "";
        for (int i = 0; i < lngObjectFlags.length; ++i)
        {
            if ((l & lngObjectFlags[i]) != 0x0)
            {
                if (!result.equals(""))
                    result+="|";
                result+=strObjectFlags[i];
            }
        }
        return result;
    }

    /**
     * @return Package GUID in String form
     */
    public String getGUIDString()
    {
        return getGUIDChar(3)+getGUIDChar(2)+getGUIDChar(1)+getGUIDChar(0)+"-"
                +getGUIDChar(7)+getGUIDChar(6)+getGUIDChar(5)+getGUIDChar(4)+"-"
                +getGUIDChar(11)+getGUIDChar(10)+getGUIDChar(9)+getGUIDChar(8)+"-"
                +getGUIDChar(15)+getGUIDChar(14)+getGUIDChar(13)+getGUIDChar(12);
    }

    /**
     * get the specified GUID char in hexstring form (2 bytes long)
     * @param i position of the guid char in array
     * @return GUID char in hexstring form
     */
    private String getGUIDChar(int i)
    {
        int a = (int)FILE_GUID[i]&0x000000FF;
        String result = Integer.toHexString(a);
        /** Pad if needed */
        while (result.length() < 2)
            result = "0"+result;
        return result;
    }
}
