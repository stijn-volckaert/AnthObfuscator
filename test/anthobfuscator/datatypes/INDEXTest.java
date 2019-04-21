/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package anthobfuscator.datatypes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Stijn
 */
public class INDEXTest {

    public INDEXTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSetLongValue() throws FileNotFoundException, IOException
    {
        /** Test 300 */
        RandomAccessFile f = new RandomAccessFile("testfile.txt","rw");
        long beginpos = f.getFilePointer();
        f.writeByte(108);
        f.writeByte(4);
        f.writeByte(0);
        f.writeByte(0);
        f.seek(beginpos);
        UIndex ui = UIndex.readINDEX(f);
        System.out.println(ui);
        assert(ui.getLongValue() == 300);
        assert(ui.toString().equals("#300"));
        ui.setLongValue(300);        
        assert(ui.A == 108);
        assert(ui.B == 4);
        assert(ui.C == 0);
        assert(ui.D == 0);
        assert(ui.L == 300);
        assert(ui.S == 2);
        f.close();

        /** Test -75 */
        f = new RandomAccessFile("testfile.txt","rw");
        beginpos = f.getFilePointer();
        f.writeByte(-53);
        f.writeByte(1);
        f.writeByte(0);
        f.writeByte(0);
        f.seek(beginpos);
        ui = UIndex.readINDEX(f);
        System.out.println(ui);
        assert(ui.getLongValue() == -75);
        assert(ui.toString().equals("#-75"));
        ui.setLongValue(-75);
        assert(ui.A == -53);
        assert(ui.B == 1);
        assert(ui.C == 0);
        assert(ui.D == 0);
        assert(ui.L == -75);
        assert(ui.S == 2);
        f.close();

        
    }

}