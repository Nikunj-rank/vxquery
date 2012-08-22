package org.apache.vxquery.runtime.functions.arithmetic;

import java.io.DataOutput;
import java.io.IOException;

import org.apache.vxquery.context.DynamicContext;
import org.apache.vxquery.datamodel.accessors.atomic.XSDatePointable;
import org.apache.vxquery.datamodel.accessors.atomic.XSDateTimePointable;
import org.apache.vxquery.datamodel.accessors.atomic.XSDecimalPointable;
import org.apache.vxquery.datamodel.accessors.atomic.XSTimePointable;
import org.apache.vxquery.datamodel.values.ValueTag;
import org.apache.vxquery.exceptions.ErrorCode;
import org.apache.vxquery.exceptions.SystemException;

import edu.uci.ics.hyracks.data.std.primitive.DoublePointable;
import edu.uci.ics.hyracks.data.std.primitive.FloatPointable;
import edu.uci.ics.hyracks.data.std.primitive.IntegerPointable;
import edu.uci.ics.hyracks.data.std.primitive.LongPointable;
import edu.uci.ics.hyracks.data.std.util.ArrayBackedValueStorage;

public class ModOperation extends AbstractArithmeticOperation {
    protected final ArrayBackedValueStorage abvsInner = new ArrayBackedValueStorage();

