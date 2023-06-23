package com.curenosm.chapter3;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer {

	private String name;

	private List<Order> orders = new ArrayList<>();

	public Customer(String name) {
		this.name = name;
	}

	public Customer addOrder(Order order) {
		orders.add(order);
		return this;
	}

}
