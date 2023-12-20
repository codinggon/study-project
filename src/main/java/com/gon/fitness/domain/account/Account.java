package com.gon.fitness.domain.account;

import com.gon.fitness.domain.tag.Tag;
import com.gon.fitness.domain.zone.Zone;
import com.gon.fitness.web.account.security.UserAccount;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    private boolean emailVerified; //email 검증 여부

    private String emailCheckToken;

    private LocalDateTime emailCheckTokenGeneratedAt;

    private LocalDateTime joinedAt;

    private String bio;

    private String url;

    private String occupation;

    private String location;

    @Column(columnDefinition = "TEXT")
    private String profileImage;

    private boolean studyCreatedByEmail;

    private boolean studyCreatedByWeb = true;

    private boolean studyEnrollmentResultByEmail;

    private boolean studyEnrollmentResultByWeb = true;

    private boolean studyUpdatedByEmail;

    private boolean studyUpdatedByWeb = true;

    @ManyToMany
    private Set<Tag> tags;

    @ManyToMany
    private Set<Zone> zones;


    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }


    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }

    public void verifiedEmail() {
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();
    }


    public void addTags(Tag tag) {
        this.getTags().add(tag);
    }

    public void addZones(Zone zone) {
        this.getZones().add(zone);
    }

    public void removeZones(Zone zone) {
        this.getZones().remove(zone);
    }


}
