// Copyright (c) 2015 Amazon.com, Inc.  All rights reserved.

package com.amazon.ion.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An output stream that does not perform any writes, used for testing.
 */
public class NullOutputStream extends OutputStream
{
    @Override
    public void write(int b)
        throws IOException
    {
    }

    @Override
    public void write(byte[] b)
        throws IOException
    {
    }

    @Override
    public void write(byte[] b, int off, int len)
        throws IOException
    {
    }
}