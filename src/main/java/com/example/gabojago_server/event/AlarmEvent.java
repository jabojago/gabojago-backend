package com.example.gabojago_server.event;

import com.example.gabojago_server.model.alarm.AlarmType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AlarmEvent extends ApplicationEvent {
    private Long targetId;
    private AlarmType alarmType;

    public AlarmEvent(Object source, Long targetId, AlarmType alarmType) {
        super(source);
        this.targetId = targetId;
        this.alarmType = alarmType;
    }
}
