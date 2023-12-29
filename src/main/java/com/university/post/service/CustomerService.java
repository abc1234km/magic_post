package com.university.post.service;

import com.university.post.model.Customer;
import com.university.post.model.DeliveryPoint;
import com.university.post.payload.request.customer.CustomerRequest;

public interface CustomerService {
    public Customer addCustomer(Customer customer);

    Customer updateCustomer(Customer customer, CustomerRequest customerRequest, String type);

    public Customer handleCustomerRegister(CustomerRequest customerRequest, String type);

    public Customer getCustomerBydId(String id);
}
