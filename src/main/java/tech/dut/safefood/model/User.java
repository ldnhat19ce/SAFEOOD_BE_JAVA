package tech.dut.safefood.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import tech.dut.safefood.enums.AuthProvider;
import tech.dut.safefood.enums.UserEnum;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "modified_at")
    @UpdateTimestamp
    private Instant modifiedAt;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;


    @Enumerated(value = EnumType.STRING)
    private AuthProvider provider;

    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    private UserEnum.Status status;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "expired_time")
    private Instant expiredTime;

    @Column(name = "oauth_id")
    private String oauthId;

    @PrePersist
    private void PrePersist() {
        if (status == null) {
            status = UserEnum.Status.INACTIVE;
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user",  cascade = CascadeType.MERGE)
    private UserInformation userInformation;


    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private Shop shop;

    @ManyToMany
    @JoinTable(name = "voucher_user",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "voucher_id", referencedColumnName = "id"))
    private List<Voucher> vouchers;

    @OneToMany(mappedBy = "user")
    private List<ShopFauvourite> shopFauvourites;

    private Boolean isLogin;
    private String deviceToken;
}