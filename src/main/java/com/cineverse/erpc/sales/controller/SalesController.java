package com.cineverse.erpc.sales.controller;

import com.cineverse.erpc.sales.service.SalesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/sales")
public class SalesController {

    private final SalesService salesService;

    @Autowired
    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "직원별 연도별 매출 조회", description = "특정 직원의 연도별 매출을 월별로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "직원을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<Map<Integer, Map<String, Long>>> getEmployeeSales(
            @Parameter(description = "직원 ID", required = true) @PathVariable long employeeId) {
        Map<Integer, Map<String, Long>> sales = salesService.calculateEmployeeYearlySales(employeeId);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/team/{teamCodeId}")
    @Operation(summary = "팀별 연도별 매출 조회", description = "특정 팀의 연도별 매출을 월별로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<Map<Integer, Map<String, Long>>> getTeamYearlySales(
            @Parameter(description = "팀 코드 ID", required = true) @PathVariable int teamCodeId) {
        Map<Integer, Map<String, Long>> sales = salesService.calculateTeamYearlySales(teamCodeId);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("")
    @Operation(summary = "총 연도별 매출 조회", description = "전체 연도별 매출을 월별로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<Map<Integer, Map<String, Long>>> getTotalSales() {
        Map<Integer, Map<String, Long>> sales = salesService.calculateTotalYearlySales();
        return ResponseEntity.ok(sales);
    }
}
