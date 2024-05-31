package com.cineverse.erpc.admin.delete.controller;

import com.cineverse.erpc.admin.delete.dto.account.RequestAccountDeleteRequestProcess;
import com.cineverse.erpc.admin.delete.dto.account.ResponseAccountDeleteRequestList;
import com.cineverse.erpc.admin.delete.dto.account.ResponseAccountDeleteRequestProcess;
import com.cineverse.erpc.admin.delete.dto.account.ResponseFindAccoundDeleteRequest;
import com.cineverse.erpc.admin.delete.dto.order.RequestOrderDeleteRequestProcess;
import com.cineverse.erpc.admin.delete.dto.order.ResponseFindOrderDeleteRequest;
import com.cineverse.erpc.admin.delete.dto.order.ResponseOrderDeleteRequestList;
import com.cineverse.erpc.admin.delete.dto.order.ResponseOrderDeleteRequestProcess;
import com.cineverse.erpc.admin.delete.dto.quotation.RequestQuotationDeleteRequestProcess;
import com.cineverse.erpc.admin.delete.dto.quotation.ResponseFindQuotationDeleteRequest;
import com.cineverse.erpc.admin.delete.dto.quotation.ResponseQuotationDeleteRequestList;
import com.cineverse.erpc.admin.delete.dto.quotation.ResponseQuotationDeleteRequestProcess;
import com.cineverse.erpc.admin.delete.service.DeleteService;
import com.cineverse.erpc.contract.aggregate.ContractDeleteRequest;
import com.cineverse.erpc.contract.dto.ContractDeleteRequestDTO;
import com.cineverse.erpc.order.order.dto.ResponseDeleteOrder;
import com.cineverse.erpc.salesopp.opportunity.aggregate.SalesOppDeleteRequest;
import com.cineverse.erpc.salesopp.opportunity.dto.SalesOppDeleteRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delete")
public class DeleteController {

    private final DeleteService deleteService;

    @Autowired
    public DeleteController(DeleteService deleteService) {
        this.deleteService = deleteService;
    }

