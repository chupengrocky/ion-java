package com.amazon.ion.impl;

import com.amazon.ion.impl.*;
import com.amazon.ion.util.Equivalence;
import java.io.IOException;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class NewTestCase_part3 {
    @Test
    public void testByteBuffer(){
        byte[] bytes = "Test Content".getBytes();
        boolean isReadOnly = false;
        int length = bytes.length;
        int start = 0;
        SimpleByteBuffer sb1 = new SimpleByteBuffer(bytes);
        SimpleByteBuffer sb2 = new SimpleByteBuffer(bytes, isReadOnly);
        SimpleByteBuffer sb3 = new SimpleByteBuffer(bytes, start, length);
        SimpleByteBuffer sb4 = new SimpleByteBuffer(bytes, start, length, isReadOnly);

        assertEquals(length - start, sb4.getLength());
        assertNotNull(sb4.getBytes());
        assertNotNull(sb3.getBytes(bytes, start, length));
        assertNotNull(sb4.getReader());
        assertNotNull(sb4.getWriter());
        assertNotNull(sb4.getWriter());
    }

    @Test
    public void testByteReader() throws Exception{
        byte[] bytes = "Test Content is a simple content for testing".getBytes();
        boolean isReadOnly = false;
        int length = bytes.length;
        int start = 0;
        SimpleByteBuffer sb1 = new SimpleByteBuffer(bytes);
        SimpleByteBuffer.SimpleByteReader sbr = new SimpleByteBuffer.SimpleByteReader(sb1);


        assertNotNull(sbr.position());
        sbr.position(1);
        sbr.skip(1);
        assertNotNull(sbr.read());
        assertNotNull(sbr.readULong(0));
        assertNotNull(sbr.readULong(1));
        assertNotNull(sbr.readULong(2));
        assertNotNull(sbr.readULong(3));
        assertNotNull(sbr.readULong(4));
        assertNotNull(sbr.readULong(5));
        assertNotNull(sbr.readULong(6));
        assertNotNull(sbr.readULong(7));
        assertNotNull(sbr.readULong(8));
    }
}
