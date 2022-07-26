package msa.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import msa.config.kafka.KafkaProcessor;
import msa.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PolicyHandler {

    @Autowired
    ReservationRepository reservationRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PaymentApproved'"
    )
    public void wheneverPaymentApproved_ConfirmReserve(
        @Payload PaymentApproved paymentApproved
    ) {
        PaymentApproved event = paymentApproved;
        System.out.println(
            "\n\n##### listener ConfirmReserve : " + paymentApproved + "\n\n"
        );

        // Sample Logic //
        Reservation.confirmReserve(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PaymentCancelled'"
    )
    public void wheneverPaymentCancelled_ConfirmCancel(
        @Payload PaymentCancelled paymentCancelled
    ) {
        PaymentCancelled event = paymentCancelled;
        System.out.println(
            "\n\n##### listener ConfirmCancel : " + paymentCancelled + "\n\n"
        );

        // Sample Logic //
        Reservation.confirmCancel(event);
    }
    // keep

}
