package com.dsp.bios.service;

import com.dsp.bios.service.dao.BillDAO;

import java.util.List;

public interface DashBoardDAOService {
    List<BillDAO> getPublished(String locale);
}
