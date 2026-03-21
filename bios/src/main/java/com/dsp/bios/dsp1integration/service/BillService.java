package com.dsp.bios.dsp1integration.service;

import com.dsp.bios.dsp1integration.dto.BillResponseDTO;
import com.dsp.bios.dsp1integration.dto.BillResponseDTO.BillRecord;
import com.dsp.bios.dsp1integration.dto.BillResponseDTO.Metadata;
import com.dsp.bios.repository.BillStatusRepository;
import com.dsp.bios.util.ConstantsUtil;
import com.dsp.bios.dsp1integration.dto.BillStatusCountDto;
import com.dsp.bios.dsp1integration.repo.BillsRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import org.springframework.util.StringUtils;

@Service
public class BillService {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private BillsRepo billsRepo;
    
    @Autowired
    private BillStatusRepository billStatusRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public BillResponseDTO getBills(
            int page, int size,
            String billNumber,
            String billName,
            String billType,
            String billCategory,
            String ministryName,
            Long billYear,
            String billIntroducedInHouse,
            String billIntroducedBy,
            LocalDate billIntroducedDate,
            String status
    ) {
        int offset = (page - 1) * size;
        StringBuilder sql = new StringBuilder("""
            SELECT 
                b.bill_no,
                b.bill_category,
                b.introduced_by,
                b.status,
                b.ministry,
                b.bill_year,
                h.house_name AS houseName,
                b.date_of_introduction,
                ubt.upload_bill_text
            FROM bills.bill b
            LEFT JOIN bills.upload_bill_text ubt ON b.id = ubt.bill_id
            LEFT JOIN master.houses h ON b.house_abbr = h.abbr
            WHERE 1 = 1
        """);

        Map<String, Object> params = new HashMap<>();

        if (billNumber != null && !billNumber.isBlank()) {
            sql.append(" AND b.bill_no ILIKE :billNumber ");
            params.put("billNumber", "%" + billNumber + "%");
        }
        if (billName != null && !billName.isBlank()) {
            sql.append(" AND b.bill_category ILIKE :billName ");
            params.put("billName", "%" + billName + "%");
        }
        if (billType != null && !billType.isBlank()) {
            sql.append(" AND b.introduced_by ILIKE :billType ");
            params.put("billType", "%" + billType + "%");
        }
        if (billCategory != null && !billCategory.isBlank()) {
            sql.append(" AND b.bill_category ILIKE :billCategory ");
            params.put("billCategory", "%" + billCategory + "%");
        }
        if (ministryName != null && !ministryName.isBlank()) {
            sql.append(" AND b.ministry ILIKE :ministryName ");
            params.put("ministryName", "%" + ministryName + "%");
        }
        if (billYear != null) {
            sql.append(" AND b.bill_year = :billYear ");
            params.put("billYear", billYear);
        }
        if (billIntroducedInHouse != null && !billIntroducedInHouse.isBlank()) {
            sql.append(" AND h.house_name ILIKE :billIntroducedInHouse ");
            params.put("billIntroducedInHouse", "%" + billIntroducedInHouse + "%");
        }
        if (billIntroducedBy != null && !billIntroducedBy.isBlank()) {
            sql.append(" AND b.introduced_by ILIKE :billIntroducedBy ");
            params.put("billIntroducedBy", "%" + billIntroducedBy + "%");
        }
        if (billIntroducedDate != null) {
            sql.append(" AND b.date_of_introduction = :billIntroducedDate ");
            params.put("billIntroducedDate", billIntroducedDate);
        }
        if (status != null && !status.isBlank()) {
            sql.append(" AND b.status ILIKE :status ");
            params.put("status", "%" + status + "%");
        }

        sql.append(" ORDER BY b.date_of_introduction DESC ");
        sql.append(" OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY ");
        params.put("offset", offset);
        params.put("limit", size);

        List<BillRecord> records = jdbcTemplate.query(sql.toString(), params, this::mapToRecord);

        // Count query (same filters, but without ORDER or pagination)
        StringBuilder countSql = new StringBuilder("""
            SELECT COUNT(*)
            FROM bills.bill b
            LEFT JOIN master.houses h ON b.house_abbr = h.abbr
            WHERE 1 = 1
        """);

        if (billNumber != null && !billNumber.isBlank()) {
            countSql.append(" AND b.bill_no ILIKE :billNumber ");
        }
        if (billName != null && !billName.isBlank()) {
            countSql.append(" AND b.bill_category ILIKE :billName ");
        }
        if (billType != null && !billType.isBlank()) {
            countSql.append(" AND b.introduced_by ILIKE :billType ");
        }
        if (billCategory != null && !billCategory.isBlank()) {
            countSql.append(" AND b.bill_category ILIKE :billCategory ");
        }
        if (ministryName != null && !ministryName.isBlank()) {
            countSql.append(" AND b.ministry ILIKE :ministryName ");
        }
        if (billYear != null) {
            countSql.append(" AND b.bill_year = :billYear ");
        }
        if (billIntroducedInHouse != null && !billIntroducedInHouse.isBlank()) {
            countSql.append(" AND h.house_name ILIKE :billIntroducedInHouse ");
        }
        if (billIntroducedBy != null && !billIntroducedBy.isBlank()) {
            countSql.append(" AND b.introduced_by ILIKE :billIntroducedBy ");
        }
        if (billIntroducedDate != null) {
            countSql.append(" AND b.date_of_introduction = :billIntroducedDate ");
        }
        if (status != null && !status.isBlank()) {
            countSql.append(" AND b.status ILIKE :status ");
        }

        long total = jdbcTemplate.queryForObject(countSql.toString(), params, Long.class);
        int totalPages = (int) Math.ceil((double) total / size);

        Metadata metadata = new Metadata(page, size, total, totalPages);
        return new BillResponseDTO(metadata, records);
    }

