package com.example.gabojago_server.model.alarm;

import com.example.gabojago_server.model.common.BaseTimeEntity;
import com.example.gabojago_server.model.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Table(name = "alarm_entity")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Column(nullable = false)
    private Long targetId;

    @Column(nullable = false)
    private Long publisherId;

    @Builder
    public AlarmEntity(Member member, AlarmType alarmType, Long targetId, Long publisherId) {
        this.member = member;
        this.alarmType = alarmType;
        this.targetId = targetId;
        this.publisherId = publisherId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AlarmEntity)) return false;
        AlarmEntity that = (AlarmEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


