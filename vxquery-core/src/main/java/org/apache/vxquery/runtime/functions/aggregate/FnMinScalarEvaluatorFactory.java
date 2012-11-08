/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.vxquery.runtime.functions.aggregate;

import org.apache.vxquery.context.DynamicContext;
import org.apache.vxquery.datamodel.accessors.SequencePointable;
import org.apache.vxquery.datamodel.accessors.TaggedValuePointable;
import org.apache.vxquery.datamodel.values.ValueTag;
import org.apache.vxquery.exceptions.SystemException;
import org.apache.vxquery.runtime.functions.base.AbstractTaggedValueArgumentScalarEvaluator;
import org.apache.vxquery.runtime.functions.base.AbstractTaggedValueArgumentScalarEvaluatorFactory;
import org.apache.vxquery.runtime.functions.comparison.AbstractValueComparisonOperation;
import org.apache.vxquery.runtime.functions.comparison.ValueLtComparisonOperation;
import org.apache.vxquery.runtime.functions.util.FunctionHelper;

import edu.uci.ics.hyracks.algebricks.common.exceptions.AlgebricksException;
import edu.uci.ics.hyracks.algebricks.runtime.base.IScalarEvaluator;
import edu.uci.ics.hyracks.algebricks.runtime.base.IScalarEvaluatorFactory;
import edu.uci.ics.hyracks.api.context.IHyracksTaskContext;
import edu.uci.ics.hyracks.data.std.api.IPointable;
import edu.uci.ics.hyracks.data.std.primitive.VoidPointable;

public class FnMinScalarEvaluatorFactory extends AbstractTaggedValueArgumentScalarEvaluatorFactory {
    private static final long serialVersionUID = 1L;

    public FnMinScalarEvaluatorFactory(IScalarEvaluatorFactory[] args) {
        super(args);
    }

    @Override
    protected IScalarEvaluator createEvaluator(IHyracksTaskContext ctx, IScalarEvaluator[] args)
            throws AlgebricksException {
        final DynamicContext dCtx = (DynamicContext) ctx.getJobletContext().getGlobalJobData();
        final SequencePointable seqp = (SequencePointable) SequencePointable.FACTORY.createPointable();
        final AbstractValueComparisonOperation aOp = new ValueLtComparisonOperation();
        final TaggedValuePointable tvpMin = (TaggedValuePointable) TaggedValuePointable.FACTORY.createPointable();
        final TaggedValuePointable tvpNext = (TaggedValuePointable) TaggedValuePointable.FACTORY.createPointable();
        final VoidPointable p = (VoidPointable) VoidPointable.FACTORY.createPointable();

        return new AbstractTaggedValueArgumentScalarEvaluator(args) {
            @Override
            protected void evaluate(TaggedValuePointable[] args, IPointable result) throws SystemException {
                // TODO Update the results to be based on specs when different types in sequence.
                TaggedValuePointable tvp = args[0];
                if (tvp.getTag() == ValueTag.SEQUENCE_TAG) {
                    tvp.getValue(seqp);
                    int seqLen = seqp.getEntryCount();
                    for (int j = 0; j < seqLen; ++j) {
                        seqp.getEntry(j, p);
                        tvpNext.set(p.getByteArray(), p.getStartOffset(), p.getLength());
                        if (j == 0) {
                            // Init.
                            tvpMin.set(tvpNext);
                        }
                        if (FunctionHelper.compareTaggedValues(aOp, tvpNext, tvpMin, dCtx)) {
                            tvpMin.set(tvpNext);
                        }
                    }
                    result.set(tvpMin);
                } else {
                    result.set(tvp);
                }
            }
        };
    }
}