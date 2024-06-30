package br.edu.ifmt.amqpdemo.consumidor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InvalidMessages {
    
    @RabbitListener(queues = "q.invalid")
    public void recebeMensagem(String msg) throws InterruptedException  {
        Thread.sleep(6000);
        System.out.println("INVALID ROUTE: " + msg);
    }
}
