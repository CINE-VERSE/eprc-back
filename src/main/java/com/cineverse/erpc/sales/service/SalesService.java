package com.cineverse.erpc.sales.service;

import java.util.Map;

public interface SalesService {

    Long calculateIndividualSales(long employeeId);

    Long calculateTeamSales(int teamCodeId);

    Long calculateTotalSales();

//    Long calculateTeamMonthlySales(int teamCodeId, int year, int month);

    Map<Integer, Map<String, Long>> calculateTeamYearlySales(int teamCodeId);

    Map<Integer, Map<String, Long>> calculateEmployeeYearlySales(long employeeId);

    Map<Integer, Map<String, Long>> calculateTotalYearlySales();
}
