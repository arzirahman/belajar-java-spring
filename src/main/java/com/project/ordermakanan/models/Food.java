package com.project.ordermakanan.models;

import java.sql.Timestamp;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "foods")
@Builder
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private int foodId;

    @Column(name = "food_name")
    private String foodName;

    @Column(name = "image_filename")
    private String imageFilename;

    @Column(name = "price")
    private Integer price;

    @Column(name = "created_by")
    private String createdBy;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time")
    private Timestamp createdTime;

    @Column(name = "modified_by")
    private String modifiedBy;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_time")
    private Timestamp modifiedTime;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    @OneToMany(mappedBy = "food")
    private Set<Cart> carts;

    @OneToMany(mappedBy = "food")
    private Set<FavoriteFood> favoriteFoods;

    @OneToMany(mappedBy = "food")
    private Set<OrderDetail> orderDetails;
}