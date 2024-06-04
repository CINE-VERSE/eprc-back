package com.cineverse.erpc.sales.service;

import com.cineverse.erpc.order.order.aggregate.Order;
import com.cineverse.erpc.order.order.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Long calculateTeamMonthlySales(int teamCodeId, int month) {
        List<Order> orders = orderRepository.findByEmployeeTeamCodeTeamCodeIdAndDepositDateIsNotNull(teamCodeId);
        return orders.stream()
                .filter(order -> {
                    String depositDate = order.getDepositDate(); // Assuming format is "yyyy-MM-dd"
                    int orderMonth = Integer.parseInt(depositDate.substring(5, 7));
                    return orderMonth == month;
                })
                .mapToLong(Order::getOrderTotalPrice)
                .sum();
    }
}
