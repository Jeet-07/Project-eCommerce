package com.manjeet.common.entity;

import jakarta.persistence.*;

import com.manjeet.common.entity.product.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cart_items")
public class CartItem extends IdBasedEntity {
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@ManyToOne
	@JoinColumn(name = "product_id")	
	private Product product;
	
	private int quantity;
	
	@Transient
	private float shippingCost;
	
	public CartItem() {
	}

	@Override
	public String toString() {
		return "CartItem [id=" + id + ", customer=" + customer.getFullName() + ", product=" + product.getShortName() + ", quantity=" + quantity
				+ "]";
	}

	@Transient
	public float getSubtotal() {
		return product.getDiscountPrice() * quantity;
	}
	
	
}
