package com.five.Maeum_Eum.common;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class CustomFunctionContributor implements FunctionContributor {
    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {

        functionContributions.getFunctionRegistry()
                .register("bit_count", new StandardSQLFunction("bit_count", StandardBasicTypes.INTEGER));
    }
}