package com.dsp.bios.dsp1integration.service;

import com.dsp.bios.dsp1integration.dto.BillResponseDTO;
import com.dsp.bios.dsp1integration.dto.BillResponseDTO.BillRecord;
import com.dsp.bios.dsp1integration.dto.BillResponseDTO.Metadata;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Service
public class BillService {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

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
                b.house_abbr,
                b.date_of_introduction,
                ubt.upload_bill_text
            FROM bills.bill b
            LEFT JOIN bills.upload_bill_text ubt ON b.id = ubt.bill_id
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
            sql.append(" AND b.house_abbr ILIKE :billIntroducedInHouse ");
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

        // ✅ Fix: Proper count query without ORDER BY / OFFSET / FETCH
        StringBuilder countSql = new StringBuilder("SELECT COUNT(*) FROM bills.bill b WHERE 1 = 1 ");
        String conditionPart = sql.toString().split("ORDER BY")[0]; // remove ORDER BY and pagination
        if (conditionPart.contains("AND")) {
            countSql.append(conditionPart.substring(conditionPart.indexOf("AND")));
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
                .billIntroducedInHouse(rs.getString("house_abbr"))
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
            // log if needed
        }
        return null;
    }


}
