package com.dsp.bios.service;

import com.dsp.bios.Entity.Bill;

import java.util.List;

public interface DashboardService {
    List<Bill> getPushblished();

    List<Bill> getDrafts();
}