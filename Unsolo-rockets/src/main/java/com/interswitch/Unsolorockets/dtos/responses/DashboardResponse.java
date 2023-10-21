package com.interswitch.Unsolorockets.dtos.responses;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DashboardResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String description;
}
