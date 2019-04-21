/*******************************************************************************
 * AnthObfuscator v1.1 - (c) 2008-2009 AnthraX
 * File : Obfuscator.java
 * Revision History:
 *      Created by AnthraX
 *      Fixed non-native childs in NPLoader classes
 ******************************************************************************/
package anthobfuscator;

import anthobfuscator.datatypes.*;
import anthobfuscator.packagemodel.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.sun.org.apache.xml.internal.security.utils.UnsyncByteArrayOutputStream;

/** The Obfuscator Class! */
public class Obfuscator
{
    /** File name of the input file */
    private String fileName;

    /** File name of the output file */
    private String newFileName;

    /** File name of the log file */
    private String logFileName;

    /** File size of the input file */
    private long fileSize;

    /** File size of the output file */
    private long newFileSize;

    /** RandomAccessFile object of the input file */
    private RandomAccessFile f;

    /** RandomAccessFile object of the output file */
    private RandomAccessFile o;

    /** Printwriter object of the logfile */
    private PrintWriter logFile;

    /** UPackage object of the input file */
    private UPackage p;

    /** Export class pointers - import/export object reference */
    private HashMap<UExportEntry,Object> exportClasses;

    /** Export superclass pointers - import/export object reference */
    private HashMap<UExportEntry,Object> exportSuperClasses;

    /** Export package pointers - import/export object reference */
    private HashMap<UExportEntry,Object> exportPackages;

    /** Export name pointers - name reference */
    private HashMap<UExportEntry,UNameEntry> exportNames;

    /** Import classpackage pointers - name reference */
    private HashMap<UImportEntry,UNameEntry> importClassPackages;

    /** Import classname pointers - name reference */
    private HashMap<UImportEntry,UNameEntry> importClassNames;

    /** Import package pointers - import/export object reference */
    private HashMap<UImportEntry,Object> importPackages;

    /** Import name pointers - name reference */
    private HashMap<UImportEntry,UNameEntry> importNames;

    /** BOGUS_NAME, used for obfuscation */
    private int BOGUS_NAME = 0x0100;
    
    /**
     * Start the obfuscator
     * @param file File name of the input file
     * @param stripSource If true, remove the sourcecode from the package
     * @param obfuscateNames If true, obfuscate the names with zz,xx,yy prefixes
     * @param fixNPLClasses If true, remove RF_LoadForServer flag in the native objects of the file
     * @param randomizeExports If true, shuffle the exports list
     */
    public Obfuscator (String file, boolean stripSource, boolean obfuscateNames, boolean fixNPLClasses, boolean randomizeExports, boolean deObfuscate, boolean fixNodes)
    {
        logln("===========================================");
        logln("AnthObfuscator v1.4 - (c) 2008-2009 AnthraX");
        logln("===========================================\n");

        fileName = file;
        try
        {
            f = new RandomAccessFile(fileName,"r");
            fileSize = f.length();
            p = new UPackage();

            // Log basic info
            logln("File Name: "+fileName);
            logln("File Size: "+fileSize+" bytes");

            // Open logfile
            log("\n* Opening Log File: ");
            openLogFile();
            log("DONE!\n");

            // Read package header
            log("* Reading Package Header: ");
            readPackageHeader();
            log("DONE!\n");

            // Print package header
            printPackageHeader();

            // Read name table
            log("* Reading Name Table: ");
            readNameTable();
            log("DONE!\n");

            // Print name table
            printNameTable();

             // Read import table
            log("* Reading Import Table: ");
            readImportTable();
            log("DONE!\n");

            // Print import table
            //printExportTable();
            printImportTable();

            // Read export table
            log("* Reading Export Table: ");
            readExportTable(true, true);
            log("DONE!\n");

            // Print export table
            printExportTable();

            // Build object reference tables
            log("* Building Object Reference Tables: ");
            getReferences();
            log("DONE!\n");

            // Strip source
            if (stripSource)
            {
                log("* Stripping Source: ");
                /*stripSource();
                log("DONE!\n");*/
                log("DISABLED!\n");
            }

            // Fix NPLoader flags
            if (fixNPLClasses)
            {
                log("* Fixing NPLoader flags: ");
                fixNPLClasses();
                log("DONE!\n");
            }

            // Obfuscate!
            if (obfuscateNames)
            {
                log("* Obfuscating names: ");
                log("DISABLED!\n");
            }

            // Randomize exports
            if (randomizeExports)
            {
                log("* Randomizing exports: ");
                log("DISABLED!\n");
            }

            // Deobfuscate
            if (deObfuscate)
            {
                log("* Deobfuscating: ");
                log("DISABLED!\n");
            }

            log("* Fixing Object References: ");
            fixReferences();
            log("DONE!\n");

            // Calculate new filesize
            log("* Calculating New Filesize and Offsets: ");
            calculateFileSize();
            log("DONE!\n");

            // Open output file
            log("* Opening Output File: ");
            openOutputFile();
            log("DONE!\n");

            // Write new header
            log("* Writing Package Header: ");
            writePackageHeader();
            log("DONE!\n");

            // Write new exportdata
            log("* Writing Export Data: ");
            writeExportData();
            log("DONE!\n");

            // Write new nametable
            log("* Writing Name Table: ");
            writeNameTable();
            log("DONE!\n");

            // Write new importtable
            log("* Writing Import Table: ");
            writeImportTable();
            log("DONE!\n");

            // Write new exporttable
            log("* Writing Export Table: ");
            writeExportTable();
            log("DONE!\n");

            // Close the files
            log("* Closing: ");
            if (f != null)
                f.close();
            if (o != null)
                o.close();
            log("DONE!\n");
        }
        catch (FileNotFoundException e)
        {
            logln("Error: Couldn't find file "+fileName);
            return;
        }
        catch (IOException e)
        {
            logln("Error: Couldn't read from file "+fileName);
            return;
        }
    }

