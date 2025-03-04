package com.dsp.bios.service.impl;

import com.dsp.bios.entity.Bill;
import com.dsp.bios.repository.LSBillsViewDetailRepository;
import com.dsp.bios.service.DashboardService;
import com.dsp.bios.util.UserUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
@Service
@AllArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final LSBillsViewDetailRepository billsView;
    private final UserUtils userUtils;
    public List<Bill> getPublished(){
        Long houseId=userUtils.getHouseId();
        return billsView.findByIsDraft(false,houseId).stream().sorted(Comparator.comparing(Bill::getId).reversed()).toList();
    }

    public List<Bill> getDrafts(){
        Long houseId=userUtils.getHouseId();
        return billsView.findByIsDraft(true,houseId);
    }
}
