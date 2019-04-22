package com.m2micro.m2mfa.common.aspect;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class JdbcAspect {


    @Pointcut("execution(public * org.springframework.jdbc.core.JdbcTemplate.query*(..))")
    public void query() {

    }

    @Around("query()")
    public Object error(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();

        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        //请求的参数
        Object[] args = point.getArgs();
        //将该方法的参数设置到context中
        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] params = discoverer.getParameterNames(method);
        EvaluationContext context = new StandardEvaluationContext();

        for (int len = 0; len < params.length; len++) {
            context.setVariable(params[len], args[len]);
        }
        //解析spel表达式
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("#sql");
        String sql = expression.getValue(context, String.class);


        //执行方法
        Object result = point.proceed();

        if(StringUtils.isEmpty(sql)){
            return result;
        }
        System.out.println("Jdbc Query Execute Sql:"+sql);
        return result;
    }
}
