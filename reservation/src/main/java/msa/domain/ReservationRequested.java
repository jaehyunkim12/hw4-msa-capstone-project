package msa.domain;

import java.util.*;
import lombok.*;
import msa.domain.*;
import msa.infra.AbstractEvent;

@Data
@ToString
public class ReservationRequested extends AbstractEvent {

    private Long rsvId;
    private Long roomId;
    private Integer status;

    public ReservationRequested(Reservation aggregate) {
        super(aggregate);
    }

    public ReservationRequested() {
        super();
    }
    // keep

}
