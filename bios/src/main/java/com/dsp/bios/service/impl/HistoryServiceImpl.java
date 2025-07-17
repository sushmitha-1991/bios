package com.dsp.bios.service.impl;

import com.dsp.bios.dto.BillHistoryResponseDTO;
import com.dsp.bios.entity.BillHistory;
import com.dsp.bios.repository.BillHistoryRepository;
import com.dsp.bios.service.HistoryService;
import com.dsp.bios.service.dao.BillDAO;
import com.dsp.bios.util.UserUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final BillHistoryRepository billHistoryRepository;
    private final UserUtils userUtils;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Value("${bill.history}")
    private String history;

    @Override
    public void create(Long billId, String formType, String actionType) {
        BillHistory billHistory= new BillHistory();
        billHistory.setBillId(billId);
        billHistory.setFormType(formType);
        billHistory.setActionType(actionType);
        billHistory.setUserId(userUtils.getUserId());
        billHistory.setActionTime(LocalDateTime.now());
        billHistoryRepository.saveAndFlush(billHistory);
    }

    @Override
    public List<BillHistoryResponseDTO> getHistory(Long billId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("billId",billId);
        return namedParameterJdbcTemplate.query(history, parameters,new BeanPropertyRowMapper<>(BillHistoryResponseDTO.class));

    }
}