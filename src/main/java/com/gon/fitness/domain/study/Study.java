package com.gon.fitness.domain.study;

import com.gon.fitness.domain.account.Account;
import com.gon.fitness.domain.tag.Tag;
import com.gon.fitness.domain.zone.Zone;
import com.gon.fitness.web.account.security.UserAccount;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //스터디 권한을 일반 유저한테 위임할 경우를 대비 -> 다대다
    @ManyToMany
    private Set<Account> managers = new HashSet<>();

    @ManyToMany
    private Set<Account> members = new HashSet<>();

    @Column(unique = true, nullable = false)
    private String path ;

    private String title;

    private String shortDescription;

    @Column(columnDefinition = "TEXT") @Basic(fetch = FetchType.EAGER)
    private String fullDescription;

    @Column(columnDefinition = "TEXT") @Basic(fetch = FetchType.EAGER)
    private String image;

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    private Set<Zone> zones = new HashSet<>();

    private LocalDateTime publishedDateTime;

    private LocalDateTime closedDateTime;

    private LocalDateTime recruitingUpdatedDateTime;

    private boolean recruiting;
    private boolean published;
    private boolean closed;
    private boolean useBanner;

    private String keyword;

    private Integer memberCount;


    /**
     * 스터디 공개 && 맴버모집 가능 && ( 맴버 or 매니저가 아니면 )
     */
    public boolean isJoinable(UserAccount userAccount) {
        Account account = userAccount.getAccount();
        return this.published && this.recruiting && !this.members.contains(account) && !managers.contains(account);
    }

    public boolean isMember(UserAccount userAccount) {
        Account account = userAccount.getAccount();
        return this.members.contains(account);
    }

    public boolean isManager(UserAccount userAccount) {
        Account account = userAccount.getAccount();
        return this.managers.contains(account);
    }

    public void addManager(Account account) {
        this.getManagers().add(account);
    }


    public void changeStudyDesc(String shortDescription, String fullDescription) {
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
    }
}
