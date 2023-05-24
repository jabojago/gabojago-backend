package com.example.gabojago_server.service.alarm;

import com.example.gabojago_server.dto.response.member.AlarmResponseDto;
import com.example.gabojago_server.event.AlarmEvent;
import com.example.gabojago_server.model.alarm.AlarmEntity;
import com.example.gabojago_server.model.alarm.AlarmType;
import com.example.gabojago_server.model.article.Article;
import com.example.gabojago_server.model.articlecomment.ArticleComment;
import com.example.gabojago_server.model.like.LikeEntity;
import com.example.gabojago_server.model.member.Member;
import com.example.gabojago_server.repository.alarm.AlarmRepository;
import com.example.gabojago_server.service.common.EntityFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final EntityFinder entityFinder;

    public Page<AlarmResponseDto> findAlarms(Long memberId, Pageable pageable) {
        return alarmRepository.findByMember(memberId, pageable)
                .map(alarm -> AlarmResponseDto.from(
                        alarm,
                        entityFinder.findArticle(alarm.getTargetId()),
                        entityFinder.findMember(alarm.getPublisherId())));
    }

    @EventListener(AlarmEvent.class)
    public void makeAlarm(AlarmEvent alarmEvent) {
        switch (alarmEvent.getAlarmType()) {
            case COMMENT:
                makeCommentAlarm(alarmEvent.getTargetId());
                return;
            case LIKE:
                makeLikeAlarm(alarmEvent.getTargetId());
        }
    }

    public void makeCommentAlarm(Long commentId) {
        ArticleComment articleComment = entityFinder.findComment(commentId);
        Member commentWriter = articleComment.getWriter();
        Article article = articleComment.getArticle();
        Member member = article.getWriter();
        if (member.equals(commentWriter)) return;
        alarmRepository.save(AlarmEntity.builder()
                .member(member)
                .publisherId(commentWriter.getId())
                .targetId(article.getId())
                .alarmType(AlarmType.COMMENT)
                .build());
    }

    public void makeLikeAlarm(Long likeId) {
        LikeEntity like = entityFinder.findLike(likeId);
        Member liker = like.getMember();
        Article article = like.getArticle();
        Member member = article.getWriter();
        if (member.equals(liker)) return;
        alarmRepository.save(AlarmEntity.builder()
                .member(member)
                .publisherId(liker.getId())
                .targetId(article.getId())
                .alarmType(AlarmType.LIKE)
                .build());
    }

}
