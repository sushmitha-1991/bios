package com.dsp.bios.service.impl;

import com.dsp.bios.dto.BillDTO;
import com.dsp.bios.dto.status.BillStatusResponseDTO;
import com.dsp.bios.service.DashBoardDAOService;
import com.dsp.bios.service.dao.BillDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashBoardDAOServiceImpl implements DashBoardDAOService {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Value("${bills.dashboard}")
    private String dashboard;

    @Override
    public List<BillDAO> getPublished(String locale) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("locale",locale);
        return namedParameterJdbcTemplate.query(dashboard, parameters,new BeanPropertyRowMapper<>(BillDAO.class));
    }
}
