package com.m2micro.m2mfa.common.aspect;


import com.m2micro.framework.authorization.TokenInfo;
import com.m2micro.m2mfa.common.entity.CommonEntity;
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
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class GroupIdAspect {

    /*@Pointcut("execution(public * com.m2micro.m2mfa..*.save(..))")
    public void save() {

    }*/


    @Pointcut("execution(public * org.springframework.data.repository.CrudRepository.save(..))")
    public void save() {

    }

    @Around("save()")
    public Object setGroupId(ProceedingJoinPoint point) throws Throwable {
        //请求的参数
        Object[] args = point.getArgs();
        if(args!=null&&args.length==1){
            Object obj = args[0];
            if(obj instanceof CommonEntity){
                CommonEntity commonEntity=(CommonEntity) obj;
                commonEntity.setGroupId(TokenInfo.getUserGroupId());
                return point.proceed(args);
            }
        }
        return point.proceed();
    }
}
