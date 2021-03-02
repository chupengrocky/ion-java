package com.amazon.ion;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class NewCasePart5ATest {

    @Test
    public void testPublicConstructor(){
        BigDecimal d1, d2, d3, d4;

        d1 = new BigDecimal ("0.", new MathContext(4, RoundingMode.HALF_UP));
        d2 = new Decimal(Double.valueOf(0.), new MathContext(4, RoundingMode.HALF_UP));
        assertEquals(d1, d2);
        assertEquals("hash code", d1.hashCode(), d2.hashCode());

        char[] negativeIn = new char [] {'-','0','.','0'};
        char[] positiveIn = new char [] {'0','.','0'};
        d1 = new BigDecimal(negativeIn);
        d2 = new Decimal(negativeIn);
        assertEquals(d1, d2);
        assertEquals("hash code", d1.hashCode(), d2.hashCode());

        // Compare between two Constructor: Double and character array
        d1 = new BigDecimal ( Double.valueOf(0.00));
        d2 = new Decimal(Double.valueOf(-0.00));
        assertEquals(d1, d2);
        assertEquals("hash code", d1.hashCode(), d2.hashCode());

        // Finish the Todo Part for char[] input in original repo
        d1 = new BigDecimal (negativeIn);
        d2 = Decimal.valueOf(positiveIn);
        assertEquals(d1, d2);
        assertEquals("hash code", d1.hashCode(), d2.hashCode());

        // Compare multiple instance
        d2 = new Decimal(Double.valueOf(-0.00));
        d3 = Decimal.valueOf("12");
        d4 = new Decimal(new char[]{'1','2'});
        assertNotEquals(d2, d3);
        assertEquals(d3, d4);
        assertEquals("hash code", d3.hashCode(), d4.hashCode());

    }
}
