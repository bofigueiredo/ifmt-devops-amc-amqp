package br.edu.ifmt.amqpdemo.produtor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/olamundo")
public class MessageController {
    
    private MessageProducer produtor;

    @Autowired
    public MessageController(MessageProducer produtor) {
        this.produtor = produtor;
    }

    @GetMapping
    public String executar() {
        produtor.enviarMensagemParaTodos(" MENSAGEM PARA TODOS ");
        produtor.enviarMensagemParaAlice(" MENSAGEM PARA ALICE ");
        produtor.enviarMensagemParaBob(" MENSAGEM PARA O BOB ");
        produtor.enviarMensagemParaCarla(" MENSAGEM PARA A CARLA ");
        produtor.enviarMensagemParaMulheres(" MENSAGEM PARA MULHERES ");
        produtor.enviarMensagemParaHomens(" MENSAGEM PARA HOMENS ");
        produtor.enviarMensagemParaMulheresAtivas(" MENSAGEM PARA MULHERES ATIVAS ");
        produtor.enviarMensagemParaAposentados(" MENSAGEM PARA APOSENTADOS ");
        produtor.enviarMensagemErrada(" MENSAGEM ROTA ERRADA (INVALID) ");
        produtor.enviarMensagemParaAliceComTTLBaixo(" MENSAGEM NÃO LIDA DEADLETTER (TTL) ");
        return "OK";
    }

}
