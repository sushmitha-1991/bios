package com.dsp.bios.service;

import com.dsp.bios.entity.Bill;

import java.util.List;

public interface DashboardService {
    List<Bill> getPushblished();

    List<Bill> getDrafts();
}