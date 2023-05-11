package com.example.gabojago_server.repository.alarm;

import com.example.gabojago_server.model.alarm.AlarmEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlarmRepository extends JpaRepository<AlarmEntity, Long> {

    @Query("select alarm from AlarmEntity alarm " +
            " join alarm.member m" +
            " where m.id = :member")
    Page<AlarmEntity> findByMember(@Param("member") Long memberId, Pageable pageable);
}
