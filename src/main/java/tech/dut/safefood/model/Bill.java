package tech.dut.safefood.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bill")
@Data
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @OneToMany(cascade = CascadeType.REMOVE)
    private List<BillItem> billItems;

    @Column(name = "total_origin", precision = 10)
    private BigDecimal totalOrigin;

    @Column(name = "total_voucher", precision = 10)
    private BigDecimal totalVoucher;

    @Column(name = "total_payment", precision = 10)
    private BigDecimal totalPayment;

    @Size(max = 50)
    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "note", length = 255)
    private String note;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "modified_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    @Column(name = "code")
    private String code;

    @Column(name = "expired_code")
    private LocalDateTime expiredCode;

    @Column(name = "is_rating")
    private Boolean isRating;

    @Column(name = "ratings")
    private Double ratings;

    @PrePersist
    private void PrePersist(){
        if(isRating == null){
            isRating = false;
        }
        if(ratings ==null){
            ratings = 0D;
        }
    }
}