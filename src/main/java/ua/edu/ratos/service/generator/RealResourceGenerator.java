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
import java.util.Arrays;
import java.util.List;

@Component
@Profile({"dev"})
public class RealResourceGenerator {

    private static List<String> resources = Arrays.asList(
            "https://drive.google.com/file/d/170jMMCjhW-mOswjT1mSJlZGxB3NI8iAw/preview",
            "https://drive.google.com/file/d/1tyFJiOpqdqXe27hs_PLpI9qHeX3wY3EY/preview",
            "https://drive.google.com/file/d/1GpEkKZp-o9kUzwgGdgiz7N4wHB2Ioweh/preview",
            "https://drive.google.com/file/d/11Pb51I7t2VJRiNnpVdxzJEWDhbP9HbTK/preview",
            "https://drive.google.com/file/d/1WKAt0LBP3hPvQolOIGYc99NY_WAM5sVB/preview",
            "https://drive.google.com/file/d/192DVKjniZbafQe7JQV1xNq3p9U1ppfNn/preview",
            "https://drive.google.com/file/d/13umBb6s5jk8c_d2sGC-y70Q-v6XDolZ1/preview",
            "https://drive.google.com/file/d/0B32rkhYCEyjuanpjdUFua2pfOVU/preview",
            "https://drive.google.com/file/d/0B32rkhYCEyjuX0E2WVVxSUc4c2s/preview",
            "https://drive.google.com/file/d/0B32rkhYCEyjuTFdERDFGYkxhOG8/preview",
            "https://docs.google.com/presentation/d/e/2PACX-1vSnjxEA3SOvbInEYWhELXyEsbpJH5B8Q6xORz-Lrwuaim892xxYKCMtUlzkmFgnK5ZNxXb2eCbewaxv/embed?start=false&loop=false&delayms=3000",
            "https://docs.google.com/presentation/d/e/2PACX-1vTpvTZqfdRi7NBUr-Urb_YnLYlbWu3Un_F8gb6m1QdfWKSvudz9lEg3r0abQgSae0TuYtx55xShuYAK/embed?start=false&loop=false&delayms=3000",
            "https://docs.google.com/presentation/d/e/2PACX-1vSIa_YS9RWAGBDGgRIfiQ5IQdIGPy5b3pWNVo-OpeyHZYre0KhIIdIvjoV0eENvi9136NJX3da2IPDb/embed?start=false&loop=false&delayms=3000"
    );

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ResourceRepository resourceRepository;

    @TrackTime
    @Transactional
    public List<Resource> generate() {
        List<Resource> result = new ArrayList<>();
        for (String url : resources) {
            Resource resource = createOne(url);
            result.add(resource);
        }
        return resourceRepository.saveAll(result);
    }

    private Resource createOne(String url) {
        Resource resource = new Resource();
        resource.setLink(url);
        resource.setWidth((short) 360);
        resource.setHeight((short) 240);
        resource.setType("image");
        resource.setDescription("Scheme, image, diagram, video fragment etc.");
        resource.setStaff(em.getReference(Staff.class, 1L));
        return resource;
    }
}
