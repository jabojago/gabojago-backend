package com.example.gabojago_server.aop;

import com.example.gabojago_server.dto.response.comment.CommentResponseDto;
import com.example.gabojago_server.dto.response.like.LikeResponseDto;
import com.example.gabojago_server.error.ErrorCode;
import com.example.gabojago_server.error.GabojagoException;
import com.example.gabojago_server.event.AlarmEvent;
import com.example.gabojago_server.model.alarm.AlarmType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AlarmPublisher {

    private final ApplicationEventPublisher eventPublisher;

    @AfterReturning(value = "@annotation(com.example.gabojago_server.aop.Alarm)", returning = "result")
    public void doReturning(JoinPoint joinPoint, Object result) {
        AlarmArgs alarmArgs = typeConvert(result);
        eventPublisher.publishEvent(new AlarmEvent(joinPoint.getTarget(), alarmArgs.id, alarmArgs.alarmType));
    }

    private AlarmArgs typeConvert(Object result) {
        if (result instanceof CommentResponseDto) {
            CommentResponseDto comment = (CommentResponseDto) result;
            return new AlarmArgs(comment.getCommentId(), AlarmType.COMMENT);
        } else if (result instanceof LikeResponseDto) {
            LikeResponseDto like = (LikeResponseDto) result;
            return new AlarmArgs(like.getLikeId(), AlarmType.LIKE);
        }
        throw new GabojagoException(ErrorCode.UNSUPPORTED_ALARM);
    }

    static class AlarmArgs {
        private Long id;
        private AlarmType alarmType;

        public AlarmArgs(Long id, AlarmType alarmType) {
            this.id = id;
            this.alarmType = alarmType;
        }
    }

}
