package msa.domain;

import java.util.*;
import lombok.*;
import msa.domain.*;
import msa.infra.AbstractEvent;

@Data
@ToString
public class ConfirmRequestMsgSended extends AbstractEvent {

    private Long msgId;
    private Long roomId;
    private Long rsvId;
    private String content;

    public ConfirmRequestMsgSended(Message aggregate) {
        super(aggregate);
    }

    public ConfirmRequestMsgSended() {
        super();
    }
    // keep

}
