package tech.dut.safefood.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import tech.dut.safefood.enums.ShopEnum;
import tech.dut.safefood.enums.UserEnum;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "shop")
@Data
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "banner", length = 50)
    private String banner;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "schedule_active", length = 50)
    private String scheduleActive;

    @Size(max = 50)
    @Column(name = "phone", length = 50)
    private String phone;

    @Column(name = "start_rating")
    private Double startRating;

    @Column(name = "ratings")
    private Double ratings;

    @PrePersist
    private void PrePersist(){
        if(scheduleActive == null){
            scheduleActive = "CLOSE";
        }
        if(ratings ==null){
            ratings = 0D;
        }
        if(startRating ==null){
            startRating = 0D;
        }
    }

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "modified_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    @OneToOne(mappedBy = "shop", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Address addresses;

    @OneToMany(mappedBy = "shop")
    private Set<ShopFauvourite> shopFauvourites = new LinkedHashSet<>();

    @OneToMany(mappedBy = "shop")
    private List<Voucher> voucherShops;

    @OneToMany(mappedBy = "shop")
    private List<Product> products;

    @OneToMany(mappedBy = "shop")
    private List<ShopImage> shopImages;
}