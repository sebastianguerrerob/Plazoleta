package com.example.Plazoleta.infrastructure.configuration;

import com.example.Plazoleta.domain.api.IPedidoServicePort;
import com.example.Plazoleta.domain.api.IPlatoServicePort;
import com.example.Plazoleta.domain.api.IRestauranteServicePort;
import com.example.Plazoleta.domain.spi.IPedidoPersistencePort;
import com.example.Plazoleta.domain.spi.IPlatoPersistencePort;
import com.example.Plazoleta.domain.spi.IRestaurantePersistencePort;
import com.example.Plazoleta.domain.usecase.PedidoUseCase;
import com.example.Plazoleta.domain.usecase.PlatoUseCase;
import com.example.Plazoleta.domain.usecase.RestauranteUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public IRestauranteServicePort restauranteServicePort(IRestaurantePersistencePort restaurantePersistencePort) {
        return new RestauranteUseCase(restaurantePersistencePort);
    }

    @Bean
    public IPlatoServicePort platoServicePort(IPlatoPersistencePort platoPersistencePort,
                                              IRestaurantePersistencePort restaurantePersistencePort) {
        return new PlatoUseCase(platoPersistencePort, restaurantePersistencePort);
    }

    @Bean
    public IPedidoServicePort pedidoServicePort(IPedidoPersistencePort pedidoPersistencePort,
                                                IRestaurantePersistencePort restaurantePersistencePort,
                                                IPlatoPersistencePort platoPersistencePort) {
        return new PedidoUseCase(pedidoPersistencePort, restaurantePersistencePort, platoPersistencePort);
    }
}
