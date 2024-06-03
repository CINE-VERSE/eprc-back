package com.cineverse.erpc.quotation.quotation.controller;

import com.cineverse.erpc.quotation.quotation.aggregate.Quotation;
import com.cineverse.erpc.quotation.quotation.dto.*;
import com.cineverse.erpc.quotation.quotation.service.QuotationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/quotation")
public class QuotationController {

    private final ModelMapper mapper;
    private final QuotationService quotationService;

    @Autowired
    public QuotationController(ModelMapper mapper, QuotationService quotationService) {
        this.mapper = mapper;
        this.quotationService = quotationService;
    }

    @PostMapping(path = "/regist", consumes = {"multipart/form-data;charset=UTF-8"})
    @Operation(summary = "견적서 등록", description = "새로운 견적서를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<ResponseRegistQuotationDTO> registQuotation(
            @Parameter(description = "견적서 JSON 데이터", required = true) @RequestPart("quotation") String quotationJson,
            @Parameter(description = "첨부 파일들") @RequestPart(value = "files", required = false) MultipartFile[] files)
            throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        RequestRegistQuotationDTO newQuotation = objectMapper.readValue(quotationJson, RequestRegistQuotationDTO.class);
        quotationService.registQuotation(newQuotation, files);

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ResponseRegistQuotationDTO responseRegistQuotation = mapper.map(newQuotation, ResponseRegistQuotationDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseRegistQuotation);
    }

    @GetMapping("/{quotationId}")
    @Operation(summary = "견적서 단일 조회", description = "특정 견적서를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "견적서를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public Quotation findQuotationByQuotationId(
            @Parameter(description = "견적서 ID", required = true) @PathVariable long quotationId) {
        return quotationService.findQuotationById(quotationId);
    }

    @GetMapping()
    @Operation(summary = "견적서 전체 조회", description = "모든 견적서를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public List<QuotationDTO> findAllQuotations() {
        return quotationService.findAllQuotations();
    }

    @PatchMapping(path = "/modify", consumes = {"multipart/form-data;charset=UTF-8"})
    @Operation(summary = "견적서 수정", description = "기존 견적서를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<ResponseModifyQuotationDTO> modifyQuotation(
            @Parameter(description = "수정할 견적서 JSON 데이터", required = true)
            @RequestPart("quotation") String quotationJson,
            @Parameter(description = "첨부 파일들") @RequestPart(value = "files", required = false) MultipartFile[] files)
            throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        RequestModifyQuotationDTO requestModifyQuotationDTO
                = objectMapper.readValue(quotationJson, RequestModifyQuotationDTO.class);
        ResponseModifyQuotationDTO responseModifyQuotationDTO
                = quotationService.modifyQuotation(requestModifyQuotationDTO, files);

        return ResponseEntity.ok().body(responseModifyQuotationDTO);
    }

    @PostMapping("/delete")
    @Operation(summary = "견적서 삭제 요청", description = "견적서 삭제 요청을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "삭제 요청 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<ResponseDeleteQuotation> deleteQuotation(
            @Parameter(description = "삭제 요청 데이터", required = true)
            @RequestBody RequestDeleteQuotation requestDeleteQuotation) {
        ResponseDeleteQuotation responseDeleteQuotation = quotationService.deleteQuotation(requestDeleteQuotation);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDeleteQuotation);
    }

    @GetMapping("/code")
    @Operation(summary = "견적서 코드 조회", description = "견적서 코드를 통해 견적서를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "견적서를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseQuotationByCode findQuotationByCode(
            @Parameter(description = "견적서 코드", required = true) @RequestParam String quotationCode) {
        return quotationService.findQuotationByCode(quotationCode);
    }
}
