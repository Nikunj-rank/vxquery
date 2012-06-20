package org.apache.vxquery.compiler.algebricks;

import java.util.List;

import org.apache.commons.lang3.mutable.Mutable;
import org.apache.vxquery.exceptions.SystemException;
import org.apache.vxquery.functions.Function;

import edu.uci.ics.hyracks.algebricks.common.exceptions.AlgebricksException;
import edu.uci.ics.hyracks.algebricks.core.algebra.base.ILogicalExpression;
import edu.uci.ics.hyracks.algebricks.core.algebra.expressions.AggregateFunctionCallExpression;
import edu.uci.ics.hyracks.algebricks.core.algebra.expressions.ConstantExpression;
import edu.uci.ics.hyracks.algebricks.core.algebra.expressions.IExpressionRuntimeProvider;
import edu.uci.ics.hyracks.algebricks.core.algebra.expressions.IVariableTypeEnvironment;
import edu.uci.ics.hyracks.algebricks.core.algebra.expressions.ScalarFunctionCallExpression;
import edu.uci.ics.hyracks.algebricks.core.algebra.expressions.StatefulFunctionCallExpression;
import edu.uci.ics.hyracks.algebricks.core.algebra.expressions.UnnestingFunctionCallExpression;
import edu.uci.ics.hyracks.algebricks.core.algebra.expressions.VariableReferenceExpression;
import edu.uci.ics.hyracks.algebricks.core.algebra.operators.logical.IOperatorSchema;
import edu.uci.ics.hyracks.algebricks.core.jobgen.impl.JobGenContext;
import edu.uci.ics.hyracks.algebricks.runtime.base.IAggregateEvaluatorFactory;
import edu.uci.ics.hyracks.algebricks.runtime.base.ICopySerializableAggregateFunctionFactory;
import edu.uci.ics.hyracks.algebricks.runtime.base.IRunningAggregateEvaluatorFactory;
import edu.uci.ics.hyracks.algebricks.runtime.base.IScalarEvaluatorFactory;
import edu.uci.ics.hyracks.algebricks.runtime.base.IUnnestingEvaluatorFactory;
import edu.uci.ics.hyracks.algebricks.runtime.evaluators.ConstantEvaluatorFactory;
import edu.uci.ics.hyracks.algebricks.runtime.evaluators.TupleFieldEvaluatorFactory;

public class VXQueryExpressionRuntimeProvider implements IExpressionRuntimeProvider {
    @Override
    public IScalarEvaluatorFactory createEvaluatorFactory(ILogicalExpression expr, IVariableTypeEnvironment env,
            IOperatorSchema[] inputSchemas, JobGenContext context) throws AlgebricksException {
        switch (expr.getExpressionTag()) {
            case CONSTANT:
                VXQueryConstantValue cv = (VXQueryConstantValue) ((ConstantExpression) expr).getValue();
                return new ConstantEvaluatorFactory(cv.getValue());

            case VARIABLE:
                VariableReferenceExpression vrExpr = (VariableReferenceExpression) expr;
                int tupleFieldIndex = inputSchemas[0].findVariable(vrExpr.getVariableReference());
                return new TupleFieldEvaluatorFactory(tupleFieldIndex);

            case FUNCTION_CALL:
                ScalarFunctionCallExpression fcExpr = (ScalarFunctionCallExpression) expr;
                Function fn = (Function) fcExpr.getFunctionInfo();

                List<Mutable<ILogicalExpression>> args = fcExpr.getArguments();
                IScalarEvaluatorFactory[] argFactories = new IScalarEvaluatorFactory[args.size()];
                for (int i = 0; i < argFactories.length; ++i) {
                    Mutable<ILogicalExpression> arg = args.get(i);
                    argFactories[i] = createEvaluatorFactory(arg.getValue(), env, inputSchemas, context);
                }

                try {
                    return fn.createScalarEvaluatorFactory(argFactories);
                } catch (SystemException e) {
                    throw new AlgebricksException(e);
                }
        }
        throw new UnsupportedOperationException("Cannot create runtime for " + expr.getExpressionTag());
    }

    @Override
    public IAggregateEvaluatorFactory createAggregateFunctionFactory(AggregateFunctionCallExpression expr,
            IVariableTypeEnvironment env, IOperatorSchema[] inputSchemas, JobGenContext context)
            throws AlgebricksException {
        return null;
    }

    @Override
    public ICopySerializableAggregateFunctionFactory createSerializableAggregateFunctionFactory(
            AggregateFunctionCallExpression expr, IVariableTypeEnvironment env, IOperatorSchema[] inputSchemas,
            JobGenContext context) throws AlgebricksException {
        return null;
    }

    @Override
    public IRunningAggregateEvaluatorFactory createRunningAggregateFunctionFactory(StatefulFunctionCallExpression expr,
            IVariableTypeEnvironment env, IOperatorSchema[] inputSchemas, JobGenContext context)
            throws AlgebricksException {
        return null;
    }

    @Override
    public IUnnestingEvaluatorFactory createUnnestingFunctionFactory(UnnestingFunctionCallExpression expr,
            IVariableTypeEnvironment env, IOperatorSchema[] inputSchemas, JobGenContext context)
            throws AlgebricksException {
        return null;
    }
}