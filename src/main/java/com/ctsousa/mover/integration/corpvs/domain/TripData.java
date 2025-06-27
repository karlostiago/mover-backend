package com.ctsousa.mover.integration.corpvs.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripData {
    private String plate;

    @JsonProperty("VEI_ID")
    private Long veiId;
    private String description;
    private Integer distance;
    private String distanceStr;
    private LatLng latLng;
    private LatLng endlatLng;
    private Integer maxSpeed;
    private String maxSpeedStr;
    private String dayName;
    private String timeMaxSpeed;
    private LatLng startTrip;
    private String startDate;
    private String endDate;
    private String startHour;
    private String endHour;
    private LatLng endTrip;
    private String duration;
    private String durationStop;
    private String durationStopIgnOn;
    private Integer seconds;
    private Address address;
}
