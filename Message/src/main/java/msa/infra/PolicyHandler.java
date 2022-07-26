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
    MessageRepository messageRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='ReservationConfirmed'"
    )
    public void wheneverReservationConfirmed_SendConfirmMsg(
        @Payload ReservationConfirmed reservationConfirmed
    ) {
        ReservationConfirmed event = reservationConfirmed;
        System.out.println(
            "\n\n##### listener SendConfirmMsg : " +
            reservationConfirmed +
            "\n\n"
        );

        // Sample Logic //
        Message.sendConfirmMsg(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='ReservationCancelled'"
    )
    public void wheneverReservationCancelled_SendCancellMsg(
        @Payload ReservationCancelled reservationCancelled
    ) {
        ReservationCancelled event = reservationCancelled;
        System.out.println(
            "\n\n##### listener SendCancellMsg : " +
            reservationCancelled +
            "\n\n"
        );

        // Sample Logic //
        Message.sendCancellMsg(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='ReservationRequested'"
    )
    public void wheneverReservationRequested_SendConfirmRequesMsg(
        @Payload ReservationRequested reservationRequested
    ) {
        ReservationRequested event = reservationRequested;
        System.out.println(
            "\n\n##### listener SendConfirmRequesMsg : " +
            reservationRequested +
            "\n\n"
        );

        // Sample Logic //
        Message.sendConfirmRequesMsg(event);
    }
    // keep

}
