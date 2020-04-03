package com.nuts.framework.aspects;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.core.config.Order;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson.JSON;
import com.nuts.framework.annotation.ParamsNotNull;
import com.nuts.framework.exception.ParamsNullPointException;

/**
 * 〈一句话简述功能〉<br>
 * 〈控制层日志打印，并验证参数是否为空〉
 *
 * @author liyi-x6063
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Aspect
@Component
@Order(1)
public class LogParamsAspect {

    private Logger logger = LoggerFactory.getLogger(LogParamsAspect.class);

    @Pointcut("(execution(public * com.nuts.casemanage..*Controller.*(..))) or (execution(public * com.nuts.cep..*Controller.*(..)))")
    private void pointCutMethod() {}


    @Around(value = "pointCutMethod()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取连接点的方法签名对象
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        // 获取参数名称
        Parameter[] parameters = method.getParameters();
        // 参数
        Object[] args = joinPoint.getArgs();

        // 必传的属性是否含有空字符串
        boolean isHasNullParams = false;
        StringBuilder requestParams = new StringBuilder();
        if (!ArrayUtils.isEmpty(parameters) && ArrayUtils.isSameLength(parameters, args)) {
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                ParamsNotNull paramsNotNull = parameter.getDeclaredAnnotation(ParamsNotNull.class);
                if (Objects.nonNull(paramsNotNull) && StringUtils.isNotEmpty(paramsNotNull.value())) {
                    requestParams.append(paramsNotNull.value());
                } else {
                    requestParams.append(parameter.getName());
                }
                if (Objects.nonNull(paramsNotNull) && ObjectUtils.isEmpty(args[i])) {
                    isHasNullParams = true;
                }
//                requestParams.append("=").append(JSON.toJSONString(args[i]));
                requestParams.append(", ");
            }
        }

        // 获取目标方法名称
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();
        long startTime = System.currentTimeMillis();
        logger.info("className:[{}]  method:[{}]  opId:[{}]  -- start requestParams:[{}]", className, methodName,
                startTime, requestParams.toString());

        // 验证参数是否为空
        if (isHasNullParams) {
            throw new ParamsNullPointException("传入的参数含有空值");
        }

        // 执行方法
        Object proceed = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            logger.debug("请求结果 result = {}", JSON.toJSONString(proceed));
        }
        logger.info("className:[{}] method:[{}]  opId:[{}] -- end costTime:[{}(ms)]", className, methodName, startTime,
                endTime - startTime);
        return proceed;
    }
}
