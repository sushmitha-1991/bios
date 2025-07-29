package com.dsp.bios.billsintroducedrs.service;


import com.dsp.bios.billsintroducedrs.dto.BillBySessionDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.dsp.bios.util.MapJsonConverter.objectMapper;

@Service
@Slf4j
public class BillIntro {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<BillBySessionDTO> getBillsBySession(int sessionNo) {
        String sql = """
            SELECT 
                b.id,
                b.bill_no,
                b.bill_category,
                b.introduced_by,
                b.status,
                b.ministry,
                b.bill_year,
                h.house_name,
                b.date_of_introduction,
                ubt.upload_bill_text,
                b.raj_sabha_session_number,
                b.short_title
            FROM bills.bill b
            LEFT JOIN bills.upload_bill_text ubt ON b.id = ubt.bill_id
            LEFT JOIN master.houses h ON b.house_abbr = h.abbr
            WHERE CAST(b.raj_sabha_session_number AS INTEGER) = :sessionno
        """;

        Map<String, Object> params = Map.of("sessionno", sessionNo);

        return jdbcTemplate.query(sql, params, (rs, rowNum) -> mapToBillDTO(rs));
    }

    private BillBySessionDTO mapToBillDTO(ResultSet rs) throws SQLException {
        String uploadJson = rs.getString("upload_bill_text");
        Map<String, String> fileMap = parseUploadBillText(uploadJson);

        String houseName = rs.getString("house_name");
        String houseShort = houseName != null && houseName.equalsIgnoreCase("Rajya Sabha") ? "rs" : "ls";
        int session = rs.getInt("raj_sabha_session_number");

        return BillBySessionDTO.builder()
                .bill_no(rs.getString("bill_no"))
                .bill_year(rs.getObject("bill_year") != null ? rs.getInt("bill_year") : null)
                .title(rs.getString("short_title"))
                .type(rs.getString("introduced_by"))
                .intro(" ")
                .ministry(rs.getString("ministry"))
                .bill_id(rs.getLong("id"))
                .house_number(0)

                .as_introduced_file(fileMap.get("as_introduced_file"))
                .as_passedrajyasabha_file(fileMap.get("as_passedrajyasabha_file"))
                .as_passedloksabha_file("") // no mapping
                .as_passedbothhouses_file(fileMap.get("as_passedbothhouses_file"))
                .errata_lsfile(fileMap.get("errata_lsfile"))

                .house(houseShort)
                .sessionno(session)
                .IntroSession(session)
                .RetSession(null)
                .passing_dt(rs.getString("date_of_introduction"))
                .passing(houseName)
                .H_as_introduced_file(fileMap.get("H_as_introduced_file"))
                .H_as_passedrajyasabha_file(fileMap.get("H_as_passedrajyasabha_file"))
                .H_as_passedloksabha_file("")
                .H_as_passedbothhouses_file("")
                .H_errata_lsfile("")

                .return_second_house(null)
                .introduction(rs.getString("date_of_introduction"))

                .as_introduced_file_dsp(fileMap.get("as_introduced_file_dsp"))
                .as_passedrajyasabha_file_dsp(fileMap.get("as_passedrajyasabha_file_dsp"))
                .as_passedloksabha_file_dsp(null)
                .as_passedbothhouses_file_dsp(fileMap.get("as_passedbothhouses_file_dsp"))
                .errata_lsfile_dsp(fileMap.get("errata_lsfile_dsp"))

                .H_as_introduced_file_dsp(fileMap.get("H_as_introduced_file_dsp"))
                .H_as_passedrajyasabha_file_dsp(fileMap.get("H_as_passedrajyasabha_file_dsp"))
                .H_as_passedloksabha_file_dsp(fileMap.get("H_as_passedloksabha_file_dsp"))
                .H_as_passedbothhouses_file_dsp(fileMap.get("H_as_passedbothhouses_file_dsp"))
                .H_errata_lsfile_dsp(fileMap.get("H_errata_lsfile_dsp"))

                .as_introduced_file_size("")
                .as_passedrajyasabha_file_size("")
                .as_passedloksabha_file_size(null)
                .as_passedbothhouses_file_size("")
                .errata_lsfile_size("")
                .H_as_introduced_file_size("")
                .H_as_passedrajyasabha_file_size("")
                .H_as_passedloksabha_file_size(null)
                .H_as_passedbothhouses_file_size(null)
                .H_errata_lsfile_size(null)
                .build();
    }

    private Map<String, String> parseUploadBillText(String uploadJson) {
        Map<String, String> fileMap = new HashMap<>();
        if (uploadJson == null || uploadJson.isBlank()) return fileMap;

        try {
            JsonNode rootArray = objectMapper.readTree(uploadJson);

            if (rootArray.isArray()) {
                for (JsonNode versionNode : rootArray) {
                    String abbr = versionNode.path("textVersionAbbr").asText();
                    JsonNode files = versionNode.path("files");

                    if (files.isArray() && files.size() > 0) {
                        String fileUUID = files.get(0).path("fileUUID").asText();

                        switch (abbr) {
                            case "As Introduced" -> fileMap.put("as_introduced_file", fileUUID);
                            case "As Passed by Rajya Sabha" -> fileMap.put("as_passedrajyasabha_file", fileUUID);
                            case "As Passed by Lok Sabha" -> fileMap.put("as_passedloksabha_file", fileUUID);
                            case "As Passed by Both Houses" -> fileMap.put("as_passedbothhouses_file", fileUUID);
                            case "Errata to the Bill (LS)" -> fileMap.put("errata_lsfile", fileUUID);

                            case "H_As Introduced" -> fileMap.put("H_as_introduced_file", fileUUID);
                            case "H_As Passed by Rajya Sabha" -> fileMap.put("H_as_passedrajyasabha_file", fileUUID);
                            case "H_As Passed by Lok Sabha" -> fileMap.put("H_as_passedloksabha_file", fileUUID);
                            case "H_As Passed by Both Houses" -> fileMap.put("H_as_passedbothhouses_file", fileUUID);
                            case "H_Errata to the Bill (LS)" -> fileMap.put("H_errata_lsfile", fileUUID);

                            case "As Introduced DSP" -> fileMap.put("as_introduced_file_dsp", fileUUID);
                            case "As Passed by Rajya Sabha DSP" -> fileMap.put("as_passedrajyasabha_file_dsp", fileUUID);
                            case "As Passed by Lok Sabha DSP" -> fileMap.put("as_passedloksabha_file_dsp", fileUUID);
                            case "As Passed by Both Houses DSP" -> fileMap.put("as_passedbothhouses_file_dsp", fileUUID);
                            case "Errata to the Bill DSP" -> fileMap.put("errata_lsfile_dsp", fileUUID);

                            case "H_As Introduced DSP" -> fileMap.put("H_as_introduced_file_dsp", fileUUID);
                            case "H_As Passed by Rajya Sabha DSP" -> fileMap.put("H_as_passedrajyasabha_file_dsp", fileUUID);
                            case "H_As Passed by Lok Sabha DSP" -> fileMap.put("H_as_passedloksabha_file_dsp", fileUUID);
                            case "H_As Passed by Both Houses DSP" -> fileMap.put("H_as_passedbothhouses_file_dsp", fileUUID);
                            case "H_Errata to the Bill DSP" -> fileMap.put("H_errata_lsfile_dsp", fileUUID);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error parsing upload_bill_text JSON array: {}", e.getMessage());
        }

        return fileMap;
    }

}
