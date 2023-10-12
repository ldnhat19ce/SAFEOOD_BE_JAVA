package tech.dut.safefood.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "voucher_user")
@Data
public class VoucherUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "used")
    private Long used;

}