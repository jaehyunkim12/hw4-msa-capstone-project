package msa.domain;

import java.util.*;
import lombok.*;
import msa.domain.*;
import msa.infra.AbstractEvent;

@Data
@ToString
public class CancellMsgSended extends AbstractEvent {

    private Long msgId;
    private Long roomId;
    private Long rsvId;
    private String content;

    public CancellMsgSended(Message aggregate) {
        super(aggregate);
    }

    public CancellMsgSended() {
        super();
    }
    // keep

}
