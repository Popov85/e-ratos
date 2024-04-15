package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.Organisation;
import ua.edu.ratos.dao.repository.OrganisationRepository;
import ua.edu.ratos.service.dto.in.OrganisationInDto;
import ua.edu.ratos.service.dto.out.OrganisationMinOutDto;
import ua.edu.ratos.service.transformer.mapper.OrganisationMapper;
import ua.edu.ratos.service.transformer.mapper.OrganisationMinMapper;

import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class OrganisationService {

    private static final String ORG_NOT_FOUND = "Requested organisation is not found, orgId = ";

    private final OrganisationRepository organisationRepository;

    private final OrganisationMapper organisationMapper;

    private final OrganisationMinMapper organisationMinMapper;


    @Transactional
    public OrganisationMinOutDto save(@NonNull final OrganisationInDto dto) {
        Organisation organisation = organisationMapper.toEntity(dto);
        organisation = organisationRepository.save(organisation);
        return organisationMinMapper.toDto(organisation);
    }

    @Transactional
    public OrganisationMinOutDto update(@NonNull final OrganisationInDto dto) {
        if (dto.getOrgId()==null)
            throw new RuntimeException("Failed to update, nullable orgId field");
        Organisation organisation = organisationMapper.toEntity(dto);
        organisation = organisationRepository.save(organisation);
        return organisationMinMapper.toDto(organisation);
    }

    @Transactional
    public void updateName(@NonNull final Long orgId, @NonNull final String name) {
        organisationRepository.findById(orgId)
                .orElseThrow(() -> new EntityNotFoundException(ORG_NOT_FOUND + orgId))
                .setName(name);
    }

    @Transactional
    public void deleteById(@NonNull final Long orgId) {
        log.warn("Organisation is to be removed, orgId= {}", orgId);
        organisationRepository.deleteById(orgId);
    }

    //---------------------------------------------------Sets-----------------------------------------------------------

    @Transactional(readOnly = true)
    public Set<OrganisationMinOutDto> findAllOrganisationsForDropDown() {
        return organisationRepository
                .findAllForDropDown()
                .stream()
                .map(organisationMinMapper::toDto)
                .collect(Collectors.toSet());
    }
}
