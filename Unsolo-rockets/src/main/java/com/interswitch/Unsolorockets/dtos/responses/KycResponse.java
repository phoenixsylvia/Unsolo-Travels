package com.interswitch.Unsolorockets.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KycResponse {
    @JsonProperty("status")
    private String status;
    @JsonProperty("verified")
    private boolean Verified;

}