    /**
     * See @log(str)
     */
    private void logln (String str)
    {
        log(str+"\n",0);
    }

    /**
     * See @log(str,lvl)
     */
    private void logln (String str,int lvl)
    {
        log(str+"\n",lvl);
    }

    /**
     * Log a string to the console/logfile.
     * @param str The String
     */
    private void log (String str)
    {
        log(str,0);
    }

    /**
     * Broadcast a string to all event loggers
     * @param str The string
     * @param lvl Debug level (0=always show,1=only show when debugging)
     */
    private void log (String str, int lvl)
    {
        if (lvl == 0)
            System.out.print(str);
        if (logFile != null)
        {
            logFile.flush();
            logFile.print(str);
        }
    }

    /**
     * Read the package header of the input file
     * @throws java.io.IOException
     */
    private void readPackageHeader() throws IOException
    {
        p.SIGNATURE = UDword.readDWORD(f);
        UDword packageVersion = UDword.readDWORD(f);
        p.LICENSE_MODE = packageVersion.getHigherWord();
        p.VERSION = packageVersion.getLowerWord();
        p.PACKAGE_FLAGS = UDword.readDWORD(f);
        p.NAMES_CNT = UDword.readDWORD(f);
        p.NAME_OFFSET = UDword.readDWORD(f);
        p.EXPORTS_CNT = UDword.readDWORD(f);
        p.EXPORT_OFFSET = UDword.readDWORD(f);
        p.IMPORTS_CNT = UDword.readDWORD(f);
        p.IMPORT_OFFSET = UDword.readDWORD(f);
        /*if (p.VERSION.getIntValue() < 68)
        {
            logln("Error: Packageversion < 68 - Aborting");
            throw new RuntimeException("Packageversion < 68");
        }*/
        if (p.VERSION.getIntValue() >= 68)
        {
            f.read(p.FILE_GUID, 0, 16);
            p.GENERATIONS_CNT = UDword.readDWORD(f);
            for (long i = 0; i < p.GENERATIONS_CNT.getLongValue(); ++i)
            {
                UDword e,n;
                e = UDword.readDWORD(f);
                n = UDword.readDWORD(f);
                p.addGeneration(new UGenerationEntry(e,n));
            }
        }
        else
        {
            p.HERITAGE_COUNT    = UDword.readDWORD(f);
            p.HERITAGE_OFFSET   = UDword.readDWORD(f);
            f.seek(p.HERITAGE_OFFSET.getLongValue());
            f.read(p.FILE_GUID, 0, 16);
        }
    }

    /**
     * Read the name table
     */
    private void readNameTable() throws IOException
    {
        if (p.NAME_OFFSET == null)
        {
            logln("Error: Nametable not found - Aborting");
            throw new RuntimeException("Nametable not found");
        }

        // Move file pointer
        f.seek(p.NAME_OFFSET.getLongValue());

        for (long l = 0; l < p.NAMES_CNT.getLongValue(); ++l)
        {
            UName n = null;
            UDword flags = null;
            long len = 0;
            if (p.VERSION.getIntValue() > 68)
                len = UIndex.readINDEX(f).getLongValue();            
            n = UName.readNAME(f, (int)len);
            flags = UDword.readDWORD(f);
            UNameEntry ne = new UNameEntry(n,flags);
            p.addName(ne);
        }
    }

