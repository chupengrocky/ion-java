/*
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

import com.amazon.ion.*;
import com.amazon.ion.impl.IonReaderBinaryRawLargeStreamTest;
import com.amazon.ion.impl.RawValueSpanReaderTest;
import com.amazon.ion.facet.FacetsTest;
import com.amazon.ion.impl.ByteBufferTest;
import com.amazon.ion.impl.CharacterReaderTest;
import com.amazon.ion.impl.IonImplUtilsTest;
import com.amazon.ion.impl.IonMarkupWriterFilesTest;
import com.amazon.ion.impl.IonMarkupWriterTest;
import com.amazon.ion.impl.IonWriterTests;
import com.amazon.ion.impl.IterationTest;
import com.amazon.ion.impl.LocalSymbolTableTest;
import com.amazon.ion.impl.SharedSymbolTableTest;
import com.amazon.ion.impl.SymbolTableTest;
import com.amazon.ion.impl.TreeReaderTest;
import com.amazon.ion.impl.bin.IonManagedBinaryWriterTest;
import com.amazon.ion.impl.bin.IonRawBinaryWriterTest;
import com.amazon.ion.impl.bin.PooledBlockAllocatorProviderTest;
import com.amazon.ion.impl.bin.WriteBufferTest;
import com.amazon.ion.impl.lite.IonContextTest;
import com.amazon.ion.impl.lite.SIDPresentLifecycleTest;
import com.amazon.ion.impl.NewCasePart3Test;
import com.amazon.ion.streaming.BadIonStreamingTest;
import com.amazon.ion.streaming.BinaryStreamingTest;
import com.amazon.ion.streaming.GoodIonStreamingTest;
import com.amazon.ion.streaming.InputStreamReaderTest;
import com.amazon.ion.streaming.MiscStreamingTest;
import com.amazon.ion.streaming.ReaderDomCopyTest;
import com.amazon.ion.streaming.ReaderIntegerSizeTest;
import com.amazon.ion.streaming.ReaderSkippingTest;
import com.amazon.ion.streaming.ReaderTest;
import com.amazon.ion.streaming.RoundTripStreamingTest;
import com.amazon.ion.streaming.SpanTests;
import com.amazon.ion.system.IonBinaryWriterBuilderTest;
import com.amazon.ion.system.IonReaderBuilderTest;
import com.amazon.ion.system.IonSystemBuilderTest;
import com.amazon.ion.system.IonTextWriterBuilderTest;
import com.amazon.ion.system.SimpleCatalogTest;
import com.amazon.ion.util.EquivalenceTest;
import com.amazon.ion.util.IonStreamUtilsTest;
import com.amazon.ion.util.JarInfoTest;
import com.amazon.ion.util.PrinterTest;
import com.amazon.ion.util.TextTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * Runs all tests for the Ion project.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    // Low-level facilities.
    AssertionsEnabledTest.class,
    IonExceptionTest.class,
    FacetsTest.class,
    ByteBufferTest.class,
    TextTest.class,
    CharacterReaderTest.class,
    JavaNumericsTest.class,
    ExtendedDecimalTest.class,
    IonImplUtilsTest.class,

    // General framework tests
    SimpleCatalogTest.class,

    // Type-based DOM tests
    IonValueTest.class,
    BlobTest.class,
    BoolTest.class,
    ClobTest.class,
    CloneTest.class,
    DatagramTest.class,
    DecimalTest.class,
    FloatTest.class,
    IntTest.class,
    ListTest.class,
    NullTest.class,
    SexpTest.class,
    StringTest.class,
    LongStringTest.class,
    StructTest.class,
    SymbolTest.class,
    TimestampTest.class,
    TimestampGoodTest.class,
    TimestampBadTest.class,

    AnnotationEscapesTest.class,
    FieldNameEscapesTest.class,
    StringFieldNameEscapesTest.class,
    SurrogateEscapeTest.class,

    // Markup tests
    IonMarkupWriterTest.class,
    IonMarkupWriterFilesTest.class,

    NopPaddingTest.class,

    // Binary format tests
    BinaryTest.class,

    // Utility tests
    JarInfoTest.class,
    LoaderTest.class,
    IterationTest.class,
    PrinterTest.class,
    SymbolTableTest.class,
    SharedSymbolTableTest.class,
    LocalSymbolTableTest.class,
    IonContextTest.class,

    // Equality tests
    EquivalenceTest.class,
    EquivsTest.class,
    NonEquivsTest.class,
    EquivTimelineTest.class,

    // General processing test suite
    GoodIonTest.class,
    BadIonTest.class,
    RoundTripTest.class,

    // Some tests are collected to make it easier to run interesting subsets.
    SystemProcessingTests.class,
    IonWriterTests.class,
    SpanTests.class,

    IonStreamUtilsTest.class,
    TreeReaderTest.class,
    MiscStreamingTest.class,
    BinaryStreamingTest.class,
    ReaderTest.class,
    InputStreamReaderTest.class,

    BadIonStreamingTest.class,
    GoodIonStreamingTest.class,
    RoundTripStreamingTest.class,
    ReaderDomCopyTest.class,
    ReaderSkippingTest.class,
    ReaderIntegerSizeTest.class,

    IonSystemTest.class,
    ValueFactorySequenceTest.class,
    IonSystemBuilderTest.class,
    IonTextWriterBuilderTest.class,
    IonBinaryWriterBuilderTest.class,
    IonReaderToIonValueTest.class,
    BinaryReaderWrappedValueLengthTest.class,
    IonReaderBuilderTest.class,
    IonReaderBinaryRawLargeStreamTest.class,

    // experimental binary writer tests
    PooledBlockAllocatorProviderTest.class,
    WriteBufferTest.class,
    IonRawBinaryWriterTest.class,
    IonManagedBinaryWriterTest.class,

    // Hash code tests
    HashCodeCorrectnessTest.class,
    HashCodeDistributionTest.class,
    HashCodeDeltaCollisionTest.class,

    IonRawWriterBasicTest.class,
    IonRawWriterSymbolsTest.class,
    RawValueSpanReaderBasicTest.class,
    RawValueSpanReaderTest.class,

    // DOM Lifecycle / mode tests
    SIDPresentLifecycleTest.class,

    //Project 261
    NewCasePart1Test.class,
    NewCasePart2Test.class,
    NewCasePart3Test.class

})
public class AllTests
{
}
