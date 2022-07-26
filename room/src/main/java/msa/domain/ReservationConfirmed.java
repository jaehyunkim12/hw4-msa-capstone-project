package msa.domain;

import java.util.*;
import lombok.*;
import msa.domain.*;
import msa.infra.AbstractEvent;

@Data
@ToString
public class ReservationConfirmed extends AbstractEvent {

    private Long rsvId;
    private Long roomId;
    private Integer status;
    private Long payId;
    // keep

}