    /**
     * Read the export table
     */
    private void readExportTable(boolean fixNodes, boolean fixTextures) throws IOException
    {
        long saveoffset;

        if (p.EXPORT_OFFSET == null)
        {
            logln("Error: Exporttable not found - Aborting");
            throw new RuntimeException("Exporttable not found");
        }

        // Move to export table
        f.seek(p.EXPORT_OFFSET.getLongValue());

        for (long l = 0; l < p.EXPORTS_CNT.getLongValue(); ++l)
        {
            UIndex iClass = null;
            UIndex iSuper = null;
            UDword dPackage = null;
            UIndex iObjectName = null;
            UDword dObjectFlags = null;
            UIndex iSerialSize = null;
            UIndex iSerialOffset = null;
            UIndex iNode = null;
            UIndex iStateNode = null;
            byte[] bData = null;

            iClass = UIndex.readINDEX(f);
            iSuper = UIndex.readINDEX(f);
            dPackage = UDword.readDWORD(f);
            iObjectName = UIndex.readINDEX(f);
            dObjectFlags = UDword.readDWORD(f);
            iSerialSize = UIndex.readINDEX(f);
            if (iSerialSize.getLongValue() > 0)
            {
                iSerialOffset = UIndex.readINDEX(f);                
                saveoffset = f.getFilePointer();                
                f.seek(iSerialOffset.getLongValue());             

                if (fixNodes && (dObjectFlags.getLongValue() & p.RF_HasStack) > 0)
                {
                    iNode = UIndex.readINDEX(f);
                    long saveoffset2 = f.getFilePointer();
                    int curpos   = 0;
                    iStateNode = UIndex.readINDEX(f);

                    if (iStateNode.getLongValue() != 0)
                    {
                        Object obj = getObjectRef((int)iStateNode.getLongValue());
                        if (obj == null || iStateNode.getSize() > 3)
                        {
                            logln("\nError: Invalid statenode for export: "+l);
                            logln("Error: Restoring node...");
                            iStateNode.setLongValue(iNode.getLongValue());
                            iSerialSize.setLongValue(iSerialSize.getLongValue()+iStateNode.getSize()-1);
                            f.seek(saveoffset2+1);
                            bData = new byte[(int)iSerialSize.getLongValue()];

                            byte[] bytes = {iNode.A, iNode.B, iNode.C, iNode.D, iNode.E};

                            for (int i = 0; i < iNode.getSize(); ++i)
                                bData[curpos++] = bytes[i];
                            byte[] bytes2 = {iStateNode.A, iStateNode.B, iStateNode.C, iStateNode.D, iStateNode.E};
                            for (int i = 0; i < iStateNode.getSize(); ++i)
                                bData[curpos++] = bytes2[i];

                            f.readFully(bData, curpos, (int)iSerialSize.getLongValue()-iNode.getSize()-iStateNode.getSize());
                        }
                    }

                    if (bData == null)
                    {
                        f.seek(iSerialOffset.getLongValue());
                        bData = new byte[(int)iSerialSize.getLongValue()];
                        f.readFully(bData, 0, (int)iSerialSize.getLongValue());
                    }
                    
                }
                else if (fixTextures && getNameByObjectRef(iClass.getLongValue()).getNAME().toString().equals("Texture"))
                {
                	f.seek(iSerialOffset.getLongValue());
                	bData = new byte[(int)iSerialSize.getLongValue()];
                    f.readFully(bData, 0, (int)iSerialSize.getLongValue());
                    f.seek(iSerialOffset.getLongValue());
                    
                    logln("Reading texture: " + p.NAMES.get((int) iObjectName.getLongValue()).getNAME());
                    
                    // extract the texture data... Properties first
                    while (true)
                    {
                    	UIndex propertyName = UIndex.readINDEX(f);  
                    	
                    	logln("Read property: " + p.NAMES.get((int) propertyName.getLongValue()).getNAME());
                    	
                    	if (p.NAMES.get((int) propertyName.getLongValue()).getNAME().toString().equals("None"))
                    		break;
                    	
                    	byte propertyType = f.readByte();
                    	int propertySize = (propertyType & 0x70) >> 4;
                        boolean isArray = ((propertyType & 0x80) == 0x80) && ((propertyType & 0x0F) != 3);
                        propertyType = (byte)(propertyType & 0x0F);
                        
                        if (isArray)
                        {
                        	byte indexByte = f.readByte();
                        	
                        	// if index >= 128 and < 16384, the most significant byte is OR-ed with 0x80
                        	if ((indexByte & 0x80) == 0x80)
                        		f.seek(f.getFilePointer()+1);
                        	// if index >= 16384, the most significant byte is OR-ed with 0xC0
                        	else if ((indexByte & 0xC0) == 0xC0)
                        		f.seek(f.getFilePointer()+3);
                        }
                        
                        // Undocumented feature!!! if the propertyType is StructProperty, 
                        // the propertyType byte is followed by an index into the nametable (points to the struct type)
                        UIndex dummy;                        
						
                        switch (propertyType)
                        {
                        	case 5: // ObjectProperty
                        	case 6: // NameProperty
                        		dummy = UIndex.readINDEX(f);
                        		break;                        	
                        	case 13: // StrProperty
                        		dummy = UIndex.readINDEX(f);
                        		f.seek(f.getFilePointer()+dummy.getLongValue());
                        		break;
                        	case 8: // ClassProperty
                        	case 10: // StructProperty
                        		dummy = UIndex.readINDEX(f);
                        	default:
                        		switch(propertySize)
                                {
                                	case 0: f.seek(f.getFilePointer()+1); break;
                                	case 1: f.seek(f.getFilePointer()+2); break;
                                	case 2: f.seek(f.getFilePointer()+4); break;
                                	case 3: f.seek(f.getFilePointer()+12); break;
                                	case 4: f.seek(f.getFilePointer()+16); break;
                                	case 5: f.seek(f.getFilePointer()+f.readByte()+1); break;
                                	case 6: f.seek(f.getFilePointer()+f.readShort()+2); break;
                                	case 7: f.seek(f.getFilePointer()+f.readInt()+4); break;
                                }
                        		break;
                        }
                    }
                    
                    byte mipMapCount = f.readByte();
                    
                    for (int i = 0; i < mipMapCount; ++i)
                    {
	                    UDword widthOffset = (p.VERSION.getIntValue() >= 63) ? UDword.readDWORD(f) : new UDword((byte)0,(byte)0,(byte)0,(byte)0);
	                    UIndex mipMapSize = UIndex.readINDEX(f);
	                    byte mipMapData [] = new byte[(int) mipMapSize.getLongValue()];
	                    f.readFully(mipMapData, 0, (int)mipMapSize.getLongValue());
	                    	                    
	                	if ((char)mipMapData[0] == 'P'
	                			&& (char)mipMapData[1] == 'V'
	                			&& (char)mipMapData[2] == 'R')
	                	{
	                		logln("This export is a PowerVR compressed texture!");
	                		Process p = Runtime.getRuntime().exec("PVRTDecompress");
	                	}
                    }
                }
                else
                {
                    bData = new byte[(int)iSerialSize.getLongValue()];
                    f.readFully(bData, 0, (int)iSerialSize.getLongValue());
                }
                /*for (int i = 0; i < bData.length; ++i)
                    sData += (char)bData[i];*/
                f.seek(saveoffset);
            }            
            UExportEntry ee = new UExportEntry(iClass,iSuper,dPackage,iObjectName,dObjectFlags,iSerialSize,iSerialOffset,bData);
            ee.STATEFRAMENODE       = iNode;
            ee.STATEFRAMESTATENODE  = iStateNode;
            p.addExport(ee);
        }        
    }

