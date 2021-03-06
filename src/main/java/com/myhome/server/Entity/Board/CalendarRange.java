package com.myhome.server.Entity.Board;

import com.myhome.server.Entity.Member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter @Setter
public class CalendarRange {

    @Id @GeneratedValue
    @Column(name = "calendar_range_id")
    private Long id;

    @Column(name = "start_date",length = 255)
    private LocalDate start_date;
    @Column(name = "end_date",length = 255)
    private LocalDate end_date;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="member_id")
    private Member member;
}
