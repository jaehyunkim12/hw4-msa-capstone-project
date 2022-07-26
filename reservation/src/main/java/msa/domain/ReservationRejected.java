package msa.domain;

import java.util.*;
import lombok.*;
import msa.domain.*;
import msa.infra.AbstractEvent;

@Data
@ToString
public class ReservationRejected extends AbstractEvent {

    private Long rsvId;
    private Long roomId;
    private Integer status;

    public ReservationRejected(Reservation aggregate) {
        super(aggregate);
    }

    public ReservationRejected() {
        super();
    }
    // keep

}
