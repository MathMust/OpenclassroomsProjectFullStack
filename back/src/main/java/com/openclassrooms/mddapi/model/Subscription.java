package com.openclassrooms.mddapi.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "subscription")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;
}