    /**
     * Read the import table
     */
    private void readImportTable() throws IOException
    {
        if (p.IMPORT_OFFSET == null)
        {
            logln("Error: Importtable not found - Aborting");
            throw new RuntimeException("Importtable not found");
        }

        // Move to import table
        f.seek(p.IMPORT_OFFSET.getLongValue());

        for (long l = 0; l < p.IMPORTS_CNT.getLongValue(); ++l)
        {
            UIndex iClassPackage = null;
            UIndex iClassName = null;
            UDword dPackage = null;
            UIndex iObjectName = null;
            iClassPackage = UIndex.readINDEX(f);
            iClassName = UIndex.readINDEX(f);
            dPackage = UDword.readDWORD(f);
            iObjectName = UIndex.readINDEX(f);
            UImportEntry ie = new UImportEntry(iClassPackage,iClassName,dPackage,iObjectName);
            p.addImport(ie);
        }
    }

    /**
     * Build tables to directly access referenced objects. Needs to be done before modifying import/export/name tables!
     */
    private void getReferences()
    {
        UExportEntry ee;
        UImportEntry ie;

        exportClasses = new HashMap<UExportEntry,Object>();
        exportSuperClasses = new HashMap<UExportEntry,Object>();
        exportPackages = new HashMap<UExportEntry,Object>();
        exportNames = new HashMap<UExportEntry,UNameEntry>();
        importClassPackages = new HashMap<UImportEntry,UNameEntry>();
        importClassNames = new HashMap<UImportEntry,UNameEntry>();
        importPackages = new HashMap<UImportEntry,Object>();
        importNames = new HashMap<UImportEntry,UNameEntry>();

        // Export table
        for (int i = 0; i < p.EXPORTS_CNT.getLongValue(); ++i)
        {
            ee = p.EXPORTS.get(i);

            exportClasses.put(ee, getObjectRef((int)ee.getCLASS().getLongValue()));
            exportSuperClasses.put(ee, getObjectRef((int)ee.getSUPER().getLongValue()));
            exportPackages.put(ee, getObjectRef((int)ee.getPACKAGE().getLongValue()));
            exportNames.put(ee, p.NAMES.get((int)ee.getNAME().getLongValue()));
        }

        // Import table
        for (int i = 0; i < p.IMPORTS_CNT.getLongValue(); ++i)
        {
            ie = p.IMPORTS.get(i);

            importClassPackages.put(ie, p.NAMES.get((int)ie.getCLASSPACKAGE().getLongValue()));
            importClassNames.put(ie, p.NAMES.get((int)ie.getCLASSNAME().getLongValue()));
            importPackages.put(ie, getObjectRef((int)ie.getPACKAGE().getLongValue()));
            importNames.put(ie, p.NAMES.get((int)ie.getOBJECTNAME().getLongValue()));
        }
    }

    /**
     * Write table info back to packagemodel. Done after modifying import/export/name tables
     */
    private void fixReferences()
    {
        UExportEntry ee;
        UImportEntry ie;

        // Export table
        for (int i = 0; i < p.EXPORTS_CNT.getLongValue(); ++i)
        {
            ee = p.EXPORTS.get(i);

            ee.getCLASS().setLongValue(getObjectIndex(exportClasses.get(ee)));
            ee.getSUPER().setLongValue(getObjectIndex(exportSuperClasses.get(ee)));
            ee.getPACKAGE().setLongValue(getObjectIndex(exportPackages.get(ee)));
            ee.getNAME().setLongValue(getNameIndex(exportNames.get(ee)));
        }

        // Import table
        for (int i = 0; i < p.IMPORTS_CNT.getLongValue(); ++i)
        {
            ie = p.IMPORTS.get(i);

            ie.getCLASSPACKAGE().setLongValue(getNameIndex(importClassPackages.get(ie)));
            ie.getCLASSNAME().setLongValue(getNameIndex(importClassNames.get(ie)));
            ie.getPACKAGE().setLongValue(getObjectIndex(importPackages.get(ie)));
            ie.getOBJECTNAME().setLongValue(getNameIndex(importNames.get(ie)));
        }

        // Generations
        if (p.VERSION.getIntValue() > 68)
        {
            p.GENERATIONS.get((int)p.GENERATIONS_CNT.getLongValue()-1).getEXPORTS().setLongValue(p.EXPORTS_CNT.getLongValue());
            p.GENERATIONS.get((int)p.GENERATIONS_CNT.getLongValue()-1).getNAMES().setLongValue(p.NAMES_CNT.getLongValue());
        }
        else
        {
            p.GENERATIONS_CNT = new UDword((byte)0,(byte)0,(byte)0,(byte)0);
            p.GENERATIONS_CNT.setLongValue(1);
            p.addGeneration(new UGenerationEntry(p.EXPORTS_CNT, p.NAMES_CNT));
        }

        printExportTable();
        printImportTable();
    }

