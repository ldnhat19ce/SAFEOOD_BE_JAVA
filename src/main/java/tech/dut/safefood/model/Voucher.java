package tech.dut.safefood.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "voucher")
@Data
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "delete_flag")
    private Boolean deleteFlag;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "ended_at", nullable = false)
    private LocalDateTime endedAt;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "value_discount")
    private BigDecimal valueDiscount;

    @Column(name = "voucher_type")
    private String voucherType;

    @Column(name = "value_need")
    private BigDecimal valueNeed;

    @Column(name = "limit_peruser")
    private Long limitPerUser;

    @Column(name = "max_discount")
    private BigDecimal maxDiscount;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "image")
    private String image;

    @Column(name = "description")
    private String description;

    @PrePersist
    private void PrePersist() {
        if (deleteFlag == null) {
            deleteFlag = false;
        }
    }

    @ManyToMany
    @JoinTable(name = "voucher_user",
            joinColumns = @JoinColumn(name = "voucher_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", referencedColumnName = "id")
    private Shop shop;

    @OneToMany(mappedBy = "voucher")
    private List<Bill> bill;
}