package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.Phrase;
import ua.edu.ratos.dao.entity.Resource;
import ua.edu.ratos.dao.repository.PhraseRepository;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.PhraseInDto;
import ua.edu.ratos.service.dto.out.PhraseOutDto;
import ua.edu.ratos.service.transformer.mapper.PhraseMapper;
import ua.edu.ratos.service.transformer.PhraseTransformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

@Service
@AllArgsConstructor
public class PhraseService {

    private static final String PHRASE_NOT_FOUND = "The requested phrase not found, phraseId = ";

    @PersistenceContext
    private final EntityManager em;

    private final PhraseRepository phraseRepository;

    private final PhraseTransformer phraseTransformer;

    private final PhraseMapper phraseMapper;

    private final SecurityUtils securityUtils;

    //-----------------------------------------------------CRUD---------------------------------------------------------
    @Transactional
    public Long save(@NonNull final PhraseInDto dto) {
        Phrase phrase = phraseTransformer.toEntity(dto);
        return phraseRepository.save(phrase).getPhraseId();
    }

    @Transactional
    public void updatePhrase(@NonNull final Long phraseId, @NonNull final String phrase) {
        phraseRepository.findById(phraseId)
                .orElseThrow(() -> new EntityNotFoundException(PHRASE_NOT_FOUND + phraseId))
                .setPhrase(phrase);
    }

    @Transactional
    public void updateResource(@NonNull final Long phraseId, @NonNull final Long resId) {
        Phrase phrase = phraseRepository.findById(phraseId)
                .orElseThrow(() -> new EntityNotFoundException(PHRASE_NOT_FOUND + phraseId));
        phrase.clearResources();
        phrase.addResource(em.getReference(Resource.class, resId));
    }

    @Transactional
    public void deleteById(@NonNull final Long phraseId) {
        phraseRepository.findById(phraseId)
                .orElseThrow(() -> new EntityNotFoundException(PHRASE_NOT_FOUND + phraseId))
                .setDeleted(true);
    }

    //-------------------------------------------------One (for update)-------------------------------------------------
    @Transactional(readOnly = true)
    public PhraseOutDto findOneForUpdate(@NonNull final Long phraseId) {
        return phraseMapper.toDto(phraseRepository.findOneForEdit(phraseId)
                .orElseThrow(() -> new EntityNotFoundException(PHRASE_NOT_FOUND + phraseId)));
    }

    //----------------------------------------------------Staff table---------------------------------------------------
    @Transactional(readOnly = true)
    public Page<PhraseOutDto> findAllByStaffId(@NonNull final Pageable pageable) {
        return phraseRepository.findAllByStaffId(securityUtils.getAuthStaffId(), pageable).map(phraseMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PhraseOutDto> findAllByDepartmentId(@NonNull final Pageable pageable) {
        return phraseRepository.findAllByDepartmentId(securityUtils.getAuthDepId(), pageable).map(phraseMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PhraseOutDto> findAllByStaffIdAndPhraseLettersContains(@NonNull final String letters, @NonNull final Pageable pageable) {
        return phraseRepository.findAllByStaffIdAndPhraseLettersContains(securityUtils.getAuthStaffId(), letters, pageable).map(phraseMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PhraseOutDto> findAllByDepartmentIdAndPhraseLettersContains(@NonNull final String letters, @NonNull final Pageable pageable) {
        return phraseRepository.findAllByDepartmentIdAndPhraseLettersContains(securityUtils.getAuthDepId(), letters, pageable).map(phraseMapper::toDto);
    }

    //--------------------------------------------------------ADMIN-----------------------------------------------------
    @Transactional(readOnly = true)
    public Page<PhraseOutDto> findAll(@NonNull final Pageable pageable) {
        return phraseRepository.findAll(pageable).map(phraseMapper::toDto);
    }

}
