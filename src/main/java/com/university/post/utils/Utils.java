package com.university.post.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.university.post.dto.CustomerDTO;
import com.university.post.dto.DeliveryPointDTO;
import com.university.post.dto.GatheringPointDTO;
import com.university.post.dto.TransactionPointDTO;
import com.university.post.enums.Permission;
import com.university.post.enums.Type;
import com.university.post.model.Customer;
import com.university.post.model.DeliveryPoint;
import com.university.post.model.Role;
import com.university.post.model.User;
import com.university.post.payload.request.user.PasswordRequest;
import com.university.post.payload.response.statistic.OrderStatisticPointResponse;
import com.university.post.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

public class Utils {
    public static final Integer OVER_AGE = 18;
    public static Boolean isOver18YearsOld(LocalDate dateOfBirth) {
        LocalDate currentDate = LocalDate.now(); // Ngày hiện tại

        Period age = Period.between(dateOfBirth, currentDate);

        Integer years = age.getYears();
        return years >= OVER_AGE;
    }

    public static Boolean isRoleValid(Long role) {
        User user = getUserAuthentication();
        Long roleUser = user.getRole().getId();
        if (roleUser.equals(Permission.TRANSACTION_ADMIN)) {
            return role.equals(Permission.TRANSACTION_STAFF);
        } else if (roleUser.equals(Permission.GATHERING_ADMIN)) {
            return role.equals(Permission.GATHERING_STAFF);
        }
        return true;
    }
    
    public static Long getLocationDistance(String source, String destination) {
        String[] addressSources = source.split("-");
        if (addressSources.length == 4) {
            source = String.join("-", Arrays.copyOfRange(addressSources, 1, addressSources.length));
        }

        String[] addressDestinations = destination.split("-");
        if (addressDestinations.length == 4) {
            destination = String.join("-", Arrays.copyOfRange(addressDestinations, 1, addressDestinations.length));
        }
        String url = "https://api.distancematrix.ai/maps/api/distancematrix/json?origins="
                    + source + "&destinations=" + destination
                    + "&key=xBvnKK6QqtI9C6OvyHkkDB1KEVjxn11exIE3T67k1ZioPZcNM3kQ5oQIXbADI1X8&language=vi";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = objectMapper.readTree(response.getBody());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Long distance = root.get("rows").get(0).get("elements").get(0).get("distance").get("value").longValue();

        return Math.floorDiv(distance, 1000);
    }
    public static User getUserAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return customUserDetails.getUser();
    }

    public static Object typeOrderDelivery(Object location) {
        if (location == null) {
            return null;
        }
        if (location instanceof Customer) {
            return new CustomerDTO((Customer) location);
        } else  {
            DeliveryPoint deliveryPoint = (DeliveryPoint) location;
            return new DeliveryPointDTO(deliveryPoint);
        }
    } 

    public static List<OrderStatisticPointResponse> getOrderStasticByDates(Map<String, Integer> mapDate) {
        List<OrderStatisticPointResponse> orderStastics = new ArrayList();
        LocalDate date = LocalDate.now();

        for (int i = 1; i < 8; i++) {
            orderStastics.add(new OrderStatisticPointResponse(date));
            mapDate.put(date.toString(), i - 1);
            date = date.minusDays(1);
        }
        return orderStastics;
    }
}
