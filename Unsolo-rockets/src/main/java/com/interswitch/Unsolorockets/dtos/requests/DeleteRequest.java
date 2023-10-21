package com.interswitch.Unsolorockets.dtos.requests;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DeleteRequest {

    private Long tripId;
    private Long travellerId;
}
