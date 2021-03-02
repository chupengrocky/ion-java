package com.amazon.ion;/*
 * Copyright 2007-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */


import org.junit.Test;
import com.amazon.ion.system.*;
import java.io.*;
import static org.junit.Assert.*;


public class NewCasePart2Test extends IonTestCase{
    @Test
    public void testFiniteStateMachineReader(){
        IonReaderBuilder readerBuilder = IonReaderBuilder.standard();
        String json = "{Key: \"Value\"}";
        IonReader reader = readerBuilder.build(json);
        assertEquals(IonType.STRUCT, reader.next());  // position the reader at the first value, a struct

        reader.stepIn();                              // step into the struct
        assertEquals(IonType.STRING, reader.next());  // position the reader at the first value in the struct

        String fieldName = reader.getFieldName();     // retrieve the current value's field name
        String value = reader.stringValue();          // retrieve the current value's String value
        assertEquals("Key", fieldName);
        assertEquals("Value", value);
        assertNull(reader.next());

        reader.stepOut();                             // step out of the struct
        assertNull(reader.next());
    }

    @Test
    public void testFiniteStateMachineTextWriter()  throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IonTextWriterBuilder textWriterBuilder = IonTextWriterBuilder.standard();


        IonWriter textWriter = textWriterBuilder.build(out);
        textWriter.stepIn(IonType.STRUCT);  // step into a struct
        textWriter.setFieldName("Key");   // set the field name for the next value to be written
        textWriter.writeString("Value");    // write the next value
        textWriter.stepOut();               // step out of the struct

        assertEquals("{Key:\"Value\"}",out.toString("UTF-8"));
    }

    @Test
    public void testFiniteStateMachineBinaryWriter()  throws Exception {
        IonSystem system = IonSystemBuilder.standard().build();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IonReaderBuilder readerBuilder = IonReaderBuilder.standard();
        String json = "{Key: \"Value\"}";
        IonReader reader = readerBuilder.build(json);

        IonBinaryWriterBuilder binaryWriterBuilder = IonBinaryWriterBuilder.standard();

        IonWriter binaryWriter = binaryWriterBuilder.build(out);

        binaryWriter.writeValues(reader);
        reader.close();
        binaryWriter.close();

        assertEquals("{Key:\"Value\"}", system.singleValue(out.toByteArray()).toString());
    }
}

