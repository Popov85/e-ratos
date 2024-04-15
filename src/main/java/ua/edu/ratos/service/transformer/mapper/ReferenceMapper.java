package ua.edu.ratos.service.transformer.mapper;

import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Objects;

@Component
public class ReferenceMapper {

    @PersistenceContext
    private EntityManager entityManager;

    @ObjectFactory
    public <T> T map(Long id,  @TargetType Class<T> type) {
        if (Objects.isNull(id)) return null;
        return entityManager.getReference(type, id);
    }
}
