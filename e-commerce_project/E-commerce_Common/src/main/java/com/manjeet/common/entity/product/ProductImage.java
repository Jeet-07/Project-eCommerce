package com.manjeet.common.entity.product;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_images")
public class ProductImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(nullable = false)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	public ProductImage() {
	}

	public ProductImage(Integer id, String name, Product product) {
		this.id = id;
		this.name = name;
		this.product = product;
	}


	public ProductImage(String name, Product product) {
		this.name = name;
		this.product = product;
	}

	@Transient
	public String getImagePath() {
		return "/product-images/" + product.getId() + "/extras/" + this.name;
	}
	
}