    private BillRecord mapToRecord(ResultSet rs, int rowNum) throws SQLException {
        String uploadJson = rs.getString("upload_bill_text");
        String fileUrl = extractFileUrl(uploadJson);

        return BillRecord.builder()
                .billNumber(rs.getString("bill_no"))
                .billName(rs.getString("bill_category"))
                .billType(rs.getString("introduced_by"))
                .billCategory(rs.getString("bill_category"))
                .ministryName(rs.getString("ministry"))
                .billYear(rs.getObject("bill_year") != null ? rs.getLong("bill_year") : null)
                .billIntroducedInHouse(rs.getString("houseName")) // updated to house_name
                .billIntroducedBy(rs.getString("introduced_by"))
                .billIntroducedDate(rs.getString("date_of_introduction"))
                .billIntroducedFile(fileUrl)
                .status(rs.getString("status"))
                .build();
    }

    private String extractFileUrl(String json) {
        if (json == null || json.isBlank()) return null;
        try {
            JsonNode node = objectMapper.readTree(json);
            JsonNode files = node.get("files");
            if (files != null && files.isArray() && files.size() > 0) {
                String fileName = files.get(0).get("fileName").asText();
                return "https://sansad.in/getFile/BillsTexts/LSBillTexts/Asintroduced/" + fileName + "?source=legislation";
            }
        } catch (Exception e) {
            // Optional: log error
        }
        return null;
    }

    public List<BillStatusCountDto> getBillStatusCount(String house, Long hsNo) {
        return billStatusRepository.getBillsStatusCount(house);
    }

    public List<String> getBillStatus(String locale) {
        return billStatusRepository.getStatusList();
    }

