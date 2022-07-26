package msa.external;

import java.util.Date;
import lombok.Data;

@Data
public class Payment {

    private Long payId;
    private Long rsvId;
    private Long roomId;
    private Boolean status;
    // keep

}
