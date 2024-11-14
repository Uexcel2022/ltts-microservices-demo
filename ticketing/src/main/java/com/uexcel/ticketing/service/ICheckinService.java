package com.uexcel.ticketing.service;

import com.uexcel.ticketing.dto.CheckinDto;
import com.uexcel.ticketing.dto.ResponseDto;


public interface ICheckinService {

    /**
     * @param checkinDto  - will hold ticketId and busId
     * @return Returns response status and message
     */
     ResponseDto checkinValidation(CheckinDto checkinDto,String correlationId);

}