    public BillResponseDTO getBillsData(int page, int size, String billNumber, String billName, String billType,
            String billCategory, String ministryName, Integer billYear, String billStatus,
            LocalDate billIntroducedDateFrom, LocalDate billIntroducedDateTo, LocalDate billPassedInLSDateFrom,
            LocalDate billPassedInLSDateTo, LocalDate billPassedInRSDateFrom, LocalDate billPassedInRSDateTo,
            LocalDate startDate, LocalDate endDate, String locale, String house, String loksabha, Integer session,
            String memberName, String mpCode, Long billTypeId, String sortOn, String sortBy) {

                    int offset = (page - 1) * size;
        StringBuilder sql = new StringBuilder("""
            SELECT 
                b.bill_no,
                b.bill_category,
                b.introduced_by,
                statusCode.code as status, 
                b.ministry,
                b.bill_year,
                h.house_name AS houseName,
                b.date_of_introduction,
                ubt.upload_bill_text
            FROM bills.bill b
             left join bills.bill_status bs on bs.bill_id = b.id 
            left join master.codes statusCode on bs.bill_status_id = statusCode.id 
            LEFT JOIN bills.upload_bill_text ubt ON b.id = ubt.bill_id
            LEFT JOIN master.houses h ON b.house_abbr = h.abbr
            WHERE 1 = 1
        """);

        Map<String, Object> params = new HashMap<>();

        if (billNumber != null && !billNumber.isBlank()) {
            sql.append(" AND b.bill_no ILIKE :billNumber ");
            params.put("billNumber", "%" + billNumber + "%");
        }
        if (billName != null && !billName.isBlank()) {
            sql.append(" AND b.bill_category ILIKE :billName ");
            params.put("billName", "%" + billName + "%");
        }
        if (billType != null && !billType.isBlank()) {
            sql.append(" AND b.introduced_by ILIKE :billType ");
            params.put("billType", "%" + billType + "%");
        }
        if (billCategory != null && !billCategory.isBlank()) {
            sql.append(" AND b.bill_category ILIKE :billCategory ");
            params.put("billCategory", "%" + billCategory + "%");
        }
        if (ministryName != null && !ministryName.isBlank()) {
            sql.append(" AND b.ministry ILIKE :ministryName ");
            params.put("ministryName", "%" + ministryName + "%");
        }
        if (billYear != null) {
            sql.append(" AND b.bill_year = :billYear ");
            params.put("billYear", billYear);
        }
        
        if (billStatus != null && !billStatus.isBlank()) {
            sql.append(" AND statusCode.code ILIKE :billStatus ");
            params.put("billStatus", "%" + billStatus.trim() + "%");
        }
        if (billIntroducedDateFrom != null && billIntroducedDateTo != null) {
            sql.append(" AND b.date_of_introduction BETWEEN :introFrom AND :introTo ");
            params.put("introFrom", billIntroducedDateFrom);
            params.put("introTo", billIntroducedDateTo);
        } else if (billIntroducedDateFrom != null) {
            sql.append(" AND b.date_of_introduction >= :introFrom ");
            params.put("introFrom", billIntroducedDateFrom);
        } else if (billIntroducedDateTo != null) {
            sql.append(" AND b.date_of_introduction <= :introTo ");
            params.put("introTo", billIntroducedDateTo);
        }
        if (billPassedInLSDateFrom != null && billPassedInLSDateTo != null) {
            sql.append(" AND b.passed_in_ls_date BETWEEN :lsFrom AND :lsTo ");
            params.put("lsFrom", billPassedInLSDateFrom);
            params.put("lsTo", billPassedInLSDateTo);
        } else if (billPassedInLSDateFrom != null) {
            sql.append(" AND b.passed_in_ls_date >= :lsFrom ");
            params.put("lsFrom", billPassedInLSDateFrom);
        } else if (billPassedInLSDateTo != null) {
            sql.append(" AND b.passed_in_ls_date <= :lsTo ");
            params.put("lsTo", billPassedInLSDateTo);
        }
        if (billPassedInRSDateFrom != null && billPassedInRSDateTo != null) {
            sql.append(" AND b.passed_in_rs_date BETWEEN :rsFrom AND :rsTo ");
            params.put("rsFrom", billPassedInRSDateFrom);
            params.put("rsTo", billPassedInRSDateTo);
        } else if (billPassedInRSDateFrom != null) {
            sql.append(" AND b.passed_in_rs_date >= :rsFrom ");
            params.put("rsFrom", billPassedInRSDateFrom);
        } else if (billPassedInRSDateTo != null) {
            sql.append(" AND b.passed_in_rs_date <= :rsTo ");
            params.put("rsTo", billPassedInRSDateTo);
        }
        String dir = ("asc".equalsIgnoreCase(sortBy) ? "ASC" : "DESC");
        sql.append(" ORDER BY b.date_of_introduction " + dir + " ");
        sql.append(" LIMIT :limit OFFSET :offset ");
        params.put("limit", size);
        params.put("offset", offset);
        System.out.println(sql.toString());
        List<BillRecord> records = jdbcTemplate.query(sql.toString(), params, this::mapToRecord);
        StringBuilder countSql = new StringBuilder("""
            SELECT COUNT(*)
            FROM bills.bill b
            left join bills.bill_status bs on bs.bill_id = b.id 
            left join master.codes statusCode on bs.bill_status_id = statusCode.id 
            LEFT JOIN bills.upload_bill_text ubt ON b.id = ubt.bill_id
            LEFT JOIN master.houses h ON b.house_abbr = h.abbr
            WHERE 1 = 1
        """);
        if (billNumber != null && !billNumber.isBlank()) {
            countSql.append(" AND b.bill_no ILIKE :billNumber ");
        }
        if (billName != null && !billName.isBlank()) {
            countSql.append(" AND b.bill_category ILIKE :billName ");
        }
        if (billType != null && !billType.isBlank()) {
            countSql.append(" AND b.introduced_by ILIKE :billType ");
        }
        if (billCategory != null && !billCategory.isBlank()) {
            countSql.append(" AND b.bill_category ILIKE :billCategory ");
        }
        if (ministryName != null && !ministryName.isBlank()) {
            countSql.append(" AND b.ministry ILIKE :ministryName ");
        }
        if (billYear != null) {
            countSql.append(" AND b.bill_year = :billYear ");
        }
        if (billStatus != null && !billStatus.isBlank()) {
            countSql.append(" AND statusCode.code ILIKE :billStatus ");
        }
        if (billIntroducedDateFrom != null && billIntroducedDateTo != null) {
            countSql.append(" AND b.date_of_introduction BETWEEN :introFrom AND :introTo ");
        } else if (billIntroducedDateFrom != null) {
            countSql.append(" AND b.date_of_introduction >= :introFrom ");
        } else if (billIntroducedDateTo != null) {
            countSql.append(" AND b.date_of_introduction <= :introTo ");
        }
        if (billPassedInLSDateFrom != null && billPassedInLSDateTo != null) {
            countSql.append(" AND b.passed_in_ls_date BETWEEN :lsFrom AND :lsTo ");
        } else if (billPassedInLSDateFrom != null) {
            countSql.append(" AND b.passed_in_ls_date >= :lsFrom ");
        } else if (billPassedInLSDateTo != null) {
            countSql.append(" AND b.passed_in_ls_date <= :lsTo ");
        }
        if (billPassedInRSDateFrom != null && billPassedInRSDateTo != null) {
            countSql.append(" AND b.passed_in_rs_date BETWEEN :rsFrom AND :rsTo ");
        } else if (billPassedInRSDateFrom != null) {
            countSql.append(" AND b.passed_in_rs_date >= :rsFrom ");
        } else if (billPassedInRSDateTo != null) {
            countSql.append(" AND b.passed_in_rs_date <= :rsTo ");
        }
        long total = jdbcTemplate.queryForObject(countSql.toString(), params, Long.class);
        int totalPages = (int) Math.ceil((double) total / size);
        Metadata metadata = new Metadata(page, size, total, totalPages);
        return new BillResponseDTO(metadata, records);
    }

    public List<String> getCategories(String locale) {
        return billsRepo.getCategoryList();
    }

    public List<String> getBillTypes() {
        return billsRepo.getBillTypes();
    }

    public List<String> getMinistries() {
        return billsRepo.getMinistries();
    }
}