    /**
     * Get an index for this object (might be null, a UExportEntry or a UImportEntry
     */
    private long getObjectIndex(Object o)
    {
        if (UExportEntry.class.isInstance(o))
        {
            // Check if the export entry is still there
            for (int i = 0; i < p.EXPORTS_CNT.getLongValue(); ++i)
            {
                if (p.EXPORTS.get(i) == o)
                    return i+1;
            }

            // Export entry not found => now null
            return 0;
        }
        else if (UImportEntry.class.isInstance(o))
        {
            // Check if the import entry is still there
            for (int i = 0; i < p.IMPORTS_CNT.getLongValue(); ++i)
            {
                if (p.IMPORTS.get(i) == o)
                    return -(i+1);
            }

            // Import entry not found => now null
            return 0;
        }
        else
            return 0;
    }

    /**
     * Find the index of this name in the name table
     */
    private long getNameIndex(UNameEntry ne)
    {
        // Check if the name entry is still there
        for (int i = 0; i < p.NAMES_CNT.getLongValue(); ++i)
        {
            if (p.NAMES.get(i) == ne)
                return i;
        }

        // name entry not found => now null
        return 0;
    }

    /**
     * Object reference by Index
     */
    private Object getObjectRef(int index)
    {
        // 1 -> 0
        if (index > 0 && p.EXPORTS != null && (index-1) < p.EXPORTS.size())
            return p.EXPORTS.get(index-1);
        // -1 -> 0
        else if (index < 0 && p.IMPORTS != null && (-index-1) < p.IMPORTS.size())
            return p.IMPORTS.get(-index-1);
        else
            return null;
    }

    /**
     * Get the UNameEntry object corresponding with an object reference
     * @param index UIndex long value of the Object Reference.
     *      => If it's negative: Index into the import table
     *      => If it's positive: Index into the export table
     * @return UNameEntry object
     */
    private UNameEntry getNameByObjectRef(long index)
    {
        if (index > 0 && p.EXPORTS != null && (index-1) < p.EXPORTS.size())
            return p.NAMES.get((int)p.EXPORTS.get((int)index-1).getNAME().getLongValue());
        else if (index < 0 && p.IMPORTS != null && (-index-1) < p.IMPORTS.size())
            return p.NAMES.get((int)p.IMPORTS.get((int)-index-1).getOBJECTNAME().getLongValue());
        else
            return p.NAMES.get(0);
    }    
    
    /**
     * Untoggle the RF_LoadForServer flag for every native class
     */
    private void fixNPLClasses()
    {
        logln("\nGathering class names:",1);
        for (int i = 0; i < p.EXPORTS_CNT.getLongValue(); ++i)
        {
            UExportEntry ee = p.EXPORTS.get(i);
            // Look for native classes
            if (ee.getCLASS().getLongValue() == 0)
            {                
                UNameEntry objectName = p.NAMES.get((int)ee.getNAME().getLongValue());
                UNameEntry superName = getNameByObjectRef(ee.getSUPER().getLongValue());
                logln("Export "+i+": [class="+objectName.getNAME()+",super="+superName.getNAME()+"]",1);
                /** Check native flag */
                if ((ee.getFLAGS().getLongValue() & p.RF_Native) != 0)
                {
                    /** It's native! untoggle RF_LoadForServer */
                    long notLoadForServer = 0xFFFFFFFF - p.RF_LoadForServer;
                    long newFlags = ee.getFLAGS().getLongValue() & notLoadForServer;
                    logln("   -> Reflagging without RF_LoadForServer",1);
                    ee.getFLAGS().setLongValue(newFlags);

                    // Look for childs and reflag
                    logln("   -> Looking for childs",1);
                    for (int j = 0; j < p.EXPORTS_CNT.getLongValue(); ++j)
                    {
                        UExportEntry child = p.EXPORTS.get(j);

                        if (getObjectRef((int)child.getPACKAGE().getLongValue()) == ee)
                        {
                            UNameEntry childName = p.NAMES.get((int)child.getNAME().getLongValue());
                            logln("   -> Child export "+j+": [class="+childName.getNAME()+"]",1);
                            logln("      -> Reflagging without RF_LoadForServer",1);
                            child.getFLAGS().setLongValue(child.getFLAGS().getLongValue() & notLoadForServer);
                        }
                    }
                }
            }
        }        
    }

    

    /**
     * Get a hex representation of a given string
     * @param inputString the input (non-hex) string
     * @return hex string
     */
    private String getHexString(String inputString)
    {
        String result = "";

        for (int i = 0; i < inputString.length(); ++i)
        {
            result += " 0x"+Integer.toHexString(inputString.charAt(i)).toUpperCase();
        }

        return result;
    }

    /**
     * Shuffle the exports ArrayList
     */
    /**private void randomizeExports()
    {
        ArrayList<UExportEntry> shuffledList = new ArrayList<UExportEntry>();
        Random r = new Random();

        while (p.EXPORTS.size() >= 1)
        {
            int i = r.nextInt(p.EXPORTS.size());
            UExportEntry ee = p.EXPORTS.get(i);
            shuffledList.add(ee);
            p.EXPORTS.remove(ee);
        }

        p.EXPORTS = shuffledList;
    }*/

