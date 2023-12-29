package com.university.post.service.impl;

import com.university.post.dataset.DeliveryPointDS;
import com.university.post.enums.Type;
import com.university.post.exception.entity.EntityNotFound;
import com.university.post.model.Customer;
import com.university.post.model.DeliveryPoint;
import com.university.post.model.User;
import com.university.post.payload.request.customer.CustomerRequest;
import com.university.post.repository.CustomerRepository;
import com.university.post.service.CustomerService;
import com.university.post.service.DeliveryPointService;
import com.university.post.utils.Constant;
import com.university.post.utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DeliveryPointService deliveryPointService;

    @Override
    @Transactional
    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Customer customer, CustomerRequest customerRequest, String type) {
        customer.setName(customerRequest.getName());
        customer.setAddress(customerRequest.getAddress());
        customer.setPhoneNo(customerRequest.getPhoneNo());
        customer.setTypeResource(customer.getTypeResource());

        if (type.equals(Type.RECEIVER_CUSTOMER_LOCATION)) {
            DeliveryPointDS deliveryPointDS = new DeliveryPointDS();
            deliveryPointDS.setType(Type.TRANSACTION_POINT_LOCATION);
            List<DeliveryPoint> deliveryPointList = deliveryPointService.getAllDeliveryPoint(deliveryPointDS);
            int minDistance = 10000;
            int index = 0;

            for (int i = 0; i < deliveryPointList.size(); i++) {
                String addressPoint = deliveryPointList.get(i).getAddress();
                Integer distance = Math
                        .toIntExact(Utils.getLocationDistance(customerRequest.getAddress(), addressPoint));

                if (distance < minDistance) {
                    minDistance = distance;
                    index = i;
                }
            }
            customer.setTransactionPoint(deliveryPointList.get(index));
        }

        return customerRepository.save(customer);
    }

    @Override
    public Customer handleCustomerRegister(CustomerRequest customerRequest, String type) {
        Customer customer = new Customer();
        Long nextId = customerRepository.getNextId();
        customer.setResourceId(Constant.POINT_PREFIX_ID + nextId);
        customer.setName(customerRequest.getName());
        customer.setPhoneNo(customerRequest.getPhoneNo());
        customer.setAddress(customerRequest.getAddress());
        customer.setTypeResource(type);
        if (type.equals(Type.RECEIVER_CUSTOMER_LOCATION)) {
            DeliveryPointDS deliveryPointDS = new DeliveryPointDS();
            deliveryPointDS.setPageSize(100);
            deliveryPointDS.setType(Type.TRANSACTION_POINT_LOCATION);

            List<DeliveryPoint> deliveryPointList = deliveryPointService.getAllDeliveryPointByPage(deliveryPointDS)
                    .getContent();
            DeliveryPoint transactionPoint = deliveryPointList.stream().min(Comparator.comparingInt(
                    point -> Math
                            .toIntExact(Utils.getLocationDistance(customerRequest.getAddress(), point.getAddress()))))
                    .get();
            customer.setTransactionPoint(transactionPoint);
        } else {
            User user = Utils.getUserAuthentication();
            customer.setTransactionPoint(user.getWorkAt());
        }
        return customer;
    }

    @Override
    public Customer getCustomerBydId(String id) {
        return customerRepository.findById(id).orElseThrow(() -> new EntityNotFound("Không tìm thấy"));
    }
}
