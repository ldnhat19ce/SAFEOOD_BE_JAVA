package tech.dut.safefood.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "news")
@Data
public class News {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    @Column(name = "title")
    private String title;

    @Column(name = "location")
    private String location;

    @Column(name = "content")
    private String content;

    @Column(name = "address")
    private String address;

    @Column(name = "image")
    private String image;

    @Column(name = "sub_title")
    private String subTitle;
}