    /**
     * Open the Output file. Delete the file first if it already exists
     * @throws java.io.FileNotFoundException
     */
    private void openOutputFile() throws FileNotFoundException
    {
        // Generate new file name
        if (fileName.contains(".u"))
            newFileName = fileName.substring(0, fileName.indexOf(".u"));
        else
            newFileName = fileName;
        newFileName += "_obfuscated.u";

        // Delete the file if needed
        File ff = new File(newFileName);
        if (ff.exists())
            ff.delete();
        
        // Open RandomAccessFile for the file
        o = new RandomAccessFile(newFileName,"rw");
    }

    /**
     * Open the Log file. Delete the file first if it already exists
     * @throws java.io.FileNotFoundException
     */
    private void openLogFile() throws FileNotFoundException, IOException
    {
        // Generate new file name
        if (fileName.contains(".u"))
            logFileName = fileName.substring(0, fileName.indexOf(".u"))+".log";
        else
            logFileName = fileName+".log";

        // Delete the file if needed
        File ff = new File(logFileName);
        if (ff.exists())
            ff.delete();

        // Open PrintWriter
        logFile = new PrintWriter(new FileWriter(logFileName));
    }

    /**
     * Calculate the new filesize and the offsets of the headers and data sections!
     * Also updates the offsets in the package model.
     *
     * This MUST be run before writing the file!
     *
     * AnthObfuscator will write the package in this format:
     *
     * offset 0x0:
     *      GLOBAL HEADER
     *
     * offset 0x0+globalHeaderSize:
     *      EXPORT DATA FOR EXPORT 0
     *
     * ...
     *
     * offset 0x0+globalHeaderSize+SUM(export_data_size[x],x=0...n-1):
     *      EXPORT DATA FOR EXPORT N
     *
     * offset 0x0+globalHeaderSize+exportDataSize:
     *      NAME TABLE
     *
     * offset 0x0+globalHeaderSize+exportDataSize+nameTableSize:
     *      IMPORT TABLE
     *
     * offset 0x0+globalHeaderSize+exportDataSize+nameTableSize+importTableSize:
     *      EXPORT TABLE
     *
     * THE EXPORT TABLE STORES OFFSETS AS INDICES ===> VARIABLE SIZE
     * We MUST calculate export data offsets before calculating the size of the export table
     *
     */
    private void calculateFileSize()
    {
        int size = 0;
        int globalHeaderSize, exportDataSize, importTableSize,
                nameTableSize, exportTableSize;

        // Add the size of the global header
        size += p.SIGNATURE.getSize()
                +p.VERSION.getSize()
                +p.LICENSE_MODE.getSize()
                +p.PACKAGE_FLAGS.getSize()
                +p.NAMES_CNT.getSize()
                +p.NAME_OFFSET.getSize()
                +p.IMPORTS_CNT.getSize()
                +p.IMPORT_OFFSET.getSize()
                +p.EXPORTS_CNT.getSize()
                +p.EXPORT_OFFSET.getSize()
                +p.FILE_GUID.length
                +p.GENERATIONS_CNT.getSize();

        // Add the size of the generations
        for (int i = 0; i < p.GENERATIONS_CNT.getLongValue(); ++i)
        {
            UGenerationEntry ge = p.GENERATIONS.get(i);
            size += ge.getEXPORTS().getSize()
                    +ge.getNAMES().getSize();
        }

        // Generations are part of the global header!
        globalHeaderSize = size;

        // Add the size of the export data and calculate offsets
        for (int i = 0; i < p.EXPORTS_CNT.getLongValue(); ++i)
        {
            UExportEntry ee = p.EXPORTS.get(i);
            if (ee.getSIZE().getLongValue() <= 0)
                continue;
            size += ee.getDATA().length;

            // Calculate offset and set in export table
            int exportDataOffset = 0x0+globalHeaderSize;
            for (int j = 0; j < i; ++j)
            {
                exportDataOffset += p.EXPORTS.get(j).getDATA().length;
            }

            // Set offset
            ee.getOFFSET().setLongValue(exportDataOffset);
        }
        exportDataSize = size - globalHeaderSize;

        // Add the size of the name table
        for (int i = 0; i < p.NAMES_CNT.getLongValue(); ++i)
        {
            UNameEntry ne = p.NAMES.get(i);
            size += ne.getNAME().getSize()
                    +ne.getFLAGS().getSize();
        }
        nameTableSize = size - globalHeaderSize - exportDataSize;

        // Add the size of the import table
        for (int i = 0; i < p.IMPORTS_CNT.getLongValue(); ++i)
        {
            UImportEntry ie = p.IMPORTS.get(i);
            size += ie.getCLASSNAME().getSize()
                    +ie.getCLASSPACKAGE().getSize()
                    +ie.getOBJECTNAME().getSize()
                    +ie.getPACKAGE().getSize();
        }
        importTableSize = size - globalHeaderSize - exportDataSize - nameTableSize;

        // Add the size of the export table
        for (int i = 0; i < p.EXPORTS_CNT.getLongValue(); ++i)
        {
            UExportEntry ee = p.EXPORTS.get(i);
            size += ee.getCLASS().getSize()
                    +ee.getFLAGS().getSize()
                    +ee.getNAME().getSize()
                    +ee.getPACKAGE().getSize()
                    +ee.getSIZE().getSize()
                    +ee.getSUPER().getSize();
            if (ee.getSIZE().getLongValue()>0)
                size += ee.getOFFSET().getSize();
        }
        exportTableSize = size - globalHeaderSize - exportDataSize - nameTableSize
                - importTableSize;

        newFileSize = size;

        // Set offsets for the nametable, importtable, exporttable
        p.NAME_OFFSET.setLongValue(0x0+globalHeaderSize+exportDataSize);
        p.IMPORT_OFFSET.setLongValue(0x0+globalHeaderSize+exportDataSize+nameTableSize);
        p.EXPORT_OFFSET.setLongValue(0x0+globalHeaderSize+exportDataSize+nameTableSize+importTableSize);

        logln("\nSizes:\nPackage Header: "+globalHeaderSize+"\nName Table: "+nameTableSize+"\nImport Table: "+importTableSize
                +"\nExport Table: "+exportTableSize+"\nTotal Size: "+size,1);
        logln("New Offsets:\nName Table: "+p.NAME_OFFSET.getLongValue()+"\nImport Table: "+p.IMPORT_OFFSET.getLongValue()
                +"\nExport Table: "+p.EXPORT_OFFSET.getLongValue()+"\nFirst Export: "+p.EXPORTS.get(0).getOFFSET().getLongValue(),1);
    }

