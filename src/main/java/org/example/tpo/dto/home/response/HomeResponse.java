package org.example.tpo.dto.home.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class HomeResponse {
    private String userName;
    private List<RecentContactDto> recentContacts;
    private List<TodayEventDto> todayEvents;
}
