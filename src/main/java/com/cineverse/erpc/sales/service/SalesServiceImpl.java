package com.cineverse.erpc.sales.service;

import com.cineverse.erpc.order.order.aggregate.Order;
import com.cineverse.erpc.order.order.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SalesServiceImpl implements SalesService {

    private final OrderRepository orderRepository;

    @Autowired
    public SalesServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Long calculateIndividualSales(long employeeId) {
        List<Order> orders = orderRepository.findByEmployeeEmployeeIdAndDepositDateIsNotNull(employeeId);
        return orders.stream().mapToLong(Order::getOrderTotalPrice).sum();
    }

    @Override
    public Long calculateTeamSales(int teamCodeId) {
        List<Order> orders = orderRepository.findByEmployeeTeamCodeTeamCodeIdAndDepositDateIsNotNull(teamCodeId);
        return orders.stream().mapToLong(Order::getOrderTotalPrice).sum();
    }

    @Override
    public Long calculateTotalSales() {
        List<Order> orders = orderRepository.findByDepositDateIsNotNull();
        return orders.stream().mapToLong(Order::getOrderTotalPrice).sum();
    }

//    @Override
//    public Long calculateTeamMonthlySales(int teamCodeId, int year, int month) {
//        List<Order> orders = orderRepository.findByEmployeeTeamCodeTeamCodeIdAndDepositDateIsNotNull(teamCodeId);
//        return orders.stream()
//                .filter(order -> {
//                    String depositDate = order.getDepositDate(); // Assuming format is "yyyy-MM-dd"
//                    int orderYear = Integer.parseInt(depositDate.substring(0, 4));
//                    int orderMonth = Integer.parseInt(depositDate.substring(5, 7));
//                    return orderYear == year && orderMonth == month;
//                })
//                .mapToLong(Order::getOrderTotalPrice)
//                .sum();
//    }

    @Override
    public Map<Integer, Map<String, Long>> calculateTeamYearlySales(int teamCodeId) {
        List<Order> orders = orderRepository.findByEmployeeTeamCodeTeamCodeIdAndDepositDateIsNotNull(teamCodeId);

        return calculateYearlySales(orders);
    }

    @Override
    public Map<Integer, Map<String, Long>> calculateEmployeeYearlySales(long employeeId) {
        List<Order> orders = orderRepository.findByEmployeeEmployeeIdAndDepositDateIsNotNull(employeeId);

        return calculateYearlySales(orders);
    }

    @Override
    public Map<Integer, Map<String, Long>> calculateTotalYearlySales() {
        List<Order> orders = orderRepository.findByDepositDateIsNotNull();

        return calculateYearlySales(orders);
    }

    private Map<Integer, Map<String, Long>> calculateYearlySales(List<Order> orders) {
        Map<Integer, Map<String, Long>> yearlySales = new LinkedHashMap<>();

        orders.forEach(order -> {
            String depositDate = order.getDepositDate(); // Assuming format is "yyyy-MM-dd"
            int orderYear = Integer.parseInt(depositDate.substring(0, 4));
            int orderMonth = Integer.parseInt(depositDate.substring(5, 7));
            long orderTotalPrice = order.getOrderTotalPrice();

            yearlySales
                    .computeIfAbsent(orderYear, k -> {
                        Map<String, Long> monthsMap = new LinkedHashMap<>();
                        for (int i = 1; i <= 12; i++) {
                            monthsMap.put(String.format("%02d", i), 0L);
                        }
                        monthsMap.put("total", 0L);
                        return monthsMap;
                    })
                    .merge(String.format("%02d", orderMonth), orderTotalPrice, Long::sum);

            yearlySales.get(orderYear).merge("total", orderTotalPrice, Long::sum);
        });

        return yearlySales;
    }
}
