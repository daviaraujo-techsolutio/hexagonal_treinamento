package com.davijaf.hexagonal.adapters.out.producer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SendCpfForValidationAdapterTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private SendCpfForValidationAdapter sendCpfForValidationAdapter;

    @Test
    void shouldSendCpfForValidation() {
        String cpf = "12345678901";

        sendCpfForValidationAdapter.send(cpf);

        verify(kafkaTemplate, times(1)).send("tp-cpf-validation", cpf);
    }

    @Test
    void shouldSendDifferentCpfForValidation() {
        String cpf = "98765432100";

        sendCpfForValidationAdapter.send(cpf);

        verify(kafkaTemplate, times(1)).send("tp-cpf-validation", cpf);
    }

    @Test
    void shouldSendCpfWithCorrectTopic() {
        String cpf = "11122233344";

        sendCpfForValidationAdapter.send(cpf);

        verify(kafkaTemplate).send("tp-cpf-validation", cpf);
    }
}
