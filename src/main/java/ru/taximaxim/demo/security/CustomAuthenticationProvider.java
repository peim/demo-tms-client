package ru.taximaxim.demo.security;

import java.util.ArrayList;
import java.util.List;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import ru.taximaxim.demo.amq.MessageReceiver;
import ru.taximaxim.demo.context.SessionContextHolder;
import ru.taximaxim.demo.protocol.RegisterProtocol;
import ru.taximaxim.demo.protocol.RegisterResponse;
import ru.taximaxim.demo.protocol.VersionProtocol;
import ru.taximaxim.demo.utils.AmqUtils;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private VersionProtocol versionProtocol;

    @Autowired
    private RegisterProtocol registerProtocol;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private SessionContextHolder sessionContextHolder;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (AmqUtils.checkProtocolVersion(versionProtocol.getVersion())) {
            RegisterResponse response = registerProtocol.register(userName, password);
            if (response != null) {
                // Создание консьюмера на чтение из очереди INFO-context
                SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
                container.setConnectionFactory(connectionFactory);
                container.setDestinationName(response.getQueue());
                container.setMessageListener(new MessageReceiver());
                container.start();
                // Добавляем контекст в хранилище контекстов
                sessionContextHolder.addContext(response.getContext());
                // Назначение роли USER для пользователя, прошедшего аутентификацию в Мброкер
                List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
                grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
                return new CustomAuthenticationToken(userName, password, grantedAuths, response.getContext());
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
