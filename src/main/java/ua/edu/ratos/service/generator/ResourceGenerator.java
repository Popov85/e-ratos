package ua.edu.ratos.service.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.config.TrackTime;
import ua.edu.ratos.dao.entity.Resource;
import ua.edu.ratos.dao.entity.Staff;
import ua.edu.ratos.dao.repository.ResourceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile({"dev"})
public class ResourceGenerator {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ResourceRepository resourceRepository;

    @TrackTime
    @Transactional
    public List<Resource> generate(int quantity) {
        List<Resource> result = new ArrayList<>();
        for (int i = 1; i <= quantity; i++) {
            Resource resource = createOne(i);
            result.add(resource);
        }
        resourceRepository.saveAll(result);
        return result;
    }

    private Resource createOne(int number) {
        Resource resource = new Resource();
        resource.setDescription("Resource "+number);
        resource.setLink("https://resources.com/"+number);
        resource.setStaff(em.getReference(Staff.class, 1L));
        return resource;
    }
}
