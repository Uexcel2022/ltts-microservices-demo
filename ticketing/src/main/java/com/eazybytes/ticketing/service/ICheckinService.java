package com.eazybytes.ticketing.service;

import com.eazybytes.ticketing.dto.CheckinDto;
import com.eazybytes.ticketing.dto.ResponseDto;


public interface ICheckinService {

    /**
     * @param checkinDto  - will hold ticketId and busId
     * @return Returns response status and message
     */
     ResponseDto checkinValidation(CheckinDto checkinDto);

}
