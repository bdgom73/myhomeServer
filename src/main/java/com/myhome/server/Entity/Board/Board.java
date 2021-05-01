package com.myhome.server.Entity.Board;

import com.myhome.server.Entity.Member.Member;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Text;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Board {

    @Id
    @GeneratedValue @Column(name = "board_id")
    private Long id;
    private String title;
    @Column(columnDefinition = "Text")
    private String description;
    private int views;
    private int recommend;
    private LocalDateTime created;
    private LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
