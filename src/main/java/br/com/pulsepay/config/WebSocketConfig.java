
package br.com.pulsepay.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.security.Principal;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        // Endereço para receber notificações
        config.enableSimpleBroker("/topic");

        // Prefixo para envio de mensagens ao servidor
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        // Endpoint de conexão WebSocket com origem padrão
        registry.addEndpoint("/ws");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {

        registration.interceptors(new ChannelInterceptor() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {

                StompHeaderAccessor accessor =
                        StompHeaderAccessor.wrap(message);

                // Validar inscrições nos canais de notificações
                if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {

                    String destination = accessor.getDestination();

                    if (destination != null &&
                            destination.startsWith("/topic/accounts/")) {

                        Principal principal = accessor.getUser();

                        String prefix = "/topic/accounts/";
                        String requestedAccountId =
                                destination.substring(prefix.length());

                        // Impedir inscrições sem usuário autenticado
                        if (principal == null) {
                            throw new AccessDeniedException(
                                    "Usuário não autenticado."
                            );
                        }

                        // Aceitar somente o canal da própria conta
                        if (requestedAccountId.isBlank()
                                || requestedAccountId.contains("/")
                                || !requestedAccountId.equals(principal.getName())) {

                            throw new AccessDeniedException(
                                    "Acesso negado ao canal de notificações."
                            );
                        }
                    }
                }

                return message;
            }
        });
    }
}
