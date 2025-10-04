package ru.yandex.practicum.filmorate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.yandex.practicum.filmorate.dal.CategoryRepository;
import ru.yandex.practicum.filmorate.storage.database.CategoryDbStorage;

@Configuration
public class CategoryConfig {
    @Bean
    @Primary
    public CategoryDbStorage categoryDbStorage(
            CategoryRepository categoryRepository
    ) {
        return new CategoryDbStorage(
                categoryRepository
        );
    }
}
