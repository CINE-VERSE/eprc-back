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

import java.util.HashMap;
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
    @Operation(summary = "직원별 매출 조회", description = "특정 직원의 매출을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "직원을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<Map<String, Long>> getEmployeeSales(
            @Parameter(description = "직원 ID", required = true) @PathVariable long employeeId) {
        Long sales = salesService.calculateIndividualSales(employeeId);
        Map<String, Long> response = new HashMap<>();
        response.put("employeeSales", sales);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/team/{teamCodeId}")
//    @Operation(summary = "팀별 매출 조회", description = "특정 팀의 매출을 조회합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "조회 성공"),
//            @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없음"),
//            @ApiResponse(responseCode = "500", description = "서버 오류")
//    })
//    public ResponseEntity<Map<String, Long>> getTeamSales(
//            @Parameter(description = "팀 코드 ID", required = true) @PathVariable int teamCodeId) {
//        Long sales = salesService.calculateTeamSales(teamCodeId);
//        Map<String, Long> response = new HashMap<>();
//        response.put("teamSales", sales);
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/team/{teamCodeId}/{month}")
    @Operation(summary = "팀별 월별 매출 조회", description = "특정 팀의 특정 월 매출을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<Map<String, Long>> getTeamMonthlySales(
            @Parameter(description = "월", required = true) @PathVariable int month,
            @Parameter(description = "팀 코드 ID", required = true) @PathVariable int teamCodeId) {
        Long sales = salesService.calculateTeamMonthlySales(teamCodeId, month);
        Map<String, Long> response = new HashMap<>();
        response.put("teamMonthlySales", sales);
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    @Operation(summary = "총 매출 조회", description = "전체 매출을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<Map<String, Long>> getTotalSales() {
        Long sales = salesService.calculateTotalSales();
        Map<String, Long> response = new HashMap<>();
        response.put("totalSales", sales);
        return ResponseEntity.ok(response);
    }
}
