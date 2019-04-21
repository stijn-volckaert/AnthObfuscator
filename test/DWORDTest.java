/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import anthobfuscator.datatypes.UDword;
import anthobfuscator.datatypes.UWord;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Stijn
 */
public class DWORDTest {

    public DWORDTest() {
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
    public void testSetLongValue()
    {
        UDword test = new UDword((byte)0x54,(byte)0x24,(byte)0x86,(byte)0x98);
        String orighexStr = test.toString();
        long origLong = test.getLongValue();
        test.setLongValue(test.getLongValue());
        assert(orighexStr.equals(test.toString()));
        assert(origLong == test.getLongValue());
    }

    @Test
    public void testGetHigherLowerWord()
    {
        UDword test = new UDword((byte)0x54,(byte)0x24,(byte)0x86,(byte)0x98);
        UWord higher = test.getHigherWord();
        UWord lower = test.getLowerWord();
        assert(higher.toString().equals("0x9886"));
        assert(lower.toString().equals("0x2454"));
    }
}