    /* 영업기회 삭제 요청 전체조회 */
    @GetMapping("/sales_opp")
    @Operation(summary = "영업기회 삭제요청 전체조회", description = "영업기회 삭제요청을 전부 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "500", description = "통신 오류")
    public List<SalesOppDeleteRequest> findSalesOppDeleteRequest() {
        List<SalesOppDeleteRequest> salesOppDeleteRequestList = deleteService.findSalesOppDeleteRequestList();

        return salesOppDeleteRequestList;
    }

    /* 영업기회 삭제 요청 단일조회 */
    @GetMapping("/sales_opp/{salesOppDeleteRequestId}")
    @Operation(summary = "영업기회 삭제요청 단일조회", description = "영업기회 삭제요청을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "500", description = "통신 오류")
    public SalesOppDeleteRequestDTO findOppDeleteRequest(
            @Parameter(required = true, description = "영업기회 삭제요청 고유번호")
            @PathVariable long salesOppDeleteRequestId) {
        SalesOppDeleteRequestDTO oppDeleteRequest = deleteService.findSalesOppDeleteRequestById(salesOppDeleteRequestId);

        return oppDeleteRequest;
    }

    /* 영업기회 삭제 요청처리 */
    @PatchMapping("/sales_opp/process")
    public ResponseEntity<SalesOppDeleteRequest> deleteSalesOpp(@RequestBody SalesOppDeleteRequest deleteOppRequest) {
        SalesOppDeleteRequest updatedOppRequest = deleteService.oppDeleteRequestProcess(deleteOppRequest);

        return ResponseEntity.ok(updatedOppRequest);
    }

    /* 계약서 삭제 요청 전체조회 */
    @GetMapping("/contract")
    public List<ContractDeleteRequest> findContractDeleteRequest() {
        List<ContractDeleteRequest> contractDeleteRequestList = deleteService.findContractDeleteRequestList();

        return contractDeleteRequestList;
    }

    /* 계약서 삭제 요청 단일조회 */
    @GetMapping("/contract/{contractDeleteRequestId}")
    public ContractDeleteRequestDTO findContractDeleteRequest(@PathVariable long contractDeleteRequestId) {
        ContractDeleteRequestDTO contractDeleteRequest =
                deleteService.findContractDeleteRequestById(contractDeleteRequestId);

        return contractDeleteRequest;
    }

    /* 계약서 삭제 요청 처리 */
    @PatchMapping("/contract/process")
    public ResponseEntity<ContractDeleteRequest> deleteContract(@RequestBody ContractDeleteRequest deleteContract) {
        ContractDeleteRequest updatedContractRequest = deleteService.contractDeleteRequestProcess(deleteContract);

        return ResponseEntity.ok(updatedContractRequest);
    }

    @GetMapping("/quotation")
    @Operation(summary = "견적서 삭제요청 전체조회", description = "견적서 삭제요청을 전부 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "500", description = "통신 오류")
    public List<ResponseQuotationDeleteRequestList> findQuotationDeleteRequestList() {
        return deleteService.findQuotationDeleteRequestList();
    }

    @GetMapping("/quotation/{quotationDeleteRequestId}")
    @Operation(summary = "견적서 삭제요청 단일조회", description = "견적서 삭제요청을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "500", description = "통신 오류")
    public ResponseEntity<ResponseFindQuotationDeleteRequest> findQuotationDeleteRequestById(
            @Parameter(required = true, description = "견적서 삭제요청 고유번호")
            @PathVariable long quotationDeleteRequestId) {
        ResponseFindQuotationDeleteRequest responseQuotationDeleteRequest =
                deleteService.findQuotationDeleteRequestById(quotationDeleteRequestId);

        return ResponseEntity.status(HttpStatus.OK).body(responseQuotationDeleteRequest);
    }

    @PatchMapping("/quotation/permission")
    @Operation(summary = "영업기회 삭제요청 처리", description = "영업기회 삭제요청을 처리합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "403", description = "입력 값 불일치")
    @ApiResponse(responseCode = "500", description = "통신 오류")
    public ResponseEntity<ResponseQuotationDeleteRequestProcess> processQuotationDeleteRequest(
            @Parameter(required = true, description = "견적서 삭제처리 요청")
            @RequestBody RequestQuotationDeleteRequestProcess requestQuotationDeleteRequestProcess) {
        ResponseQuotationDeleteRequestProcess responseQuotationDeleteRequestProcess
                = deleteService.processQuotationDeleteRequest(requestQuotationDeleteRequestProcess);

        return ResponseEntity.status(HttpStatus.OK).body(responseQuotationDeleteRequestProcess);
    }

    @GetMapping("/account")
    @Operation(summary = "거래처 삭제요청 전체조회", description = "거래처 삭제요청을 전부 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "500", description = "통신 오류")
    public List<ResponseAccountDeleteRequestList> findAccountDeleteRequestList() {
        return deleteService.findAccountDeleteList();
    }

    @GetMapping("/account/{accountDeleteRequestId}")
    @Operation(summary = "거래처 삭제요청 단일조회", description = "거래처 삭제요청을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "500", description = "통신 오류")
    public ResponseFindAccoundDeleteRequest findAccoundDeleteRequestById(
            @Parameter(required = true, description = "거래처 삭제요청 고유번호")
            @PathVariable long accountDeleteRequestId) {
        return deleteService.findAccountDeleteRequestById(accountDeleteRequestId);
    }

    @PatchMapping("/account/process")
    @Operation(summary = "거래처 삭제요청 처리", description = "거래처 삭제요청을 처리합니다..")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "403", description = "입력값 불일치")
    @ApiResponse(responseCode = "500", description = "통신 오류")
    public ResponseEntity<ResponseAccountDeleteRequestProcess> accountDeleteRequestProcess(
            @Parameter(required = true, description = "거래처 삭제처리 요청")
            @RequestBody RequestAccountDeleteRequestProcess requestAccountDeleteRequestProcess) {
        ResponseAccountDeleteRequestProcess responseAccountDeleteRequestProcess
                = deleteService.accountDeleteRequestProcess(requestAccountDeleteRequestProcess);

        return ResponseEntity.status(HttpStatus.OK).body(responseAccountDeleteRequestProcess);
    }

    @GetMapping("/order")
    @Operation(summary = "수주 삭제요청 전체조회", description = "수주 삭제요청을 전부 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "500", description = "통신 오류")
    public List<ResponseOrderDeleteRequestList> orderDeleteRequestList() {
        return deleteService.findOrderDeleteRequestList();
    }

    @GetMapping("/order/{orderDeleteRequestId}")
    @Operation(summary = "수주 삭제요청 단일조회", description = "수주 삭제요청을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "500", description = "통신 오류")
    private ResponseFindOrderDeleteRequest findOrderDeleteRequestById(
            @Parameter(required = true, description = "거래처 삭제요청 고유번호")
            @PathVariable long orderDeleteRequestId) {
        return deleteService.findOrderDeleteRequestById(orderDeleteRequestId);
    }

    @PatchMapping("/order/process")
    @Operation(summary = "수주 삭제요청 처리", description = "수주 삭제요청을 처리합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "500", description = "통신 오류")
    private ResponseEntity<ResponseOrderDeleteRequestProcess> processOrderDeleteRequest(
            @Parameter(required = true, description = "수주 삭제처리 요청")
            @RequestBody RequestOrderDeleteRequestProcess requestOrderDeleteRequestProcess) {
        ResponseOrderDeleteRequestProcess orderDeleteRequestProcess =
                deleteService.processOrderDeleteRequest(requestOrderDeleteRequestProcess);

        return ResponseEntity.status(HttpStatus.OK).body(orderDeleteRequestProcess);
    }
}
