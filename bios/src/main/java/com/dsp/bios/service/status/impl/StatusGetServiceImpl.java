package com.dsp.bios.service.status.impl;

import com.dsp.bios.dto.status.BillPassedStatusResponseDTO;
import com.dsp.bios.dto.status.BillStatusDTO;
import com.dsp.bios.dto.status.BillStatusResponseDTO;
import com.dsp.bios.service.status.StatusGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class StatusGetServiceImpl implements StatusGetService  {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Value("${main.status}")
    private String mainStatusSQL;

    @Value("${pending.status}")
    private String pendingStatusSQL;

    @Value("${passed.status}")
    private String passedStatusSQL;

    @Override
    public List<BillStatusResponseDTO> getMainStatus(Long billId,String locale) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("billId",billId);
        parameters.addValue("locale",locale);
        return namedParameterJdbcTemplate.query(mainStatusSQL, parameters,new BeanPropertyRowMapper<>(BillStatusResponseDTO.class));
    }

    @Override
    public List<BillStatusResponseDTO> getPendingStatus(Long billId,String locale) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("billId",billId);
        parameters.addValue("locale",locale);
        return namedParameterJdbcTemplate.query(pendingStatusSQL, parameters,new BeanPropertyRowMapper<>(BillStatusResponseDTO.class));
    }

    @Override
    public List<BillPassedStatusResponseDTO> getPassedStatus(Long billId,String locale) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("billId",billId);
        parameters.addValue("locale",locale);
        return namedParameterJdbcTemplate.query(passedStatusSQL, parameters,new BeanPropertyRowMapper<>(BillPassedStatusResponseDTO.class));
    }
}
