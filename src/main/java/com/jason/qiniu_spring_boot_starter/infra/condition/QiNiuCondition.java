package com.jason.qiniu_spring_boot_starter.infra.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

public class QiNiuCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String ak = context.getEnvironment().getProperty("qiniu.access-key");
        String sk = context.getEnvironment().getProperty("qiniu.secret-key");
        String bucketName = context.getEnvironment().getProperty("qiniu.bucket-name") ;
        String zone = context.getEnvironment().getProperty("qiniu.zone") ;

        if(StringUtils.isEmpty(ak)){
            throw new RuntimeException("Lack of qiniuyun configuration:access-key") ;
        }else if(StringUtils.isEmpty(sk)){
            throw new RuntimeException("Lack of qiniuyun configuration:secret-key") ;
        }else if(StringUtils.isEmpty(bucketName)){
            throw new RuntimeException("Lack of qiniuyun configuration:bucket-name") ;
        }else if(StringUtils.isEmpty(zone)){
            throw new RuntimeException("Lack of qiniuyun configuration:zone") ;
        }
        return true ;

    }
}
