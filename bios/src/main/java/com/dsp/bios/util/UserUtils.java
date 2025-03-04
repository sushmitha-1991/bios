package com.dsp.bios.util;


import com.dsp.bios.dto.UserDetailsDTO;
import com.dsp.bios.exception.BadRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class UserUtils {
    public Long getUserId() {
        Long userId;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsDTO userDetailsDTO) {
            userId =userDetailsDTO.getId();
        } else {
            throw new BadRequest("Bad Authentication");
        }
        return userId;
    }

    public Long getHouseId() {
        Long houseId;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsDTO userDetailsDTO) {
            houseId =userDetailsDTO.getHouseId();
        } else {
            throw new BadRequest("Bad Authentication");
        }
        return houseId;
    }
}