    /**
     * Write the new package header to the output file o
     */
    private void writePackageHeader() throws IOException
    {
        p.SIGNATURE.writeDWORD(o);
        p.VERSION.setIntValue(69);
        p.VERSION.writeWORD(o);
        p.LICENSE_MODE.writeWORD(o);        
        p.PACKAGE_FLAGS.writeDWORD(o);
        p.NAMES_CNT.writeDWORD(o);
        p.NAME_OFFSET.writeDWORD(o);
        p.EXPORTS_CNT.writeDWORD(o);
        p.EXPORT_OFFSET.writeDWORD(o);
        p.IMPORTS_CNT.writeDWORD(o);
        p.IMPORT_OFFSET.writeDWORD(o);
        o.write(p.FILE_GUID, 0, p.FILE_GUID.length);
        p.GENERATIONS_CNT.writeDWORD(o);
        for (int i = 0; i < p.GENERATIONS_CNT.getLongValue(); ++i)
        {
            p.GENERATIONS.get(i).getEXPORTS().writeDWORD(o);
            p.GENERATIONS.get(i).getNAMES().writeDWORD(o);
        }
    }

    /**
     * Write the new NameTable to outputfile o
     */
    private void writeNameTable() throws IOException
    {
        if (o.getFilePointer() != p.NAME_OFFSET.getLongValue())
        {
            logln(">>> FILE POINTER WAS NOT AT NAME_OFFSET! BUG?! >>> SEEKING TO NAME_OFFSET",1);
            o.seek(p.NAME_OFFSET.getLongValue());
        }

        for (int l = 0; l < p.NAMES_CNT.getLongValue(); ++l)
        {
            p.NAMES.get(l).getNAME().writeNAME(o);
            p.NAMES.get(l).getFLAGS().writeDWORD(o);
        }
    }

    /**
     * Write the new ImportTable to outputfile o
     */
    private void writeImportTable() throws IOException
    {
        if (o.getFilePointer() != p.IMPORT_OFFSET.getLongValue())
        {
            logln(">>> FILE POINTER WAS NOT AT IMPORT_OFFSET! BUG?! >>> SEEKING TO IMPORT_OFFSET",1);
            o.seek(p.IMPORT_OFFSET.getLongValue());
        }

        for (int l = 0; l < p.IMPORTS_CNT.getLongValue(); ++l)
        {
            UImportEntry ie = p.IMPORTS.get(l);
            ie.getCLASSPACKAGE().writeINDEX(o);
            ie.getCLASSNAME().writeINDEX(o);
            ie.getPACKAGE().writeDWORD(o);
            ie.getOBJECTNAME().writeINDEX(o);
        }
    }

    /**
     * Write the new ExportTable to outputfile o
     */
    private void writeExportTable() throws IOException
    {
        if (o.getFilePointer() != p.EXPORT_OFFSET.getLongValue())
        {
            logln(">>> FILE POINTER WAS NOT AT EXPORT_OFFSET! BUG?! >>> SEEKING TO EXPORT_OFFSET",1);
            o.seek(p.EXPORT_OFFSET.getLongValue());
        }

        for (int l = 0; l < p.EXPORTS_CNT.getLongValue(); l++)
        {
            UExportEntry ee = p.EXPORTS.get(l);            

            ee.getCLASS().writeINDEX(o);
            ee.getSUPER().writeINDEX(o);
            ee.getPACKAGE().writeDWORD(o);
            ee.getNAME().writeINDEX(o);
            ee.getFLAGS().writeDWORD(o);
            ee.getSIZE().writeINDEX(o);
            if (ee.getSIZE().getLongValue() > 0)
                ee.getOFFSET().writeINDEX(o);           
        }        
    }

    /**
     * Write the new ExportData to outputfile o
     */
    private void writeExportData() throws IOException
    {
        for (int l = 0; l < p.EXPORTS_CNT.getLongValue(); ++l)
        {
            UExportEntry ie = p.EXPORTS.get(l);

            if (ie.getSIZE().getLongValue() <= 0)
                continue;

            if (o.getFilePointer() != ie.getOFFSET().getLongValue())
            {
                logln(">>> FILE POINTER WAS NOT AT EXPORT_DATA_"+l+"_OFFSET! BUG?! >>> SEEKING TO EXPORT_DATA_"+l+"_OFFSET",1);
                o.seek(ie.getOFFSET().getLongValue());
            }

            o.write(ie.getDATA());
        }
    }

