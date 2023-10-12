package tech.dut.safefood.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "payment")
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @Size(max = 1000)
    @Column(name = "payment_info", length = 1000)
    private String paymentInfo;

    @Column(name = "status", length = 255)
    private String status;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "txnref")
    private String txnref;

    @Column(name = "response_code")
    private String responseCode;


    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Instant createdAt;


    @Column(name = "modified_at", nullable = false)
    @UpdateTimestamp
    private Instant modifiedAt;

    @OneToMany(mappedBy = "payment")
    private List<Bill> bill;

}