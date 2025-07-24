package com.dsp.bios.service.impl;

import com.dsp.bios.dto.BillDTO;
import com.dsp.bios.dto.status.BillStatusResponseDTO;
import com.dsp.bios.service.DashBoardDAOService;
import com.dsp.bios.service.dao.BillDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashBoardDAOServiceImpl implements DashBoardDAOService {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Value("${bills.dashboard}")
    private String dashboard;


    @Value("${bills.filter.select}")
    private String select;
    @Value("${bills.filter.where}")
    private String where;

    @Value("${bills.orderBy}")
    private String orderBy;
    @Value("${passedStatusCTE}")
    private String passedStatusCTE;

    @Override
    public List<BillDAO> getPublished(String locale) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("locale",locale);
        return namedParameterJdbcTemplate.query(dashboard, parameters,new BeanPropertyRowMapper<>(BillDAO.class));
    }

    @Override
    public List<BillDAO> filterBills(String locale, String billCategoryAbbr, String ministryAbbr, String statusAbbr, Long lokSabhaId, Long lokSabhaSessionId, Long rajyaSabhaId, String introducedIn, String passedIn, Long pendingStatus, Boolean isOrdinanceReplacingBill, String referredToCommittee,String introducedBy) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        StringBuilder sbSelect = new StringBuilder(select);
        StringBuilder sbWhere = new StringBuilder(where);


        if (StringUtils.hasLength(billCategoryAbbr)) {
            sbWhere.append(" AND b1_0.bill_category_abbr = :billCategory ");
            parameters.addValue("billCategory", billCategoryAbbr);
        }

        // Add ministry condition
        if (StringUtils.hasLength(ministryAbbr)) {
            sbWhere.append(" AND b1_0.ministry_abbr = :ministry ");
            parameters.addValue("ministry", ministryAbbr);
        }

        if (StringUtils.hasLength(statusAbbr)) {
            sbWhere.append(" AND c1.abbr = :status ");
            parameters.addValue("status", statusAbbr);
        }

        if (lokSabhaId != null) {
            sbWhere.append(" AND b1_0.lok_sabha_number_id = :lokSabhaId ");
            parameters.addValue("lokSabhaId", lokSabhaId);
        }

        if (lokSabhaSessionId != null) {
            sbWhere.append(" AND b1_0.lok_sabha_session_number_id = :lokSabhaSessionId ");
            parameters.addValue("lokSabhaSessionId", lokSabhaSessionId);
        }

        if (rajyaSabhaId != null) {
            sbWhere.append(" AND b1_0.raj_sabha_session_number_id = :rajyaSabhaId ");
            parameters.addValue("rajyaSabhaId", rajyaSabhaId);
        }

        if (StringUtils.hasLength(introducedIn)) {
            sbWhere.append(" AND b1_0.house_abbr = :introducedIn ");
            parameters.addValue("introducedIn", introducedIn);
        }

        if (StringUtils.hasLength(passedIn)) {
            MapSqlParameterSource passedInParam = new MapSqlParameterSource();
            passedInParam.addValue("status",passedIn);
            List<Long> billIds=namedParameterJdbcTemplate.queryForList(passedStatusCTE,passedInParam , Long.class);
            if(billIds.isEmpty()){
                return List.of();
            }
            sbWhere.append(" AND b1_0.id in ( :billIds ) ");
            parameters.addValue("billIds", billIds);
        }

        if (pendingStatus != null) {
            sbSelect.append(" inner join bills.pending_status ps on ps.display_sequence=1 and ps.bill_id=b1_0.id and ps.is_active=true  ");
            sbWhere.append(" AND ps.pending_status_id = :pendingStatus ");
            parameters.addValue("pendingStatus", pendingStatus);
        }

        if (isOrdinanceReplacingBill != null) {
            sbSelect.append(" inner join bills.entry_data b on b.bill_id=b1_0.id ");
            sbWhere.append(" AND (b.entry_data::json->>'weatherOrdinanceReplacingBill')::boolean = :isOrdinanceReplacingBill ");
            parameters.addValue("isOrdinanceReplacingBill", isOrdinanceReplacingBill);
        }


        if (StringUtils.hasLength(referredToCommittee )) {
            sbSelect.append("  inner join  bills.refer_to_committee rtc on rtc.bill_id= b1_0.id  ");
            sbWhere.append(" AND b1_0.r.committee_data::json->>'committeeTypeAbbr' = :referredToCommittee ");
            parameters.addValue("referredToCommittee", referredToCommittee);
        }

        if(StringUtils.hasLength(introducedBy)){
            sbWhere.append(" AND b1_0.introduced_by = :introducedBy ");
            parameters.addValue("introducedBy", introducedBy);
        }

        String query=sbSelect.append(" ").append(sbWhere).append(" ").append(orderBy).toString();
        log.debug("filter query {} ",query);
        return namedParameterJdbcTemplate.query(query, parameters,new BeanPropertyRowMapper<>(BillDAO.class));

    }
}
