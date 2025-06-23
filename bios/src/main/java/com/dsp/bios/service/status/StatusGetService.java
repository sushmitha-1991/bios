package com.dsp.bios.service.status;

import java.util.List;
import java.util.Map;

public interface StatusGetService {
    List<Map<String, Object>> getMainStatus(Long billId,String locale);
    List<Map<String, Object>> getPendingStatus(Long billId,String locale);
}
