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
import com.example.gabojago_server.repository.article.article.ArticleRepository;
import com.example.gabojago_server.repository.articlecomment.ArticleCommentRepository;
import com.example.gabojago_server.repository.like.LikeRepository;
import com.example.gabojago_server.repository.member.MemberRepository;
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
    private final ArticleCommentRepository articleCommentRepository;
    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    public Page<AlarmResponseDto> findAlarms(Long memberId, Pageable pageable) {
        return alarmRepository.findByMember(memberId, pageable)
                .map(alarm -> AlarmResponseDto.from(
                        alarm,
                        getArticle(alarm.getTargetId()),
                        getMember(alarm.getPublisherId())));
    }

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(IllegalStateException::new);
    }

    public Article getArticle(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(IllegalStateException::new);
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
        ArticleComment articleComment = articleCommentRepository.findById(commentId).orElseThrow(IllegalStateException::new);
        Member commentWriter = articleComment.getWriter();
        Article article = articleComment.getArticle();
        Member member = article.getWriter();
        alarmRepository.save(AlarmEntity.builder()
                .member(member)
                .publisherId(commentWriter.getId())
                .targetId(article.getId())
                .alarmType(AlarmType.COMMENT)
                .build());
    }

    public void makeLikeAlarm(Long likeId) {
        LikeEntity like = likeRepository.findById(likeId).orElseThrow(IllegalStateException::new);
        Member liker = like.getMember();
        Article article = like.getArticle();
        Member member = article.getWriter();
        alarmRepository.save(AlarmEntity.builder()
                .member(member)
                .publisherId(liker.getId())
                .targetId(article.getId())
                .alarmType(AlarmType.LIKE)
                .build());
    }

}
