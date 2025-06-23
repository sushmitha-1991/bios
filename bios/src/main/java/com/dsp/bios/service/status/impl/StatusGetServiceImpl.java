package com.dsp.bios.service.status.impl;

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

    @Override
    public List<Map<String, Object>> getMainStatus(Long billId,String locale) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("billId",billId);
        parameters.addValue("locale",locale);
        return namedParameterJdbcTemplate.queryForList(mainStatusSQL, parameters);
    }
}