    @Override
    public void operateDateDate(XSDatePointable datep, XSDatePointable datep2, DynamicContext dCtx, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateDateDTDuration(XSDatePointable datep, LongPointable longp, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateDatetimeDatetime(XSDateTimePointable datetimep, XSDateTimePointable datetimep2,
            DynamicContext dCtx, DataOutput dOut) throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateDatetimeDTDuration(XSDateTimePointable datetimep, LongPointable longp, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateDatetimeYMDuration(XSDateTimePointable datetimep, IntegerPointable intp, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateDateYMDuration(XSDatePointable datep, IntegerPointable intp, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateDecimalDecimal(XSDecimalPointable decp1, XSDecimalPointable decp2, DataOutput dOut)
            throws SystemException, IOException {
        // Prepare
        long value1 = decp1.getDecimalValue();
        long value2 = decp2.getDecimalValue();
        byte place1 = decp1.getDecimalPlace();
        byte place2 = decp2.getDecimalPlace();
        byte count1 = decp1.getDigitCount();
        byte count2 = decp2.getDigitCount();
        // Convert to matching values
        while (place1 > place2) {
            ++place2;
            value2 *= 10;
            ++count2;
        }
        while (place1 < place2) {
            ++place1;
            value1 *= 10;
            ++count1;
        }
        // Add
        if (count1 > XSDecimalPointable.PRECISION || count2 > XSDecimalPointable.PRECISION) {
            throw new SystemException(ErrorCode.XPDY0002);
        }
        value1 %= value2;

        // Save
        dOut.write(ValueTag.XS_DECIMAL_TAG);
        dOut.writeByte(place1);
        dOut.writeLong(value1);
    }

    @Override
    public void operateDecimalDouble(XSDecimalPointable decp, DoublePointable doublep, DataOutput dOut)
            throws SystemException, IOException {
        double value = decp.doubleValue();
        value %= doublep.doubleValue();
        dOut.write(ValueTag.XS_DOUBLE_TAG);
        dOut.writeDouble(value);
    }

    @Override
    public void operateDecimalDTDuration(XSDecimalPointable decp, LongPointable longp, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateDecimalFloat(XSDecimalPointable decp, FloatPointable floatp, DataOutput dOut)
            throws SystemException, IOException {
        float value = decp.floatValue();
        value %= floatp.floatValue();
        dOut.write(ValueTag.XS_FLOAT_TAG);
        dOut.writeFloat(value);
    }

    @Override
    public void operateDecimalInteger(XSDecimalPointable decp1, LongPointable longp2, DataOutput dOut)
            throws SystemException, IOException {
        abvsInner.reset();
        XSDecimalPointable decp2 = new XSDecimalPointable();
        decp2.set(abvsInner.getByteArray(), abvsInner.getStartOffset(), XSDecimalPointable.TYPE_TRAITS.getFixedLength());
        decp2.setDecimal(longp2.longValue(), (byte) 0);
        operateDecimalDecimal(decp1, decp2, dOut);
    }

    @Override
    public void operateDecimalYMDuration(XSDecimalPointable decp, IntegerPointable intp, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateDoubleDecimal(DoublePointable doublep1, XSDecimalPointable decp2, DataOutput dOut)
            throws SystemException, IOException {
        double value = doublep1.doubleValue();
        value %= decp2.doubleValue();
        dOut.write(ValueTag.XS_DOUBLE_TAG);
        dOut.writeDouble(value);
    }

    @Override
    public void operateDoubleDouble(DoublePointable doublep1, DoublePointable doublep2, DataOutput dOut)
            throws SystemException, IOException {
        double value = doublep1.doubleValue();
        value %= doublep2.doubleValue();
        dOut.write(ValueTag.XS_DOUBLE_TAG);
        dOut.writeDouble(value);
    }

    @Override
    public void operateDoubleDTDuration(DoublePointable doublep1, LongPointable longp2, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateDoubleFloat(DoublePointable doublep, FloatPointable floatp, DataOutput dOut)
            throws SystemException, IOException {
        double value = doublep.doubleValue();
        value %= floatp.doubleValue();
        dOut.write(ValueTag.XS_DOUBLE_TAG);
        dOut.writeDouble(value);
    }

    @Override
    public void operateDoubleInteger(DoublePointable doublep, LongPointable longp, DataOutput dOut)
            throws SystemException, IOException {
        double value = doublep.doubleValue();
        value %= longp.doubleValue();
        dOut.write(ValueTag.XS_DOUBLE_TAG);
        dOut.writeDouble(value);
    }

    @Override
    public void operateDoubleYMDuration(DoublePointable doublep, IntegerPointable intp, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateDTDurationDate(LongPointable longp, XSDatePointable datep, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateDTDurationDatetime(LongPointable longp, XSDateTimePointable datetimep, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateDTDurationDecimal(LongPointable longp, XSDecimalPointable decp, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateDTDurationDouble(LongPointable longp, DoublePointable doublep, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateDTDurationDTDuration(LongPointable longp, LongPointable longp2, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateDTDurationFloat(LongPointable longp, FloatPointable floatp, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateDTDurationInteger(LongPointable longp1, LongPointable longp2, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateDTDurationTime(LongPointable longp1, XSTimePointable timep2, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateFloatDecimal(FloatPointable floatp, XSDecimalPointable decp, DataOutput dOut)
            throws SystemException, IOException {
        float value = floatp.floatValue();
        value %= decp.floatValue();
        dOut.write(ValueTag.XS_FLOAT_TAG);
        dOut.writeFloat(value);
    }

    @Override
    public void operateFloatDouble(FloatPointable floatp, DoublePointable doublep, DataOutput dOut)
            throws SystemException, IOException {
        double value = floatp.doubleValue();
        value %= doublep.doubleValue();
        dOut.write(ValueTag.XS_DOUBLE_TAG);
        dOut.writeDouble(value);
    }

    @Override
    public void operateFloatDTDuration(FloatPointable floatp, LongPointable longp, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateFloatFloat(FloatPointable floatp, FloatPointable floatp2, DataOutput dOut)
            throws SystemException, IOException {
        float value = floatp.floatValue();
        value %= floatp2.floatValue();
        dOut.write(ValueTag.XS_FLOAT_TAG);
        dOut.writeFloat(value);
    }

    @Override
    public void operateFloatInteger(FloatPointable floatp, LongPointable longp, DataOutput dOut)
            throws SystemException, IOException {
        float value = floatp.floatValue();
        value %= longp.floatValue();
        dOut.write(ValueTag.XS_FLOAT_TAG);
        dOut.writeFloat(value);
    }

    @Override
    public void operateFloatYMDuration(FloatPointable floatp, IntegerPointable intp, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateIntegerDecimal(LongPointable longp1, XSDecimalPointable decp2, DataOutput dOut)
            throws SystemException, IOException {
        abvsInner.reset();
        XSDecimalPointable decp1 = new XSDecimalPointable();
        decp1.set(abvsInner.getByteArray(), abvsInner.getStartOffset(), XSDecimalPointable.TYPE_TRAITS.getFixedLength());
        decp1.setDecimal(longp1.longValue(), (byte) 0);
        operateDecimalDecimal(decp1, decp2, dOut);
    }

    @Override
    public void operateIntegerDouble(LongPointable longp, DoublePointable doublep, DataOutput dOut)
            throws SystemException, IOException {
        double value = longp.doubleValue();
        value %= doublep.doubleValue();
        dOut.write(ValueTag.XS_DOUBLE_TAG);
        dOut.writeDouble(value);
    }

    @Override
    public void operateIntegerDTDuration(LongPointable longp1, LongPointable longp2, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateIntegerFloat(LongPointable longp, FloatPointable floatp, DataOutput dOut)
            throws SystemException, IOException {
        float value = longp.floatValue();
        value %= floatp.floatValue();
        dOut.write(ValueTag.XS_FLOAT_TAG);
        dOut.writeFloat(value);
    }

    @Override
    public void operateIntegerInteger(LongPointable longp, LongPointable longp2, DataOutput dOut)
            throws SystemException, IOException {
        long value = longp.getLong();
        value %= longp2.getLong();
        dOut.write(ValueTag.XS_INTEGER_TAG);
        dOut.writeLong(value);
    }

    @Override
    public void operateIntegerYMDuration(LongPointable longp, IntegerPointable intp, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateTimeDTDuration(XSTimePointable timep, LongPointable longp, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateTimeTime(XSTimePointable timep, XSTimePointable timep2, DynamicContext dCtx, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateYMDurationDate(IntegerPointable intp, XSDatePointable datep, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateYMDurationDatetime(IntegerPointable intp, XSDateTimePointable datetimep, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateYMDurationDecimal(IntegerPointable intp, XSDecimalPointable decp, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateYMDurationDouble(IntegerPointable intp, DoublePointable doublep, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateYMDurationFloat(IntegerPointable intp, FloatPointable floatp, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateYMDurationInteger(IntegerPointable intp, LongPointable longp, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void operateYMDurationYMDuration(IntegerPointable intp, IntegerPointable intp2, DataOutput dOut)
            throws SystemException, IOException {
        throw new UnsupportedOperationException();
    }

}