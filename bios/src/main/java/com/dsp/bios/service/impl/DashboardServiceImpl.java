package com.dsp.bios.service.impl;

import com.dsp.bios.entity.Bill;
import com.dsp.bios.repository.LSBillsViewDetailRepository;
import com.dsp.bios.service.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final LSBillsViewDetailRepository billsView;
    public List<Bill> getPushblished(){
        return billsView.findByIsDraft(false);
    }

    public List<Bill> getDrafts(){
        return billsView.findByIsDraft(true);
    }
}
