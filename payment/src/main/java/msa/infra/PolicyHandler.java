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
    PaymentRepository paymentRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='ReservationCancelRequested'"
    )
    public void wheneverReservationCancelRequested_CancelPayment(
        @Payload ReservationCancelRequested reservationCancelRequested
    ) {
        ReservationCancelRequested event = reservationCancelRequested;
        System.out.println(
            "\n\n##### listener CancelPayment : " +
            reservationCancelRequested +
            "\n\n"
        );

        // Sample Logic //
        Payment.cancelPayment(event);
    }
    // keep

}
