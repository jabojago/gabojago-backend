package com.example.gabojago_server.dto.response.member;

import com.example.gabojago_server.model.alarm.AlarmEntity;
import com.example.gabojago_server.model.alarm.AlarmType;
import com.example.gabojago_server.model.article.Article;
import com.example.gabojago_server.model.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlarmResponseDto {
    private String message; // 메시지
    private AlarmType alarmType; // 알람 타입
    private String publisherNickname; // 댓글 혹은 좋아요를 누른 사용자
    private String nickname; // 게시글 주인
    private Long postId; // 알람이 발생한 게시글

    @Builder
    public AlarmResponseDto(String message, AlarmType alarmType, String publisherNickname, String nickname, Long postId) {
        this.message = message;
        this.alarmType = alarmType;
        this.publisherNickname = publisherNickname;
        this.nickname = nickname;
        this.postId = postId;
    }

    public static AlarmResponseDto from(AlarmEntity alarm, Article article, Member publisher) {
        Member member = alarm.getMember();
        AlarmType alarmType = alarm.getAlarmType();
        String message = publisher.getNickname() + " 님이 " + member.getNickname() + " 님의 " + article.getTitle() + "게시글에 " + alarmType.getMessage() + "을/를 남겼습니다.";
        return AlarmResponseDto.builder()
                .message(message)
                .nickname(member.getNickname())
                .alarmType(alarmType)
                .publisherNickname(publisher.getNickname())
                .postId(article.getId())
                .build();
    }
}
