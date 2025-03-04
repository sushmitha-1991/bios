package com.dsp.bios.service;

import com.dsp.bios.entity.Bill;

import java.util.List;

public interface DashboardService {
    List<Bill> getPublished();

    List<Bill> getDrafts();
}