/*******************************************************************************
 * AnthObfuscator v1.1 - (c) 2008-2009 AnthraX
 * File : Main.java
 * Revision History:
 *      Created by AnthraX
 ******************************************************************************/
package anthobfuscator;

/**
 * Main class of the Application. Parses the parameters and creates an
 * obfuscator object. Will be replaced by a Swing GUI in a later version.
 */
public class Main
{
    /**
     * Syntax: AnthObfuscator.jar <file> [-o] [-n] [-s] [-r]
     * @param args
     */
    public static void main(String[] args)
    {
        boolean obfuscateNames = false, fixNPLClasses = false, stripSource = false, randomizeExports = false, deObfuscate = false;
        String fileName = "";

        if (args.length < 1)
        {
            System.out.println("ERROR: Incorrect Syntax.");
            System.out.println("Syntax: AnthObfuscator.jar <file> [-o] [-n] [-s] [-r]");
            System.out.println("    or: AnthObfuscator.jar <file> -d");
            return;
        }
        else
        {
            fileName = args[0];

            if (args.length == 2 && args[1].equalsIgnoreCase("-d"))
            {
                deObfuscate = true;
            }
            else
            {
                for (int i = 1; i < args.length; ++i)
                {
                    if (args[i].equalsIgnoreCase("-o"))
                    {
                        obfuscateNames = true;
                    }
                    else if (args[i].equalsIgnoreCase("-n"))
                    {
                        fixNPLClasses = true;
                    }
                    else if (args[i].equalsIgnoreCase("-s"))
                    {
                        stripSource = true;
                    }
                    else if (args[i].equalsIgnoreCase("-r"))
                    {
                        randomizeExports = true;
                    }
                }
            }
            
            System.out.println("Starting Obfuscator with the following settings:");
            System.out.println("File Name: "+fileName);
            System.out.println("DeObfuscate Names: "+deObfuscate);
            System.out.println("Obfuscate Names: "+obfuscateNames);
            System.out.println("Fix NPL Classes: "+fixNPLClasses);
            System.out.println("Strip Source: "+stripSource);
            System.out.println("Randomize Exports: "+randomizeExports+"\n");
            Obfuscator o = new Obfuscator(fileName,stripSource,obfuscateNames,fixNPLClasses,randomizeExports,deObfuscate,true);
        }
    }    
}
