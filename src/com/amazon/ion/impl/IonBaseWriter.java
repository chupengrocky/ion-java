/*
 * Copyright (c) 2008 Amazon.com, Inc.  All rights reserved.
 */

package com.amazon.ion.impl;

import com.amazon.ion.IonReader;
import com.amazon.ion.IonType;
import com.amazon.ion.IonValue;
import com.amazon.ion.IonWriter;
import com.amazon.ion.SymbolTable;
import java.io.IOException;

/**
 *  Base type for Ion writers.  This handles the writeIonEvents and default
 *  handlers for array writers, annotations, the symbol table, and the field name.
 */
public abstract class IonBaseWriter
    implements IonWriter
{
    private static final boolean _debug_on = false;

    /**
     * Must be either local or system table.
     * FIXME when can this be null?  When can it be changed?
     */
    protected SymbolTable _symbol_table;

    protected IonType     _field_name_type;     // really ion type is only used for int, string or null (unknown)
    protected String      _field_name;
    protected int         _field_name_sid;

    protected IonType     _annotations_type;     // really ion type is only used for int, string or null (unknown)
    protected int         _annotation_count;
    protected String[]    _annotations;
    protected int[]       _annotation_sids = new int[10];


    public SymbolTable getSymbolTable()
    {
        return _symbol_table;
    }

    public void setSymbolTable(SymbolTable symbols)
    {
        if (symbols != null
            && ! symbols.isLocalTable()
            && ! symbols.isSystemTable())
        {
            throw new IllegalArgumentException("table must be local or system");
        }
        _symbol_table = symbols;
    }

    protected boolean symtabIsLocalAndNonTrivial()
    {
        return (_symbol_table.isLocalTable()
                && !_symbol_table.isTrivial());
    }

    protected String getSymbolTableName() {
        if (_symbol_table != null) {
            return _symbol_table.getName();
        }
        return null;
    }
    protected int getSymbolTableVersion() {
        if (_symbol_table != null) {
            return _symbol_table.getVersion();
        }
        return 0;
    }
    protected UnifiedSymbolTable[] getSymbolTableImportedTables() {
        if (_symbol_table instanceof UnifiedSymbolTable) {
            return ((UnifiedSymbolTable)_symbol_table).getImportedTables();
        }
        return null;
    }
    protected UnifiedSymbolTable.Symbol[] getSymbolArray() {
        if (_symbol_table instanceof UnifiedSymbolTable) {
            return ((UnifiedSymbolTable)_symbol_table)._symbols;
        }
        else if (_symbol_table != null) {
            int count = _symbol_table.getMaxId();
            UnifiedSymbolTable.Symbol[] symbols = new UnifiedSymbolTable.Symbol[count];
            SymbolTable system = _symbol_table.getSystemSymbolTable();
            int systemidmax = system == null ? 0 : system.getMaxId();
            for (int ii=systemidmax; ii<count; ii++) {
                String name = _symbol_table.findKnownSymbol(ii);
                if (name != null) {
                    UnifiedSymbolTable.Symbol sym = new UnifiedSymbolTable.Symbol();
                    sym.name = name;
                    sym.sid = ii;
                    symbols[ii] = sym;
                }
            }
            return symbols;
        }
        return null;
    }
    /* TODO delete this if the routine above proves more useful
    Iterator<UnifiedSymbolTable.Symbol> xxgetSymbolTableSymbols() {
        if (_symbol_table instanceof UnifiedSymbolTable) {
            return ((UnifiedSymbolTable)_symbol_table).getLocalSymbols();
        }
        else if (_symbol_table != null) {
            int count = _symbol_table.getMaxId();
            Stack<UnifiedSymbolTable.Symbol> symbols = new Stack<UnifiedSymbolTable.Symbol>();
            SymbolTable system = _symbol_table.getSystemSymbolTable();
            int systemidmax = system == null ? 0 : system.getMaxId();
            for (int ii=systemidmax; ii<count; ii++) {
                String name = _symbol_table.findKnownSymbol(ii);
                if (name != null) {
                    UnifiedSymbolTable.Symbol sym = new UnifiedSymbolTable.Symbol();
                    sym.name = name;
                    sym.sid = ii;
                    symbols.push(sym);
                }
            }
            return symbols.iterator();
        }
        return null;
    }
    */
    protected int getSymbolTableMaxId() {
        if (_symbol_table != null) {
            return _symbol_table.getMaxId();
        }
        return 0;
    }

    public void clearAnnotations()
    {
        _annotation_count = 0;
        _annotations_type = IonType.NULL;
    }

    protected String[] get_annotations_as_strings() {
        if (_annotations_type == IonType.INT) {
            SymbolTable symtab = getSymbolTable();
            if (symtab == null) {
                throw new IllegalStateException("a symbol table is required for MIXED id and string use");
            }
            for (int ii=0; ii<_annotation_count; ii++) {
                String name;
                int id = _annotation_sids[ii];
                name = symtab.findKnownSymbol(id); // FIXME can return null
                _annotations[ii] = name;
            }
        }
        return _annotations;
    }

    protected int[] get_annotations_as_ints() {
        if (_annotations_type == IonType.STRING) {
            for (int ii=0; ii<_annotation_count; ii++) {
                String name = _annotations[ii];
                _annotation_sids[ii] = add_symbol(name);
            }
        }
        return _annotation_sids;

    }

    /**
     * Does not increase _annotation_count.
     */
    protected void growAnnotations(int length) {
        int oldlen = (_annotations == null) ? 0 : _annotations.length;
        if (length < oldlen) return;
        int newlen = (_annotations == null) ? 10 : (_annotations.length * 2);
        if (length > newlen) {
            newlen = length;
        }

        String[] temp1 = new String[newlen];
        int[]    temp2 = new int[newlen];

        if (_annotation_count > 0) {
            if (_annotations_type == IonType.STRING) {
                System.arraycopy(_annotations, 0, temp1, 0, _annotation_count);
            }
            if (_annotations_type == IonType.INT) {
                System.arraycopy(_annotation_sids, 0, temp2, 0, _annotation_count);
            }
        }
        _annotations = temp1;
        _annotation_sids = temp2;
    }

    public void setTypeAnnotations(String[] annotations)
    {
        _annotations_type = IonType.STRING;
        if (annotations.length > _annotation_count) {
            growAnnotations(annotations.length);
        }
        System.arraycopy(annotations, 0, _annotations, 0, annotations.length);
        _annotation_count = annotations.length;
    }

    public void setTypeAnnotationIds(int[] annotationIds)
    {
        _annotations_type = IonType.INT;
        if (annotationIds.length > _annotation_count) {
            growAnnotations(annotationIds.length);
        }
        System.arraycopy(annotationIds, 0, _annotation_sids, 0, annotationIds.length);
        _annotation_count = annotationIds.length;
    }

    public void addTypeAnnotation(String annotation)
    {
        growAnnotations(_annotation_count + 1);
        if (_annotations_type == IonType.INT) {
            int sid = this.add_symbol(annotation);
            this.addTypeAnnotationId(sid);
        }
        else {
            _annotations_type = IonType.STRING;
            // FIXME: annotations need to be "washed" through a symbol
            //        table to address issues like $0234 -> $234 or 'xyzzx'
            _annotations[_annotation_count++] = annotation;
        }
    }

    public void addTypeAnnotationId(int annotationId)
    {
        growAnnotations(_annotation_count + 1);
        if (_annotations_type == IonType.STRING) {
            SymbolTable symtab = getSymbolTable();
            String annotation = symtab.findSymbol(annotationId);
            addTypeAnnotation(annotation);
        }
        else {
            _annotations_type = IonType.INT;
            _annotation_sids[_annotation_count++] = annotationId;
        }
    }


    protected void clearFieldName() {
        _field_name_type = IonType.NULL;
        _field_name = null;
        _field_name_sid = 0;
    }
    protected String get_field_name_as_string() {
        if (_field_name_type == IonType.INT) {
            SymbolTable symtab = getSymbolTable();
            if (symtab == null) {
                throw new IllegalStateException("a symbol table is required for mixed id and string use");
            }
            String name;
            int id = _field_name_sid;
            name = symtab.findKnownSymbol(id); // FIXME this can return null
            _field_name = name;
        }
        return _field_name;
    }

    protected int get_field_name_as_int() {
        if (_field_name_type == IonType.STRING) {
            _field_name_sid = add_symbol(_field_name);
        }
        return _field_name_sid;
    }

//    protected void updateCurrentSymbolTable() // XXX
//    {
//        return _symbol_table;
//    }

    protected int add_symbol(String name)
    {
        // FIXME I think the current symtab should *never* be null.
        // That's because any Ion data must be written in the context of a
        // specific system version (eg $ion_1_0) and therefore we should always
        // know a specific system symtab at any point of the data.

        if (_symbol_table == null) {
            _symbol_table = new UnifiedSymbolTable(UnifiedSymbolTable.getSystemSymbolTableInstance());
        }

        int sid = _symbol_table.findSymbol(name);
        if (sid > 0) return sid;

        if (_symbol_table.isSystemTable()) {
            _symbol_table = new UnifiedSymbolTable(_symbol_table);
        }
        assert _symbol_table.isLocalTable();

        sid = _symbol_table.addSymbol(name);
        return sid;
    }

    public void setFieldName(String name)
    {
        if (!this.isInStruct()) {
            throw new IllegalStateException();
        }
        _field_name_type = IonType.STRING;
        _field_name = name;
    }

    public void setFieldId(int id)
    {
        if (!this.isInStruct()) {
            throw new IllegalStateException();
        }
        _field_name_type = IonType.INT;
        _field_name_sid = id;
    }

    public void writeStringList(String[] values)
        throws IOException
    {
        stepIn(IonType.LIST);
        for (int ii=0; ii<values.length; ii++) {
            writeString(values[ii]);
        }
        stepOut();
    }

    public void writeBoolList(boolean[] values)
        throws IOException
    {
        stepIn(IonType.LIST);
        for (int ii=0; ii<values.length; ii++) {
            writeBool(values[ii]);
        }
        stepOut();
    }

    public void writeFloatList(float[] values)
        throws IOException
    {
        stepIn(IonType.LIST);
        for (int ii=0; ii<values.length; ii++) {
            writeFloat(values[ii]);
        }
        stepOut();
    }

    public void writeFloatList(double[] values)
        throws IOException
    {
        stepIn(IonType.LIST);
        for (int ii=0; ii<values.length; ii++) {
            writeFloat(values[ii]);
        }
        stepOut();
    }

    public void writeIntList(byte[] values)
        throws IOException
    {
        stepIn(IonType.LIST);
        for (int ii=0; ii<values.length; ii++) {
            writeInt(values[ii]);
        }
        stepOut();
    }

    public void writeIntList(short[] values)
        throws IOException
    {
        stepIn(IonType.LIST);
        for (int ii=0; ii<values.length; ii++) {
            writeInt(values[ii]);
        }
        stepOut();
    }

    public void writeIntList(int[] values)
        throws IOException
    {
        stepIn(IonType.LIST);
        for (int ii=0; ii<values.length; ii++) {
            writeInt(values[ii]);
        }
        stepOut();
    }

    public void writeIntList(long[] values)
        throws IOException
    {
        stepIn(IonType.LIST);
        for (int ii=0; ii<values.length; ii++) {
            writeInt(values[ii]);
        }
        stepOut();
    }

    public void writeValue(IonValue value) throws IOException
    {
        IonReader valueReader = new IonTreeReader(value);
        writeValues(valueReader);
    }

    public void writeValues(IonReader reader) throws IOException
    {
        while (reader.hasNext()) {
            reader.next();
            writeValue(reader);
        }
        return;
    }

    public void writeValue(IonReader reader) throws IOException
    {
        if (/* reader.isInStruct() && */ this.isInStruct()) {
            String fieldname = reader.getFieldName();
            setFieldName(fieldname);
            if (_debug_on) System.out.print(":");
        }
        String [] a = reader.getTypeAnnotations();
        if (a != null) {
            setTypeAnnotations(a);
            if (_debug_on) System.out.print(";");
        }

        if (reader.isNullValue()) {
            this.writeNull(reader.getType());  // TODO hoist getType
        }
        else {
            switch (reader.getType()) {
            case NULL:
                writeNull();
                if (_debug_on) System.out.print("-");
                break;
            case BOOL:
                writeBool(reader.booleanValue());
                if (_debug_on) System.out.print("b");
                break;
            case INT:
                writeInt(reader.longValue());  // FIXME should use bigInteger
                if (_debug_on) System.out.print("i");
                break;
            case FLOAT:
                writeFloat(reader.doubleValue());
                if (_debug_on) System.out.print("f");
                break;
            case DECIMAL:
                writeDecimal(reader.bigDecimalValue());
                if (_debug_on) System.out.print("d");
                break;
            case TIMESTAMP:
                writeTimestamp(reader.timestampValue());
                if (_debug_on) System.out.print("t");
                break;
            case STRING:
                writeString(reader.stringValue());
                if (_debug_on) System.out.print("$");
                break;
            case SYMBOL:
                writeSymbol(reader.stringValue());
                if (_debug_on) System.out.print("y");
                break;
            case BLOB:
                writeBlob(reader.newBytes());
                if (_debug_on) System.out.print("B");
                break;
            case CLOB:
                writeClob(reader.newBytes());
                if (_debug_on) System.out.print("L");
                break;
            case STRUCT:
                if (_debug_on) System.out.print("{");
                stepIn(IonType.STRUCT);
                reader.stepIn();
                writeValues(reader);
                reader.stepOut();
                stepOut();
                if (_debug_on) System.out.print("}");
                break;
            case LIST:
                if (_debug_on) System.out.print("[");
                stepIn(IonType.LIST);
                reader.stepIn();
                writeValues(reader);
                reader.stepOut();
                stepOut();
                if (_debug_on) System.out.print("]");
                break;
            case SEXP:
                if (_debug_on) System.out.print("(");
                stepIn(IonType.SEXP);
                reader.stepIn();
                writeValues(reader);
                reader.stepOut();
                stepOut();
                if (_debug_on) System.out.print(")");
                break;
            default:
                throw new IllegalStateException();
            }
        }
    }
}