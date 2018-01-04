package io.zipcoder.tc_spring_poll_application.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Poll {

    @Id
    @GeneratedValue
    @Column(name = "POLL_ID")
    Long id;

    @Column(name = "POLL_QUESTION")
    String question;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "POLL_ID")
    @OrderBy
    Set<Option> options;

    

}
