package gov.usgs.cida.qw.code;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class AggregatedCodesTest {

    @Test
    public void testAggregatedCode() {
        //We want to make sure that the providers come out in upper case, space delimited, and in alphabetic order.
        AggregatedCode aCode = new AggregatedCode(new Code("myValue", "description"), "MID");

        assertEquals("myValue", aCode.getValue());
        assertEquals("description", aCode.getDesc());
        assertEquals("MID", aCode.getProviders());
        assertEquals("(value=myValue, desc=description)", aCode.toString());

        aCode.addProvider("SUPER");
        assertEquals("MID SUPER", aCode.getProviders());

        aCode.addProvider("BIG");
        assertEquals("BIG MID SUPER", aCode.getProviders());

        aCode.addProvider("lame");
        assertEquals("BIG LAME MID SUPER", aCode.getProviders());

        aCode.addProvider("BIG");
        assertEquals("BIG LAME MID SUPER", aCode.getProviders());
    }

    @Test
    public void testBadThings() {
        try {
            new AggregatedCode(null, null);
            fail("Should have gotten an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Must provide the Code to use this constructor.", e.getMessage());
        }

        try {
            new AggregatedCode(new Code("myValue", "description"), null);
            fail("Should have gotten an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The provider cannot be empty.", e.getMessage());
        }

        AggregatedCode aCode = new AggregatedCode();
        try {
            aCode.addProvider(null);
            fail("Should have gotten an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The provider cannot be empty.", e.getMessage());
        }

        try {
            aCode.addProvider("");
            fail("Should have gotten an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The provider cannot be empty.", e.getMessage());
        }
    }
}
