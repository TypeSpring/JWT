package com.typespring.example.member;

import javax.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Authority authority = Authority.ROLE_USER;

    public enum Authority {
        ROLE_USER, ROLE_ADMIN
    }

    @Builder
    public Member(final String email, final String password, final Authority authority) {
        this.email = email;
        this.password = password;
        this.authority = authority;
    }
}