    /**
     * Print the package header (for debugging purposes)
     */
    private void printPackageHeader()
    {
        logln("Signature: "+p.SIGNATURE+(p.SIGNATURE.toString().equals("0x9E2A83C1")?" OK":" NOT OK"),1);
        logln("Licensee Mode: "+p.LICENSE_MODE,1);
        logln("Package Version: "+p.VERSION.getIntValue()+(p.VERSION.getIntValue()>=68?" OK":" NOT OK"),1);
        logln("Package Flags: "+p.getPackageFlagsString()+"\n",1);
        logln("Names: "+p.NAMES_CNT.getLongValue()+" @ offset "+p.NAME_OFFSET,1);
        logln("Exports: "+p.EXPORTS_CNT.getLongValue()+" @ offset "+p.EXPORT_OFFSET,1);
        logln("Imports: "+p.IMPORTS_CNT.getLongValue()+" @ offset "+p.IMPORT_OFFSET+"\n",1);
        if (p.VERSION.getIntValue() >= 68)
        {
            logln("Generations Count: "+p.GENERATIONS_CNT.getLongValue(),1);
            for (long i = 0; i < p.GENERATIONS_CNT.getLongValue(); ++i)
            {
                logln("Generation "+i+": "+p.GENERATIONS.get((int)i),1);
            }
            logln("\nFile GUID: "+p.getGUIDString().toUpperCase(),1);
        }
        else
        {
            logln("Heritage Count: " + p.HERITAGE_COUNT.getLongValue(), 1);
            logln("Heritage Offset: " + p.HERITAGE_OFFSET.getLongValue(), 1);
            logln("\nFile GUID: "+p.getGUIDString().toUpperCase(),1);
        }
    }

    /**
     * Print the name table (for debugging purposes)
     */
    private void printNameTable()
    {
        for (long i = 0; i < p.NAMES_CNT.getLongValue(); ++i)
        {
            logln("\nName "+i+": "+p.NAMES.get((int)i).getNAME(),1);
            logln("Object flags: "+p.getObjectFlagsString(p.NAMES.get((int)i).getFLAGS()),1);
        }
    }

    /**
     * Print the export table (for debugging purposes)
     */
    private void printExportTable()
    {
        for (long i = 0; i < p.EXPORTS_CNT.getLongValue(); ++i)
        {
            UExportEntry ee = p.EXPORTS.get((int)i);
            UNameEntry ne = p.NAMES.get((int)ee.getNAME().getLongValue());

            logln("\nExportNum: "+i,1);
            logln("Name: "+ne.getNAME(),1);
            logln("Class: "+ee.getCLASS().getLongValue()+" => "+getNameByObjectRef(ee.getCLASS().getLongValue()).getNAME().toString(),1);
            logln("Super: "+ee.getSUPER().getLongValue()+" => "+getNameByObjectRef(ee.getSUPER().getLongValue()).getNAME().toString(),1);
            logln("Package: "+ee.getPACKAGE().getLongValue()+" => "+getNameByObjectRef(ee.getPACKAGE().getLongValue()).getNAME().toString(),1);
            logln("NameNum: "+ee.getNAME().getLongValue(),1);
            logln("Size: "+ee.getSIZE().getLongValue(),1);
            if (ee.getOFFSET() != null)
            {
                logln("SerialOffset: "+Long.toHexString(ee.getOFFSET().getLongValue()),1);
                if ((ee.getFLAGS().getLongValue() & p.RF_HasStack) > 0)
                {
                    logln("Node: "+ee.STATEFRAMENODE.getLongValue()+" => "+getNameByObjectRef(ee.STATEFRAMENODE.getLongValue()).getNAME().toString(),1);
                    logln("StateNode: "+ee.STATEFRAMESTATENODE.getLongValue()+" => "+getNameByObjectRef(ee.STATEFRAMESTATENODE.getLongValue()).getNAME().toString(),1);
                }
            }
            logln("Flags: "+p.getObjectFlagsString(ee.getFLAGS()),1);
            if (ne.getNAME().toString().equalsIgnoreCase("ScriptText")
                    || ne.getNAME().toString().equalsIgnoreCase("TextBuffer"))
                logln("Data: "+ee.getDATA(),1);
        }
    }

    /**
     * Print the import table (for debugging purposes)
     */
    private void printImportTable()
    {
        for (int i = 0; i < p.IMPORTS_CNT.getLongValue(); ++i)
        {
            UImportEntry ie = p.IMPORTS.get(i);

            logln("\nImportNum: "+i,1);
            logln("ClassPackageNum: "+ie.getCLASSPACKAGE()+" => "+p.NAMES.get((int)ie.getCLASSPACKAGE().getLongValue()).getNAME().toString(),1);
            logln("ClassNameNum: "+ie.getCLASSNAME()+" => "+p.NAMES.get((int)ie.getCLASSNAME().getLongValue()).getNAME().toString(),1);
            logln("PackageNum: "+ie.getPACKAGE().getLongValue()+" => "+getNameByObjectRef(ie.getPACKAGE().getLongValue()).getNAME().toString(),1);
            logln("ObjectNameNum: "+ie.getOBJECTNAME()+" => "+p.NAMES.get((int)ie.getOBJECTNAME().getLongValue()).getNAME().toString(),1);
        }
    }
}
