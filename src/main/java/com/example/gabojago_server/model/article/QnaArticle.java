package com.example.gabojago_server.model.article;

import com.example.gabojago_server.model.common.BooleanToYNConverter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@DiscriminatorValue("QnaArticle")
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QnaArticle extends Article {

    @Column(name = "isSelected")
    @Convert(converter = BooleanToYNConverter.class)
    private boolean isSelected;

}