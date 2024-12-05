package com.ctsousa.mover.response;

import com.ctsousa.mover.enumeration.InspectionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InspectionPhotoResponse {

    private InspectionResponse inspection;

    private InspectionStatus inspectionStatus;

    private PhotoResponse photo;
}
