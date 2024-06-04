package com.cineverse.erpc.sales.service;

public interface SalesService {

    Long calculateIndividualSales(long employeeId);

    Long calculateTeamSales(int teamCodeId);

    Long calculateTotalSales();

    Long calculateTeamMonthlySales(int teamCodeId, int month);
}
