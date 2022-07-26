package msa.domain;

import java.util.*;
import lombok.*;
import msa.domain.*;
import msa.infra.AbstractEvent;

@Data
@ToString
public class PaymentApproved extends AbstractEvent {

    private Long payId;
    private Long rsvId;
    private Long roomId;
    private Boolean status;

    public PaymentApproved(Payment aggregate) {
        super(aggregate);
    }

    public PaymentApproved() {
        super();
    }
    // keep

}
